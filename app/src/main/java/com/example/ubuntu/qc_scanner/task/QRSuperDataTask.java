package com.example.ubuntu.qc_scanner.task;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.ubuntu.qc_scanner.database.BaseColumns;
import com.example.ubuntu.qc_scanner.database.QRContentProviderMetaData;
import com.example.ubuntu.qc_scanner.mode.ExcelDataItem;
import com.example.ubuntu.qc_scanner.mode.IQRDataItem;

import java.util.ArrayList;

/**
 * Created by EdwardAdmin on 2017/9/17.
 */

public class QRSuperDataTask implements IQRDataTask {

    private static final String TAG = "QRSuperDataTask";

    private Cursor mCursor;
    private Context mContext;
    private int mCount;

    public ArrayList<? extends IQRDataItem> mAllQRDataLists = null;

    public QRSuperDataTask() {

    }
    public QRSuperDataTask(Context context) {
        mContext = context;
    }


    @Override
    public boolean isExistData() {
        String columns[] = new String[]{BaseColumns.QRDATA_ID, BaseColumns.QRDATA_FOREIGN_GROUP_ID,
                BaseColumns. QRDATA_NUMBER_ID, BaseColumns.QRDATA_DATE,
                BaseColumns.QRDATA_TOTAL_AMOUNT, BaseColumns.QRDATA_VALLEY_VALUE,
                BaseColumns.QRDATA_CASE_NAME, BaseColumns.QRDATA_CASE_TYPE
        };
        Uri mUri = QRContentProviderMetaData.QRTableMetaData.CONTENT_URI;
        Cursor cursor = mContext.getContentResolver().query(mUri, columns, null, null, null);

        mCursor = cursor;
        mCount = cursor.getCount();
        Log.d(TAG, "queryAllQRData count = " + mCount);
        return mCount == 0 ? false : true;
    }

    @Override
    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    public boolean queryAllData(Cursor cursor) {
        if (cursor.moveToFirst()) {
            int id = 0;
            int foreign_id = 0;
            String numberId = null;
            String date = null;
            float valleyValue = 0.0f;
            float totalValue = 0.0f;
            String case_name;
            String case_type;

            do {
                id = cursor.getInt(cursor.getColumnIndex(BaseColumns.QRDATA_ID));
                foreign_id = cursor.getInt(cursor.getColumnIndex(BaseColumns.QRDATA_FOREIGN_GROUP_ID));
                numberId = cursor.getString(cursor.getColumnIndex(BaseColumns.QRDATA_NUMBER_ID));
                date = cursor.getString(cursor.getColumnIndex(BaseColumns.QRDATA_DATE));
                totalValue = cursor.getFloat(cursor.getColumnIndex(BaseColumns.QRDATA_TOTAL_AMOUNT));
                valleyValue = cursor.getFloat(cursor.getColumnIndex(BaseColumns.QRDATA_VALLEY_VALUE));
                case_name = cursor.getString(cursor.getColumnIndex(BaseColumns.QRDATA_CASE_NAME));
                case_type = cursor.getString(cursor.getColumnIndex(BaseColumns.QRDATA_CASE_TYPE));
                Log.d(TAG, "id = " + id + ",foreign_id = " + foreign_id + ",date = " + date + ",numberId = " + numberId );
                Log.d(TAG, "valleyValue = " + valleyValue + ",totalValue = " + totalValue + ",case_name = " + case_name + ",case_type = " + case_type);

                ExcelDataItem data = new ExcelDataItem(String.valueOf(id), String.valueOf(foreign_id), date, String.valueOf(valleyValue), String.valueOf(totalValue), case_name,case_type);

            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
            mCursor = null;
        }
        return true;
    }

    @Override
    public int clearAllData() {
        Uri dataUri = QRContentProviderMetaData.QRTableMetaData.CONTENT_URI;
        Uri groupUri = QRContentProviderMetaData.QRTableMetaData.GROUP_CONTENT_URI;
        int dataCount = mContext.getContentResolver().delete(dataUri,null,null);
        int groupCount = mContext.getContentResolver().delete(groupUri,null,null);
        int allCount = dataCount + groupCount;
        clearQRDataList();
        Log.d(TAG, "clearAllData allCount = " + allCount);
        Toast.makeText(mContext, "清除数据成功！", Toast.LENGTH_SHORT).show();
        return allCount;
    }

    @Override
    public void addArrayList(ArrayList<? extends IQRDataItem> arrayList) {
        mAllQRDataLists = arrayList;
    }

    @Override
    public ArrayList<? extends IQRDataItem> getDataArrayList() {
        return mAllQRDataLists == null ? null : mAllQRDataLists;
    }

    private void clearQRDataList() {
        if (mAllQRDataLists != null && mAllQRDataLists.size() > 0) {
            mAllQRDataLists.clear();
        }
    }
}
