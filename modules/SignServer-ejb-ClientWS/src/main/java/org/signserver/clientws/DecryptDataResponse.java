package org.signserver.clientws;

/**
 * Created by sun on 10/24/16.
 */
public class DecryptDataResponse {
    private String result;
    private byte[] decryptedData;

    public DecryptDataResponse() {
    }

    public DecryptDataResponse(String result, byte[] decryptedData) {
        this.result = result;
        this.decryptedData = decryptedData;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public byte[] getDecryptedData() {
        return decryptedData;
    }

    public void setDecryptedData(byte[] decryptedData) {
        this.decryptedData = decryptedData;
    }
}
