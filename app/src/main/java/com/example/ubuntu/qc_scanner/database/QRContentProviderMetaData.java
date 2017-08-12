package com.example.ubuntu.qc_scanner.database;

import android.net.Uri;

/**
 * Created by ubuntu on 17-8-12.
 */

public class QRContentProviderMetaData {

    public static final Uri CONTENT_URI = Uri.parse("content://com.example.ubuntu.qc_scanner.provider.ContentProvider");

    public static final String AUTHORITIES = "com.example.ubuntu.qc_scanner.provider.ContentProvider";
    public static final String DATABASE_NAME = "QRContentProvider.db";
    public static final String QR_DATA_TABLE_NAME = "qrdata";
    public static final String QR_GROUP_TABLE_NAME = "qrgroup";

    public static final int DATABASE_VERSION = 1;

    public static final class QRTableMetaData {

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITIES + "/" + QR_DATA_TABLE_NAME);
        public static final Uri GROUP_CONTENT_URI = Uri.parse("content://" + AUTHORITIES + "/" + QR_GROUP_TABLE_NAME);

        public static final String TABLE_NAME = "qrdata";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.myprovider.user";
        public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.myprovider.user";

        public static final String QRDATA_NAME = "_qrda";
        public static final String DEFAULT_SORT_ORDER = "_id desc";
    }

}
