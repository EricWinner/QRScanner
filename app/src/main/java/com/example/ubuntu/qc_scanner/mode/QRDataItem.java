package com.example.ubuntu.qc_scanner.mode;

import com.example.ubuntu.qc_scanner.fragment.QRDataFragment;

/**
 * Created by EdwardAdmin on 2017/9/17.
 */

public class QRDataItem extends IQRDataItem {

    public QRDataItem() {
    }

    public QRDataItem(String id, String groupID, String dateTime, String valleyValue, String totalValue, String qrDataCaseNumber, String qrDataCaseName, String qrDataCaseType) {
        this.id = id;
        this.groupID = groupID;
        this.dateTime = dateTime;
        this.valleyValue = valleyValue;
        this.totalValue = totalValue;
        this.caseName = qrDataCaseName;
        this.caseType = qrDataCaseType;
    }

    public QRDataItem(String foreignGroupID, String qrDataNumberID, String qrDateTime, String qrValleyValue, String qrTotalValue) {
        this.groupID = foreignGroupID;
        this.numberID = qrDataNumberID;
        this.dateTime = qrDateTime;
        this.valleyValue = qrValleyValue;
        this.totalValue = qrTotalValue;
    }

    public QRDataItem(String foreignGroupID, String qrDataNumberID, String qrDateTime, String qrValleyValue, String qrTotalValue, String qrDataCaseName, String qrDataCaseType) {
        this.groupID = foreignGroupID;
        this.numberID = qrDataNumberID;
        this.dateTime = qrDateTime;
        this.valleyValue = qrValleyValue;
        this.totalValue = qrTotalValue;
        this.caseName = qrDataCaseName;
        this.caseType = qrDataCaseType;
    }
}
