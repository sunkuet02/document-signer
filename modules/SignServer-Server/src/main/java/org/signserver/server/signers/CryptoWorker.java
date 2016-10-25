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
package org.signserver.server.signers;

import java.util.List;
import org.signserver.common.StaticWorkerStatus;
import org.signserver.common.WorkerStatus;
import org.signserver.server.IServices;

/**
 * Worker not performing any operations on its own.
 * Meant as a placeholder for a crypto token to be referenced from an other 
 * worker.
 * @author Markus Kilås
 * @version $Id: CryptoWorker.java 5781 2015-02-25 16:29:33Z netmackan $
 */
public class CryptoWorker extends NullSigner {

    private static final String WORKER_TYPE = "CryptoWorker";

    @Override
    protected boolean isNoCertificates() {
        return true;
    }

    @Override
    public WorkerStatus getStatus(List<String> additionalFatalErrors, final IServices services) {
        WorkerStatus status = super.getStatus(additionalFatalErrors, services);
        if (status instanceof StaticWorkerStatus) {
            // Adjust worker type
            ((StaticWorkerStatus) status).getInfo().setWorkerType(WORKER_TYPE);
        }
        return status;
    }

}
