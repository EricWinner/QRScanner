package com.example.ubuntu.qc_scanner.mode;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.ubuntu.qc_scanner.database.BaseColumns;
import com.example.ubuntu.qc_scanner.database.QRContentProviderMetaData;
import com.example.ubuntu.qc_scanner.fragment.QRExcelFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu on 17-8-15.
 */

public class ExcelConstant {

    private static final String TAG = "ExcelConstant";
    private Context mContext;
    public List<ExcelData> mAllQRDataLists = null;
    private QRDataCallback mCallback;

    public ExcelConstant(Context context, QRDataCallback callback) {
        mContext = context;
        mCallback = callback;
    }

    public void queryAllQRData() {
        mAllQRDataLists = new ArrayList<ExcelData>();
        String columns[] = new String[]{BaseColumns.QRDATA_ID, BaseColumns.QRDATA_FOREIGN_GROUP_ID, BaseColumns.QRDATA_NUMBER_ID,
                BaseColumns.QRDATA_DATE, BaseColumns.QRDATA_PEAK_VALUE,
                BaseColumns.QRDATA_VALLEY_VALUE, BaseColumns.QRDATA_TOTAL_AMOUNT};
        Uri mUri = QRContentProviderMetaData.QRTableMetaData.CONTENT_URI;
        Cursor cursor = mContext.getContentResolver().query(mUri, columns, null, null, null);

        int count = cursor.getCount();
        Log.d(TAG, "queryAllQRData count = " + count);
        if (cursor.moveToFirst()) {
            int id = 0;
            int foreign_id = 0;
            String numberId = null;
            String date = null;
            float peakValue = 0.0f;
            float valleyValue = 0.0f;
            float totalValue = 0.0f;

            do {
                id = cursor.getInt(cursor.getColumnIndex(BaseColumns.QRDATA_ID));
                foreign_id = cursor.getInt(cursor.getColumnIndex(BaseColumns.QRDATA_FOREIGN_GROUP_ID));
                numberId = cursor.getString(cursor.getColumnIndex(BaseColumns.QRDATA_NUMBER_ID));
                date = cursor.getString(cursor.getColumnIndex(BaseColumns.QRDATA_DATE));
                peakValue = cursor.getFloat(cursor.getColumnIndex(BaseColumns.QRDATA_PEAK_VALUE));
                valleyValue = cursor.getFloat(cursor.getColumnIndex(BaseColumns.QRDATA_VALLEY_VALUE));
                totalValue = cursor.getFloat(cursor.getColumnIndex(BaseColumns.QRDATA_TOTAL_AMOUNT));
                Log.d(TAG, "id = " + id + ",foreign_id = " + foreign_id + ",date = " + date + ",numberId = " + numberId);
                Log.d(TAG, "peakValue = " + peakValue + ",valleyValue = " + valleyValue + ",totalValue = " + totalValue);

                ExcelData data = new ExcelData(String.valueOf(id), String.valueOf(foreign_id), numberId, date, String.valueOf(peakValue), String.valueOf(valleyValue), String.valueOf(totalValue));
                mAllQRDataLists.add(data);
            } while (cursor.moveToNext());
            mCallback.writeToExcel();
        }
    }
}
