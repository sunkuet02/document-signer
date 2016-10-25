package org.signserver.clientws;

import java.util.List;

/**
 * Created by sun on 10/13/16.
 */
public class SODRequestList {
    private List<DataGroup> dataGroups;
    private String signingDocumentID;

    public SODRequestList() {
    }

    public SODRequestList(List<DataGroup> dataGroups) {
        this.dataGroups = dataGroups;
    }

    public SODRequestList(List<DataGroup> dataGroups, String signingDocumentID) {
        this.dataGroups = dataGroups;
        this.signingDocumentID = signingDocumentID;
    }

    public List<DataGroup> getDataGroups() {
        return dataGroups;
    }

    public String getSigningDocumentID() {
        return signingDocumentID;
    }

    public void setDataGroups(List<DataGroup> dataGroups) {
        this.dataGroups = dataGroups;
    }

    public void setSigningDocumentID(String signingDocumentID) {
        this.signingDocumentID = signingDocumentID;
    }
}
