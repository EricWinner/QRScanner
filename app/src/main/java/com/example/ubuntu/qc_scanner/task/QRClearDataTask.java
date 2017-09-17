package com.example.ubuntu.qc_scanner.task;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by EdwardAdmin on 2017/9/17.
 */

public class QRClearDataTask extends QRSuperDataTask {

    public QRClearDataTask() {
    }

    public QRClearDataTask(Context context) {
        super(context);
    }

    @Override
    public boolean isExistData() {
        return super.isExistData();
    }

    @Override
    public boolean queryAllData(Cursor cursor) {
        return super.queryAllData(cursor);
    }

    @Override
    public int clearAllData() {
        return super.clearAllData();
    }

}
