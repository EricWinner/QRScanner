package com.example.ubuntu.qc_scanner.task;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.ubuntu.qc_scanner.database.BaseColumns;
import com.example.ubuntu.qc_scanner.database.QRContentProviderMetaData;
import com.example.ubuntu.qc_scanner.mode.ExcelDataItem;
import com.example.ubuntu.qc_scanner.mode.IQRDataItem;
import com.example.ubuntu.qc_scanner.mode.QRDataCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu on 17-8-15.
 */

public class QRExcelDataTask extends QRSuperDataTask{

    private static final String TAG = "QRExcelDataTask";
    private Cursor mCursor;

    public ArrayList<IQRDataItem> mAllQRDataLists = null;

    public QRExcelDataTask(Context context, QRDataCallback callback) {
        super(context,callback);
        mAllQRDataLists = new ArrayList<IQRDataItem>();
    }

    public QRExcelDataTask() {
    }

    @Override
    public boolean isExistData() {
        return super.isExistData();
    }
    @Override
    public boolean queryAllData(Cursor cursor) {
        return super.queryAllData(cursor);
    }


}
