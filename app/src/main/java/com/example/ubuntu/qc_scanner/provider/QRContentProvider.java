package com.example.ubuntu.qc_scanner.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ubuntu.qc_scanner.database.BaseColumns;
import com.example.ubuntu.qc_scanner.database.QRDataBaseHelper;

import static com.example.ubuntu.qc_scanner.database.QRContentProviderMetaData.*;

/**
 * Created by ubuntu on 17-8-12.
 */

public class QRContentProvider extends ContentProvider {

    private static final String TAG = "QRContentProvider";

    public static final int INCOMING_QRDATA = 0x1;
    public static final int INCOMING_QRDATA_SINGLE = 0x2;
    public static final int INCOMING_QRGROUP = 0x3;
    public static final int INCOMING_QRGROUP_SINGLE = 0x4;
    public static final UriMatcher uriMatcher;

    private QRDataBaseHelper mQRDataBaseHelper;
    private SQLiteDatabase mSqliteDB;
    private ContentResolver mResolver = null;
    private Context mContext;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITIES, "/qrdata",
                INCOMING_QRDATA);
        uriMatcher.addURI(AUTHORITIES, "/qrdata/#",
                INCOMING_QRDATA_SINGLE);
        uriMatcher.addURI(AUTHORITIES, "/qrgroup",
                INCOMING_QRGROUP);
        uriMatcher.addURI(AUTHORITIES, "/qrgroup/#",
                INCOMING_QRGROUP_SINGLE);
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "QRContentProvider onCreate!");
        mQRDataBaseHelper = new QRDataBaseHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        mContext = getContext();
        mResolver = mContext.getContentResolver();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqliteBuilder = new SQLiteQueryBuilder();
        mSqliteDB = mQRDataBaseHelper.getReadableDatabase();
        sqliteBuilder.setTables(QR_DATA_TABLE_NAME);
        Cursor cursor = sqliteBuilder.query(mSqliteDB, projection, selection, null, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert values = " + values);
        mSqliteDB = mQRDataBaseHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case INCOMING_QRDATA:
                long insertQRDataId = mSqliteDB.insert(QR_DATA_TABLE_NAME, "",values);
                Log.d(TAG, "insertQRDataId = " + insertQRDataId);
                if (insertQRDataId > 0) {
                    Uri rowUri = ContentUris.appendId(QRTableMetaData.CONTENT_URI.buildUpon(), insertQRDataId).build();
                    getContext().getContentResolver().notifyChange(rowUri, null);
                    mResolver.notifyChange(uri, null);
                    return rowUri;
                }
                throw new SQLException("Failed to insert row into " + uri);
            case INCOMING_QRGROUP:
                long inserQRGroupId = mSqliteDB.insert(QR_GROUP_TABLE_NAME, "",values);
                Log.d(TAG, "inserQRGroupId = " + inserQRGroupId);
                if (inserQRGroupId > 0) {
                    Uri rowUri = ContentUris.appendId(QRTableMetaData.GROUP_CONTENT_URI.buildUpon(), inserQRGroupId).build();
                    getContext().getContentResolver().notifyChange(rowUri, null);
                    mResolver.notifyChange(rowUri, null);
                    return rowUri;
                }
                throw new SQLException("Failed to insert row into " + uri);
            default:
                    return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        mSqliteDB = mQRDataBaseHelper.getWritableDatabase();
        int count = 0;

        switch(uriMatcher.match(uri)) {
            case INCOMING_QRDATA:
                count = mSqliteDB.delete(QR_DATA_TABLE_NAME, selection, selectionArgs);
                break;
            case INCOMING_QRGROUP:
                count = mSqliteDB.delete(QR_GROUP_TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }

        mResolver.notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "update values = " + values);
        mSqliteDB = mQRDataBaseHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case INCOMING_QRDATA:
                int updateQRDataId = mSqliteDB.update(QR_DATA_TABLE_NAME, values, selection, selectionArgs);
                Log.d(TAG, "updateQRDataId = " + updateQRDataId);
                if (updateQRDataId > 0) {
                    Uri rowUri = ContentUris.appendId(QRTableMetaData.CONTENT_URI.buildUpon(), updateQRDataId).build();
                    getContext().getContentResolver().notifyChange(rowUri, null);
                    return updateQRDataId;
                }
                throw new SQLException("Failed to update row into " + uri);
            case INCOMING_QRGROUP:
                int updateQRGroupId = mSqliteDB.update(QR_GROUP_TABLE_NAME, values, selection, selectionArgs);
                Log.d(TAG, "updateQRGroupId = " + updateQRGroupId);
                if (updateQRGroupId > 0) {
                    Uri rowUri = ContentUris.appendId(QRTableMetaData.GROUP_CONTENT_URI.buildUpon(), updateQRGroupId).build();
                    getContext().getContentResolver().notifyChange(rowUri, null);
                    return updateQRGroupId;
                }
                throw new SQLException("Failed to update row into " + uri);
            default:
                return 0;
        }
    }
}
