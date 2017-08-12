package com.example.ubuntu.qc_scanner.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
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

    public static final int INCOMING_USER_COLLECTION = 1;
    public static final int INCOMING_USER_SINGLE = 2;
    public static final UriMatcher uriMatcher;

    private QRDataBaseHelper mQRDataBaseHelper;
    private SQLiteDatabase mSqliteDB;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITIES, "/qrdata",
                INCOMING_USER_COLLECTION);
        uriMatcher.addURI(AUTHORITIES, "/qrdata/#",
                INCOMING_USER_SINGLE);
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "QRContentProvider onCreate!");
        mQRDataBaseHelper = new QRDataBaseHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqliteBuilder = new SQLiteQueryBuilder();
        mSqliteDB = mQRDataBaseHelper.getReadableDatabase();
        sqliteBuilder.setTables(QR_DATA_TABLE_NAME);
        Cursor cursor = sqliteBuilder.query(mSqliteDB, projection, selection, null, null,null, sortOrder);
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
        mSqliteDB = mQRDataBaseHelper.getWritableDatabase();
        Log.d(TAG, "insert values = " + values);
        long insertId = mSqliteDB.insert(QR_DATA_TABLE_NAME, "",values);
        Log.d(TAG, "insertId = " + insertId);
        if (insertId > 0) {
            Uri rowUri = ContentUris.appendId(QRTableMetaData.CONTENT_URI.buildUpon(), insertId).build();
            getContext().getContentResolver().notifyChange(rowUri, null);
            return rowUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
