package org.signserver.clientws;

/**
 * Created by sun on 10/24/16.
 */
public class DecryptDataResponse {
    private String result;
    private String decryptedData;

    public DecryptDataResponse() {
    }

    public DecryptDataResponse(String result, String decryptedData) {
        this.result = result;
        this.decryptedData = decryptedData;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDecryptedData() {
        return decryptedData;
    }

    public void setDecryptedData(String decryptedData) {
        this.decryptedData = decryptedData;
    }
}