package com.example.ubuntu.qc_scanner.mode;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by ubuntu on 17-8-15.
 */

public class ExcelDataItem extends IQRDataItem {

    public ExcelDataItem() {
    }

    public ExcelDataItem(String id, String groupID, String numberID, String totalValue, String valleyValue, String qrDataCaseName, String qrDataCaseType) {
        this.id = id;
        this.groupID = groupID;
        this.numberID = numberID;
        this.valleyValue = valleyValue;
        this.totalValue = totalValue;
        this.caseName = qrDataCaseName;
        this.caseType = qrDataCaseType;
    }

    public ExcelDataItem(JSONObject obj) {
        this.id = obj.optString("ID");
        this.groupID = obj.optString("groupID");
        this.numberID = obj.optString("numberID");
        this.totalValue = obj.optString("totalValue");
        this.valleyValue = obj.optString("valleyValue");
        this.caseName = obj.optString("caseName");
        this.caseType = obj.optString("caseType");
    }
}
