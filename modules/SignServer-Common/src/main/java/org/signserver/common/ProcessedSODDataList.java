package org.signserver.common;

import java.util.List;

/**
 * Created by sun on 10/24/16.
 */
public class ProcessedSODDataList {
    private List<SODData> processedDataList;

    public ProcessedSODDataList() {
    }

    public ProcessedSODDataList(List<SODData> processedDataList) {
        this.processedDataList = processedDataList;
    }

    public List<SODData> getProcessedDataList() {
        return processedDataList;
    }

    public void setProcessedDataList(List<SODData> processedDataList) {
        this.processedDataList = processedDataList;
    }
}
