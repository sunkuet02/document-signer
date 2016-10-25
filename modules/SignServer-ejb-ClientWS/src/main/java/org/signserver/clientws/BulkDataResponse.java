package org.signserver.clientws;

import org.signserver.common.NotProcessedSodData;
import org.signserver.common.SODData;

import java.util.List;
import java.util.Map;

/**
 * Created by sun on 10/16/16.
 */
public class BulkDataResponse {
    private int requestId;
    private List<SODData> data;
    private List<NotProcessedSodData> notProcessedSodData;
    private byte[] signerCertificate;
    private List<Metadata> metadata;

    public BulkDataResponse() {
    }
    
    public BulkDataResponse(int requestId, List<SODData> data,List<NotProcessedSodData> notProcessedSodData , byte[] signerCertificate, List<Metadata> metadata) {
        this.requestId = requestId;
        this.data = data;
        this.notProcessedSodData = notProcessedSodData;
        this.signerCertificate = signerCertificate;
        this.metadata = metadata;
    }

    public int getRequestId() {
        return requestId;
    }

    public List<SODData> getData() {
        return data;
    }

    public byte[] getSignerCertificate() {
        return signerCertificate;
    }

    public List<Metadata> getMetadata() {
        return metadata;
    }

    public List<NotProcessedSodData> getNotProcessedSodData() {
        return notProcessedSodData;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setData(List<SODData> data) {
        this.data = data;
    }

    public void setSignerCertificate(byte[] signerCertificate) {
        this.signerCertificate = signerCertificate;
    }

    public void setMetadata(List<Metadata> metadata) {
        this.metadata = metadata;
    }

    public void setNotProcessedSodData(List<NotProcessedSodData> notProcessedSodData) {
        this.notProcessedSodData = notProcessedSodData;
    }
}
