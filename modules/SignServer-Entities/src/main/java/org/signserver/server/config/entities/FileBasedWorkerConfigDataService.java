/*************************************************************************
 *                                                                       *
 *  SignServer: The OpenSource Automated Signing Server                  *
 *                                                                       *
 *  This software is free software; you can redistribute it and/or       *
 *  modify it under the terms of the GNU Lesser General Public           *
 *  License as published by the Free Software Foundation; either         *
 *  version 2.1 of the License, or any later version.                    *
 *                                                                       *
 *  See terms of license at gnu.org.                                     *
 *                                                                       *
 *************************************************************************/
package org.signserver.server.config.entities;

import java.beans.XMLDecoder;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJBException;
import org.apache.log4j.Logger;
import org.ejbca.util.Base64GetHashMap;
import org.ejbca.util.Base64PutHashMap;
import org.signserver.common.FileBasedDatabaseException;
import org.signserver.common.WorkerConfig;
import org.signserver.server.nodb.FileBasedDatabaseManager;

/**
 * Entity Service class that acts as migration layer for
 * the old Home Interface for the Worker Config Entity Bean
 * 
 * Contains about the same methods as the EJB 2 entity beans home interface.
 *
 * @version $Id: FileBasedWorkerConfigDataService.java 2965 2012-11-09 08:37:49Z netmackan $
 */
public class FileBasedWorkerConfigDataService implements IWorkerConfigDataService {

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(FileBasedWorkerConfigDataService.class);
    
    private final FileBasedDatabaseManager manager;
    private final File folder;
    private static final String PREFIX = "signerdata-";
    private static final String SUFFIX = ".dat";
    private static final int SCHEMA_VERSION = 1;

    public FileBasedWorkerConfigDataService(FileBasedDatabaseManager manager) {
        this.manager = manager;
        this.folder = manager.getDataFolder();
    }

    @Override
    public void create(int workerId, String configClassPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating worker config data, id=" + workerId);
        }

        try {
            setWorkerConfig(workerId, (WorkerConfig) this.getClass().getClassLoader().loadClass(configClassPath).newInstance());
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    /**
     * Returns the value object containing the information of the entity bean.
     * This is the method that should be used to worker config correctly
     * correctly.
     *
     */
    @SuppressWarnings("unchecked")
    private WorkerConfig getWorkerConfig(int workerId)  throws FileBasedDatabaseException {
        WorkerConfig result = null;

        WorkerConfigDataBean wcdb;
        
        try {
            synchronized (manager) {
                wcdb = loadData(workerId);
            }

            if (wcdb != null) {
                XMLDecoder decoder;
                try {
                    decoder = new XMLDecoder(new ByteArrayInputStream(wcdb.getSignerConfigData().getBytes("UTF8")));
                } catch (UnsupportedEncodingException e) {
                    throw new EJBException(e);
                }
                HashMap h = (HashMap) decoder.readObject();
                decoder.close();
                // Handle Base64 encoded string values
                HashMap data = new Base64GetHashMap(h);
                try {
                    result = new WorkerConfig();
                    result.loadData(data);
                    result.upgrade();
                } catch (Exception e) {
                    LOG.error(e);
                }
            }
        } catch (IOException ex) {
            throw new FileBasedDatabaseException("Could not load from or write data to file based database", ex);
        }

        return result;
    }

    /**
     * Method that saves the Worker Config to database.
     */
    @Override
    public void setWorkerConfig(int workerId, WorkerConfig signconf) throws FileBasedDatabaseException {
        synchronized (manager) {
            // We must base64 encode string for UTF safety
            @SuppressWarnings("unchecked")
            HashMap<Object, Object> a = new Base64PutHashMap();
            final Object o = signconf.saveData();
            if (o instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<Object, Object> data = (Map) o;
                a.putAll(data);
            } else {
                throw new IllegalArgumentException("WorkerConfig should return a Map");
            }

            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();

            java.beans.XMLEncoder encoder = new java.beans.XMLEncoder(baos);
            encoder.writeObject(a);
            encoder.close();

            try {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("WorkerConfig data: \n" + baos.toString("UTF8"));
                }
                WorkerConfigDataBean wcdb = new WorkerConfigDataBean();
                wcdb.setSignerId(workerId);
                wcdb.setSignerConfigData(baos.toString("UTF8"));
                writeData(workerId, wcdb);
            } catch (IOException ex) {
                throw new FileBasedDatabaseException("Could not load from or write data to file based database", ex);
            }
        }
    }

    /**
     * Method that removes a worker config
     * 
     * @return true if the removal was successful
     */
    @Override
    public boolean removeWorkerConfig(int workerId) throws FileBasedDatabaseException {
        boolean retval = false;
        
        try {
            synchronized (manager) {
                removeData(workerId);
                retval = loadData(workerId) == null;
            }
        } catch (IOException ex) {
            throw new FileBasedDatabaseException("Could not load from or write data to file based database", ex);
        }

        return retval;
    }

    /* (non-Javadoc)
     * @see org.signserver.ejb.IWorkerConfigDataService#getWorkerProperties(int)
     */
    @Override
    public WorkerConfig getWorkerProperties(int workerId) {
        WorkerConfig workerConfig;
        
        synchronized (manager) {
            workerConfig = getWorkerConfig(workerId);
            if (workerConfig == null) {
                create(workerId, WorkerConfig.class.getName());
                workerConfig = getWorkerConfig(workerId);
            }
        }
        
        if (workerConfig == null) {
            throw new FileBasedDatabaseException("Could not load from or write data to file based database");
        }

        return workerConfig;
    }

    private WorkerConfigDataBean loadData(final int workerId) throws IOException {
        assert Thread.holdsLock(manager);
        checkSchemaVersion();
        
        WorkerConfigDataBean result;
        final File file = new File(folder, PREFIX + workerId + SUFFIX);
        
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            int read;
            while ((read = in.read(buff)) != -1) {
                bout.write(buff, 0, read);
            }
            String data = bout.toString("UTF-8");
            result = new WorkerConfigDataBean();
            result.setSignerId(workerId);
            result.setSignerConfigData(data);
            
            XMLDecoder decoder;
            decoder = new XMLDecoder(new FileInputStream(file));
            HashMap h = (HashMap) decoder.readObject();
            decoder.close();
        } catch (FileNotFoundException ex) {
            result = null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {} // NOPMD
            }
        }
        return result;
    }

    private void writeData(int workerId, WorkerConfigDataBean dataStore) throws IOException {
        assert Thread.holdsLock(manager);
        checkSchemaVersion();
        
        final File file = new File(folder, PREFIX + workerId + SUFFIX);
        
        OutputStream out = null;
        FileOutputStream fout = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fout = new FileOutputStream(file);
            out = new BufferedOutputStream(fout);
            out.write(dataStore.getSignerConfigData().getBytes("UTF-8"));
            out.flush();
            fout.getFD().sync();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {} // NOPMD
            } else if (fout != null) {
                try {
                    fout.close();
                } catch (IOException ignored) {} // NOPMD
            }
        }
    }
    
    private void removeData(int workerId) throws IOException {
        assert Thread.holdsLock(manager);
        final File file = new File(folder, PREFIX + workerId + SUFFIX);
        file.delete();
    }
    
    private void checkSchemaVersion() {
        if (manager.getSchemaVersion() != SCHEMA_VERSION) {
            throw new FileBasedDatabaseException("Unsupported schema version: " + manager.getSchemaVersion());
        }
    }
}
