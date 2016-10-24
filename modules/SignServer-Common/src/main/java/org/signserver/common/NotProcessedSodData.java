package org.signserver.common;

/**
 * Created by sun on 10/16/16.
 */
public class NotProcessedSodData {
    private String documentID;
    private String exceptionMessage;

    public NotProcessedSodData() {
    }

    public NotProcessedSodData(String documentID) {
        this.documentID = documentID;
    }

    public NotProcessedSodData(String documentID, String exceptionMessage) {
        this.documentID = documentID;
        this.exceptionMessage = exceptionMessage;
    }

    public String getDocumentID() {
        return documentID;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
