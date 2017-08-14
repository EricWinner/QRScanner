package com.example.ubuntu.qc_scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ubuntu.qc_scanner.database.BaseColumns;
import com.example.ubuntu.qc_scanner.database.QRContentProviderMetaData;
import com.example.ubuntu.qc_scanner.util.DateUtils;

import io.github.xudaojie.qrcodelib.CaptureActivity;

/**
 * Created by xdj on 16/9/17.
 */

public class SimpleCaptureActivity extends CaptureActivity {

    private static final String TAG = "SimpleCaptureActivity";

    private static final int OPERATION_INSERT = 0x1;
    private static final int OPERATION_UPDATE = 0x2;

    private Activity mActivity = this;
    private AlertDialog mAlertDialog;

    private TextView mQRDataNumber;
    private EditText mQRDataGroupID;
    private EditText mQRDataValleyValue;
    private EditText mQRDataPeakValue;
    private EditText mQRDataTotalValue;

    private String mDataNumberID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void handleResult(String resultString) {
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(mActivity, io.github.xudaojie.qrcodelib.R.string.scan_failed, Toast.LENGTH_SHORT).show();
            restartPreview();
        } else {
            createCustomDialog(resultString);
        }
    }

    private void createCustomDialog(String resultString) {
        Log.d(TAG, "createCustomDialog resultString = " + resultString);
        mDataNumberID = resultString;
        ConfirmDialogListener cd = new ConfirmDialogListener();
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        View view = View.inflate(mActivity, R.layout.qrscanner_data_dialog, null);
        mQRDataNumber = (TextView) view.findViewById(R.id.qrscanner_data_number);
        mQRDataNumber.setText(resultString);

        mQRDataGroupID     = (EditText) view.findViewById(R.id.input_group_name);
        mQRDataValleyValue = (EditText) view.findViewById(R.id.input_valley_value);
        mQRDataPeakValue   = (EditText) view.findViewById(R.id.input_peak_value);
        mQRDataTotalValue  = (EditText) view.findViewById(R.id.input_total_value);

        builder.setView(view);
        builder.setOnCancelListener(cd);
        view.findViewById(R.id.dialog_cancel_choose).setOnClickListener(cd);
        view.findViewById(R.id.dialog_verity_choose).setOnClickListener(cd);
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    private class ConfirmDialogListener implements View.OnClickListener, DialogInterface.OnCancelListener {

        public ConfirmDialogListener() {
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_verity_choose:
                    saveQRData();
                    mAlertDialog.dismiss();
                    break;
                case R.id.dialog_cancel_choose:
                    mAlertDialog.dismiss();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onCancel(DialogInterface dialog) {
        }
    }

    private void saveQRData() {
        boolean isFirstData = queryQRData();
        if (!isFirstData) {
            updateData();
            return;
        }
        operationDatabase(OPERATION_INSERT);
    }

    private boolean queryQRData() {
        String columns[] = new String[]{BaseColumns.QRDATA_NUMBER_ID};
        Uri mUri = QRContentProviderMetaData.QRTableMetaData.CONTENT_URI;
        Cursor cursor = mActivity.getContentResolver().query(mUri, columns, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(BaseColumns.QRDATA_ID));
            Log.d(TAG,"queryQRData id = " + id);
            return false;
        }
        return true;
    }

    private void updateData() {
        operationDatabase(OPERATION_UPDATE);
    }

    private void operationDatabase(int operationAction) {
        QRDataInfo QRDataInfo = new QRDataInfo().invoke();
        String groupId = QRDataInfo.getGroupId();
        String valleyValue = QRDataInfo.getValleyValue();
        String peakValue = QRDataInfo.getPeakValue();
        String totalValue = QRDataInfo.getTotalValue();

        if (groupId != null && valleyValue != null && peakValue != null && totalValue != null) {
            ContentValues qrGroupValues = new ContentValues();
            qrGroupValues.put(BaseColumns.QRDATA_GROUP_NAME, groupId);

            ContentValues qrDataValues = new ContentValues();
            qrDataValues.put(BaseColumns.QRDATA_DATE, DateUtils.getCurrentDate());
            qrDataValues.put(BaseColumns.QRDATA_FOREIGN_GROUP_ID, groupId);
            qrDataValues.put(BaseColumns.QRDATA_NUMBER_ID, mDataNumberID);
            qrDataValues.put(BaseColumns.QRDATA_PEAK_VALUE, Float.valueOf(valleyValue));
            qrDataValues.put(BaseColumns.QRDATA_VALLEY_VALUE, Float.valueOf(peakValue));
            qrDataValues.put(BaseColumns.QRDATA_TOTAL_AMOUNT, Float.valueOf(totalValue));

            switch (operationAction){
                case OPERATION_INSERT:
                    mActivity.getContentResolver().insert(QRContentProviderMetaData.QRTableMetaData.GROUP_CONTENT_URI, qrGroupValues);
                    mActivity.getContentResolver().insert(QRContentProviderMetaData.QRTableMetaData.CONTENT_URI, qrDataValues);
                    break;
                case OPERATION_UPDATE:
                    mActivity.getContentResolver().update(QRContentProviderMetaData.QRTableMetaData.GROUP_CONTENT_URI, qrGroupValues, null, null);
                    mActivity.getContentResolver().update(QRContentProviderMetaData.QRTableMetaData.CONTENT_URI, qrDataValues, null, null);
                    break;
                default:
                    break;
            }
        } else {
            Toast.makeText(mActivity, "请填写全部数据",Toast.LENGTH_LONG).show();
        }
    }

    private class QRDataInfo {
        private String groupId;
        private String valleyValue;
        private String peakValue;
        private String totalValue;

        public String getGroupId() {
            return groupId;
        }

        public String getValleyValue() {
            return valleyValue;
        }

        public String getPeakValue() {
            return peakValue;
        }

        public String getTotalValue() {
            return totalValue;
        }

        public QRDataInfo invoke() {
            groupId = mQRDataGroupID.getText().toString();
            valleyValue = mQRDataValleyValue.getText().toString();
            peakValue = mQRDataPeakValue.getText().toString();
            totalValue = mQRDataTotalValue.getText().toString();
            return this;
        }
    }

}
