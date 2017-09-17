package com.example.ubuntu.qc_scanner.mode;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by ubuntu on 17-8-15.
 */

public class ExcelDataItem extends IQRDataItem {

    public ExcelDataItem(String id, String groupID, String numberID, String dateTime, String valleyValue, String peakValue, String totalValue) {
        this.id = id;
        this.groupID = groupID;
        this.numberID = numberID;
        this.dateTime = dateTime;
        this.valleyValue = valleyValue;
        this.peakValue = peakValue;
        this.totalValue = totalValue;
    }

    public ExcelDataItem(String id, String groupID, String dateTime, String valleyValue, String totalValue, String qrDataCaseNumber, String qrDataCaseName, String qrDataCaseType) {
        this.id = id;
        this.groupID = groupID;
        this.dateTime = dateTime;
        this.valleyValue = valleyValue;
        this.totalValue = totalValue;
        this.caseName = qrDataCaseName;
        this.caseType = qrDataCaseType;
    }

    public ExcelDataItem(JSONObject obj) {
        this.id = obj.optString("ID");
        this.groupID = obj.optString("groupID");
        this.numberID = obj.optString("numberID");
        this.dateTime = obj.optString("dateTime");
        this.valleyValue = obj.optString("valleyValue");
        this.peakValue = obj.optString("peakValue");
        this.totalValue = obj.optString("totalValue");
    }
}
