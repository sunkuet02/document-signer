/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.signserver.clientws;

import org.signserver.common.NotProcessedSodData;
import org.signserver.common.SODData;

import java.util.List;
import java.util.Map;

/**
 *
 * @author sun
 */
public class BulkSODResponse extends BulkDataResponse{

    public BulkSODResponse() {
    }

    public BulkSODResponse(int requestId, List<SODData> data, List<NotProcessedSodData>notProcessedSodData, byte[] signerCertificate, List<Metadata> metadata) {
        super(requestId, data, notProcessedSodData, signerCertificate, metadata);
    }
    
}
