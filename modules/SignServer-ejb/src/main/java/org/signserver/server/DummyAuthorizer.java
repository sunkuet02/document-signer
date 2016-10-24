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
package org.signserver.server;

import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import org.ejbca.util.CertTools;
import org.signserver.common.GenericSignRequest;
import org.signserver.common.IllegalRequestException;
import org.signserver.common.ProcessRequest;
import org.signserver.common.RequestContext;
import org.signserver.common.SignServerException;
import org.signserver.common.WorkerConfig;

/**
 * Dummy authorizer used for testing and demonstration purposes
 * 
 * @author Philip Vendil 24 nov 2007
 * @version $Id: DummyAuthorizer.java 3452 2013-04-20 21:32:59Z netmackan $
 */
public class DummyAuthorizer implements IAuthorizer {

    /**
     * @see org.signserver.server.IAuthorizer#init(int, org.signserver.common.WorkerConfig, javax.persistence.EntityManager)
     */
    @Override
    public void init(int workerId, WorkerConfig config, EntityManager em)
            throws SignServerException {

        if (config.getProperties().getProperty("TESTAUTHPROP") == null) {
            throw new SignServerException("Error initializing DummyAuthorizer, TESTAUTHPROP must be set");
        }

    }
    
    @Override
    public List<String> getFatalErrors() {
        return Collections.emptyList();
    }

    /**
     * @see org.signserver.server.IAuthorizer#isAuthorized(ProcessRequest, RequestContext)
     */
    @Override
    public void isAuthorized(ProcessRequest request, RequestContext requestContext)
            throws SignServerException, IllegalRequestException {

        String clientIP = (String) requestContext.get(RequestContext.REMOTE_IP);
        X509Certificate clientCert = (X509Certificate) requestContext.get(RequestContext.CLIENT_CERTIFICATE);

        if (clientIP != null && !clientIP.equals("1.2.3.4")) {
            throw new IllegalRequestException("Not authorized");
        }
        if (clientCert != null && (CertTools.stringToBCDNString(clientCert.getSubjectDN().toString()).equals("CN=timestamptest,O=PrimeKey Solution AB")
                || CertTools.stringToBCDNString(clientCert.getSubjectDN().toString()).equals("CN=Signer 4,OU=Testing,O=SignServer,C=SE")
                || clientCert.getSerialNumber().toString(16).equalsIgnoreCase("58ece0453711fe20"))) {
            throw new IllegalRequestException("Not authorized");
        }
        if (request instanceof GenericSignRequest && ((GenericSignRequest) request).getRequestID() != 1) {
            throw new IllegalRequestException("Not authorized");
        }
    }
}
