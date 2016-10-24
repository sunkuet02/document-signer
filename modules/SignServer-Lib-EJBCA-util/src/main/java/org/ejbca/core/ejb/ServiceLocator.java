package org.ejbca.core.ejb;

import java.net.URL;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.Context;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;
import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
//import javax.mail.Session;

/**
 * A simple implementation of the ServiceLocator/HomeFactory J2EE Pattern.
 * {@link http://developer.java.sun.com/developer/restricted/patterns/ServiceLocator.html}
 *
 * It is used to look up JNDI related resources such as EJB homes, datasources, ...
 * @version $Id: ServiceLocator.java 5585 2008-05-01 20:55:00Z anatom $
 */
public class ServiceLocator {

    /** ejb home cache */
    private transient Map ejbHomes = Collections.synchronizedMap(new HashMap());

    /** the jndi context */
    private transient Context ctx;

    /** the singleton instance */
    private static transient ServiceLocator instance;

    /**
     * Create a new service locator object.
     * @throws ServiceLocatorException if the context failed to be initialized
     */
    private ServiceLocator() throws ServiceLocatorException {
        try {
            this.ctx = new InitialContext();
        } catch (NamingException e){
            throw new ServiceLocatorException(e);
        }
    }

    /**
     * return the singleton instance
     * @return the singleton instance
     * @throws ServiceLocatorException if the instance could not be initialized the first time
     */
    public static final ServiceLocator getInstance() throws ServiceLocatorException {
        // synchronization is intentionally left out. It 'should' not have dramatic
        // consequences as it is not that destructive.
        if (instance == null){
            instance = new ServiceLocator();
        }
        return instance;
    }

    /**
     * return the ejb local home.
     * clients need to cast to the type of EJBHome they desire
     * @param jndiHomeName the jndi home name matching the requested local home.
     * @return the Local EJB Home corresponding to the home name
     */
    public EJBLocalHome getLocalHome(String jndiHomeName) throws ServiceLocatorException {
        EJBLocalHome home = (EJBLocalHome)ejbHomes.get(jndiHomeName);
        if (home == null) {
            home = (EJBLocalHome)getObject(jndiHomeName);
            ejbHomes.put(jndiHomeName, home);
        }
        return home;
    }

    /**
     * return the ejb remote home.
     * clients need to cast to the type of EJBHome they desire
     * @param jndiHomeName the jndi home name matching the requested remote home.
     * @return the Local EJB Home corresponding to the home name
     */
    public EJBHome getRemoteHome(String jndiHomeName, Class className) throws ServiceLocatorException {
        EJBHome home = (EJBHome)ejbHomes.get(className);
        if (home == null) {
            Object objref = getObject(jndiHomeName);
            home = (EJBHome) PortableRemoteObject.narrow(objref, className);
            ejbHomes.put(className, home);
        }
        return home;
    }

    /**
     * return the datasource object corresponding the the env entry name
     * @return the DataSource corresponding to the env entry name parameter
     * @throws ServiceLocatorException if the lookup fails
     */
    public DataSource getDataSource(String dataSourceName) throws ServiceLocatorException {
        return (DataSource)getObject(dataSourceName);
    }

    /**
     * return the URL object corresponding to the env entry name
     * @param envName the env entry name
     * @return the URL value corresponding to the env entry name.
     * @throws ServiceLocatorException if the lookup fails
     */
    public URL getUrl(String envName) throws ServiceLocatorException {
        return (URL)getObject(envName);
    }

    /**
     * return a boolean value corresponding to the env entry
     * @param envName the env entry name
     * @return the boolean value corresponding to the env entry.
     * @throws ServiceLocatorException if the lookup fails
     */
    public boolean getBoolean(String envName) throws ServiceLocatorException {
        return ((Boolean)getObject(envName)).booleanValue();
    }

    /**
     * return a string value corresponding to the env entry
     * @param envName the env entry name
     * @return the boolean value corresponding to the env entry.
     * @throws ServiceLocatorException if the lookup fails
     */
    public String getString(String envName) throws ServiceLocatorException {
        String ret = null;
        try {
            ret = (String)getObject(envName);        	
        } catch (ServiceLocatorException e) {
        	if (e.getCause() instanceof NameNotFoundException) {
				// ignore this and return null, otherwise we can not have empty values in Glassfish
        		ret = null;
			}
        }
        return ret;
    }

    /**
     * return a mail session corresponding to the env entry
     * @param envName the env entry name
     * @return the mail session corresponding to the env entry.
     * @throws ServiceLocatorException if the lookup fails
     */
//    public Session getMailSession(String envName) throws ServiceLocatorException {
//        return (Session)getObject(envName);
//    }

    /**
     * return a known java object corresponding to the env entry
     * @param envName the env entry name
     * @return the java object corresponding to the env entry
     * @throws ServiceLocatorException if the lookup fails
     */
    public Object getObject(String envName) throws ServiceLocatorException {
        try {
            return ctx.lookup(envName);
        } catch (NamingException e) {
            throw new ServiceLocatorException(e);
        }
    }
}
