package org.signserver.common;

import java.util.List;

/**
 * Created by sun on 10/24/16.
 */
public class NotProcessedSodDataList {
    private  List<NotProcessedSodData> notProcessedDataList;

    public NotProcessedSodDataList(List<NotProcessedSodData> notProcessedDataList) {
        this.notProcessedDataList = notProcessedDataList;
    }

    public NotProcessedSodDataList() {
    }

    public List<NotProcessedSodData> getNotProcessedDataList() {
        return notProcessedDataList;
    }

    public void setNotProcessedDataList(List<NotProcessedSodData> notProcessedDataList) {
        this.notProcessedDataList = notProcessedDataList;
    }
}
