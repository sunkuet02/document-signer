package org.signserver.clientws;

import java.security.cert.Certificate;

/**
 * Created by sun on 10/18/16.
 */
public class CryptoWorkerResponse {

    private int workerID;
    private byte[] certificate;

    public CryptoWorkerResponse() {
    }

    public CryptoWorkerResponse(int workerID, byte[] certificate) {
        this.workerID = workerID;
        this.certificate = certificate;
    }

    public int getWorkerID() {
        return workerID;
    }

    public void setWorkerID(int workerID) {
        this.workerID = workerID;
    }

    public byte[] getCertificate() {
        return certificate;
    }

    public void setCertificate(byte[] certificate) {
        this.certificate = certificate;
    }
}
