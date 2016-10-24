/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.signserver.clientws;

import org.signserver.clientws.DataGroup;

import java.util.List;

/**
 *
 * @author sun
 */
public class BulkSODRequest{
    private String ldsVersion;
    private String unicodeVersion;
    private List<SODRequestList> sodRequestLists;

    public BulkSODRequest() {
    }

    public BulkSODRequest(List<SODRequestList> singleSodRequests) {
        this(null,null,singleSodRequests);
    }

    public BulkSODRequest(String ldsVersion, String unicodeVersion, List<SODRequestList> sodRequestLists) {
        this.ldsVersion = ldsVersion;
        this.unicodeVersion = unicodeVersion;
        this.sodRequestLists = sodRequestLists;
    }

    public String getLdsVersion() {
        return ldsVersion;
    }

    public String getUnicodeVersion() {
        return unicodeVersion;
    }

    public void setLdsVersion(String ldsVersion) {
        this.ldsVersion = ldsVersion;
    }

    public void setUnicodeVersion(String unicodeVersion) {
        this.unicodeVersion = unicodeVersion;
    }

    public List<SODRequestList> getSodRequestLists() {
        return sodRequestLists;
    }

    public void setSodRequestLists(List<SODRequestList> sodRequestLists) {
        this.sodRequestLists = sodRequestLists;
    }
}
