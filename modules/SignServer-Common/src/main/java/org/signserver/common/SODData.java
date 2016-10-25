package org.signserver.common;

/**
 * Created by sun on 10/16/16.
 */
public class SODData {
    private String documentID;
    private byte[] sodData;

    public SODData() {
    }

    public SODData(String documentID, byte[] sodData) {
        this.documentID = documentID;
        this.sodData = sodData;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public void setSodData(byte[] sodData) {
        this.sodData = sodData;
    }

    public String getDocumentID() {
        return documentID;
    }

    public byte[] getSodData() {
        return sodData;
    }
}
