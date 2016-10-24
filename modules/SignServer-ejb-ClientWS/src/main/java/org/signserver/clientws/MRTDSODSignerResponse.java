package org.signserver.clientws;

/**
 * Created by sun on 10/24/16.
 */
public class MRTDSODSignerResponse {
    private int workerID;
    private byte[] publicKey;

    public MRTDSODSignerResponse() {
    }

    public MRTDSODSignerResponse(int workerID, byte[] publicKey) {
        this.workerID = workerID;
        this.publicKey = publicKey;
    }

    public int getWorkerID() {
        return workerID;
    }

    public void setWorkerID(int workerID) {
        this.workerID = workerID;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }
}
