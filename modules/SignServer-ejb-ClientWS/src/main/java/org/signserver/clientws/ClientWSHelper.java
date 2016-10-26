package org.signserver.clientws;

import org.apache.log4j.Logger;
import org.signserver.common.ServiceLocator;
import org.signserver.ejb.interfaces.IGlobalConfigurationSession;
import org.signserver.ejb.interfaces.IWorkerSession;

import javax.naming.NamingException;
import java.rmi.RemoteException;

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

    private IWorkerSession.IRemote signsession;
    public IWorkerSession.IRemote getWorkerSession() throws RemoteException {
        if (signsession == null) {
            try {
                signsession = ServiceLocator.getInstance().lookupRemote(
                        IWorkerSession.IRemote.class);
            } catch (NamingException e) {
                LOG.error("Error looking up signserver interface");
                throw new RemoteException("Error looking up signserver interface", e);
            }
        }
        return signsession;
    }
}
