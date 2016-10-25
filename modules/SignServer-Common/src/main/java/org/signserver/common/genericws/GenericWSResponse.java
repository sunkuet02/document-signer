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
package org.signserver.common.genericws;

import java.security.cert.Certificate;
import java.util.Collection;
import org.signserver.common.GenericSignResponse;
import org.signserver.server.archive.Archivable;

/**
 * Response object used for generic WS services. Contains no
 * specific data.
 *
 * @author Philip Vendil
 * @version $Id: GenericWSResponse.java 2841 2012-10-16 08:31:40Z netmackan $
 */
public class GenericWSResponse extends GenericSignResponse {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor used during serialization
     */
    public GenericWSResponse() {
    }

    /**
     * Creates a GenericWSResponse, works as a simple VO.
     * 
     * @see org.signserver.common.ProcessRequest
     */
    public GenericWSResponse(int requestID,
            Certificate signerCertificate,
            String archiveId, Collection<? extends Archivable> archivables) {
        super(requestID, null, signerCertificate, archiveId, archivables);
    }
}
