package com.example.ubuntu.qc_scanner.mode;

import java.io.Serializable;

/**
 * Created by EdwardAdmin on 2017/9/17.
 */

public class IQRDataItem implements Serializable{
    protected String id;
    protected String groupID;
    protected String numberID;
    protected String dateTime;
    protected String valleyValue;
    protected String peakValue;
    protected String totalValue;
    //add
    protected String caseName;
    protected String caseType;


    public String getId() {
        return id;
    }

    public String getGroupID() {
        return groupID;
    }

    public String getNumberID() {
        return numberID;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getValleyValue() {
        return valleyValue;
    }

    public String getPeakValue() {
        return peakValue;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public String getCaseName() {
        return caseName;
    }

    public String getCaseType() {
        return caseType;
    }
}
