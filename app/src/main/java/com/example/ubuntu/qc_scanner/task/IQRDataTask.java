package com.example.ubuntu.qc_scanner.task;

import android.database.Cursor;

import com.example.ubuntu.qc_scanner.mode.ExcelDataItem;
import com.example.ubuntu.qc_scanner.mode.IQRDataItem;

import java.util.ArrayList;


/**
 * Created by EdwardAdmin on 2017/9/17.
 */

public interface IQRDataTask {

    boolean isExistData();
    boolean queryAllData(Cursor cursor);
    Cursor getCursor();
    int clearAllData();
    void addArrayList(ArrayList<IQRDataItem> arrayList,  IQRDataItem iqrDataItem);
    ArrayList<? extends IQRDataItem> getDataArrayList();
    void addQueryLimitNumber(String number);

}
