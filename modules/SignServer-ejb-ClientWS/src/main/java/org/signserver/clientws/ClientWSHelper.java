package org.signserver.clientws;

import org.apache.log4j.Logger;
import org.signserver.common.GlobalConfiguration;
import org.signserver.common.ServiceLocator;
import org.signserver.common.WorkerConfig;
import org.signserver.ejb.interfaces.IGlobalConfigurationSession;
import org.signserver.ejb.interfaces.IWorkerSession;

import javax.ejb.EJB;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sun on 10/26/16.
 */
public class ClientWSHelper {
    public final String CRYPTOWORKER_CLASSPATH = "org.signserver.server.signers.CryptoWorker";
    public final String PKCS11_CRYPTOWORKER_SIGNERTOKEN_CLASSPATH = "org.signserver.server.cryptotokens.PKCS11CryptoToken";
    public final String MRTDSODSIGNER_CLASSPATH = "org.signserver.module.mrtdsodsigner.MRTDSODSigner";
    public final String SCOPE_GLOB = "GLOB.";
    public final String WORKER_TEXT = "WORKER";
    public final String CLASSPATH_TEXT=".CLASSPATH";
    public final String SIGNERTOKEN_CLASSPATH_TEXT=".SIGNERTOKEN.CLASSPATH";

    private static Logger LOG = org.apache.log4j.Logger.getLogger(ClientWSHelper.class);

    public ClientWSHelper() {
    }

    private IGlobalConfigurationSession.IRemote globalConfig;
    public IGlobalConfigurationSession.IRemote getGlobalConfigurationSession()
            throws RemoteException {
        if (globalConfig == null) {
            try {
                globalConfig = ServiceLocator.getInstance().lookupRemote(
                        IGlobalConfigurationSession.IRemote.class);
            } catch (NamingException e) {
                LOG.error("Error instanciating the GlobalConfigurationSession.", e);
                throw new RemoteException("Error instanciating the GlobalConfigurationSession", e);
            }
        }
        return globalConfig;
    }

    public String removeWorker(int workerId, IWorkerSession.ILocal workerSession) {
        try {
            removeGlobalProperties(workerId);
            removeWorkerProperty(workerId, workerSession);
        } catch (RemoteException e) {
            return "failed";
        } catch (Exception e) {
            return "failed";
        }
        return "success";
    }


    private void removeGlobalProperties(int workerid) throws RemoteException, Exception {
        GlobalConfiguration gc = getGlobalConfigurationSession().getGlobalConfiguration();
        Enumeration<String> en = gc.getKeyEnumeration();
        while (en.hasMoreElements()) {
            String key = en.nextElement();
            if (key.toUpperCase().startsWith("GLOB.WORKER" + workerid)) {

                key = key.substring("GLOB.".length());
                if (getGlobalConfigurationSession().removeProperty(GlobalConfiguration.SCOPE_GLOBAL, key)) {
                    LOG.info("Successfully removed the global property :" + key);
                } else {
                    LOG.info("Couldn't remove the global property :" + key);
                }
            }
        }
    }

    private void removeWorkerProperty(int workerId,IWorkerSession.ILocal workerSession ) throws RemoteException, Exception {
        removeGlobalProperties(workerId);

        WorkerConfig wc = workerSession.getCurrentWorkerConfig(workerId);
        Iterator<Object> iter = wc.getProperties().keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            if (workerSession.removeWorkerProperty(workerId, key)) {
                LOG.info("  Property '" + key + "' removed.");
            } else {
                LOG.info("  Error, the property '" + key + "' couldn't be removed.");
            }
        }
    }
}
