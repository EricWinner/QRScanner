package com.example.ubuntu.qc_scanner.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ubuntu on 17-8-12.
 */

public class QRDataBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "QRDataBaseHelper";

    public QRDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public QRDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG, "init table begin !");
        //init group table
        db.execSQL("CREATE TABLE " + QRContentProviderMetaData.QR_GROUP_TABLE_NAME + "("+
                    BaseColumns.QRDATA_GROUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    BaseColumns.QRDATA_GROUP_NAME + " TEXT);");

        //init data table
        db.execSQL("CREATE TABLE " + QRContentProviderMetaData.QR_DATA_TABLE_NAME + "(" +
                    BaseColumns.QRDATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    BaseColumns.QRDATA_FOREIGN_GROUP_ID + " INTEGER ," +
                    BaseColumns.QRDATA_DATE + " TEXT ," +
                    BaseColumns.QRDATA_PEAK_VALUE + " FLOAT ," +
                    BaseColumns.QRDATA_VALLEY_VALUE + " FLOAT ," +
                    BaseColumns.QRDATA_TOTAL_AMOUNT + " FLOAT ," +
                    "FOREIGN KEY(" + BaseColumns.QRDATA_FOREIGN_GROUP_ID + ") REFERENCES " +
                    QRContentProviderMetaData.QR_DATA_TABLE_NAME + "(" +
                    BaseColumns.QRDATA_GROUP_ID + "));");

        Log.d(TAG, "init table end !");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade !!");
    }
}
