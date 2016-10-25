package org.signserver.common;

import java.security.cert.Certificate;
import java.util.List;

/**
 * Created by sun on 10/16/16.
 */
public class BulkSodSignResponse extends GenericBulkSignResponse{
    private static final long serialVersionUID = 2L;

    public BulkSodSignResponse() {
        this.tag = RequestAndResponseManager.RESPONSETYPE_BULKSODSIGNRESPONSE;
    }

    public BulkSodSignResponse(int requestID, ProcessedSODDataList processedDataList, NotProcessedSodDataList notProcessedSodDataList,Certificate signerCertificate) {
        super(requestID, processedDataList, notProcessedSodDataList,signerCertificate);
        this.tag = RequestAndResponseManager.RESPONSETYPE_BULKSODSIGNRESPONSE;
    }    
}
