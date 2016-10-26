package org.signserver.clientws;

/**
 * Created by sun on 10/26/16.
 */
public class EncryptedMessageResponse {
    private String result;
    private byte[] encryptedMessage;

    public EncryptedMessageResponse() {
    }

    public EncryptedMessageResponse(String result, byte[] encryptedMessage) {
        this.result = result;
        this.encryptedMessage = encryptedMessage;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public byte[] getEncryptedMessage() {
        return encryptedMessage;
    }

    public void setEncryptedMessage(byte[] encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }
}
