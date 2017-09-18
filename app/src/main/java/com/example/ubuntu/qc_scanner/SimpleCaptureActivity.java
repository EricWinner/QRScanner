package com.example.ubuntu.qc_scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ubuntu.qc_scanner.database.BaseColumns;
import com.example.ubuntu.qc_scanner.database.QRContentProviderMetaData;
import com.example.ubuntu.qc_scanner.util.DateUtils;
import com.piotrek.customspinner.CustomSpinner;

import io.github.xudaojie.qrcodelib.CaptureActivity;

/**
 * Created by xdj on 16/9/17.
 */

public class SimpleCaptureActivity extends CaptureActivity {

    private static final String TAG = "SimpleCaptureActivity";

    private static final int OPERATION_INSERT = 0x1;
    private static final int OPERATION_UPDATE = 0x2;
    private static final int OPERATION_CHECK = 0x3;
    private static final int OPERATION_CHECK_INSERT = 0x4;
    private static final int OPERATION_CHECK_UPDATE = 0x5;

    private Activity mActivity = this;
    private AlertDialog mAlertDialog;

    private TextView mQRDataNumber;
    private EditText mQRDataGroupID;
    private EditText mQRDataValleyValue;
    private EditText mQRDataTotalValue;
    private RadioGroup mQRDataRadioGroup;
    private RadioButton mQRDataRadion1;
    private RadioButton mQRDataRadion2;
    private CustomSpinner mCustomSpinner;

    private String mDataNumberID;
    private String mDataCaseName = "";
    private String mDataCaseType = "";
    private int mDataNumberGroupID;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case OPERATION_CHECK_INSERT:
                    Log.d(TAG, "OPERATION_CHECK_INSERT !");
                    operationDatabase(OPERATION_INSERT);
                    break;
                case OPERATION_CHECK_UPDATE:
                    Log.d(TAG, "OPERATION_UPDATE !");
                    operationDatabase(OPERATION_UPDATE);
                    break;
                default:
                    break;
            }
        }
    };

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
        initDialogViews(resultString, view);
        builder.setView(view);
        builder.setOnCancelListener(cd);
        view.findViewById(R.id.dialog_cancel_choose).setOnClickListener(cd);
        view.findViewById(R.id.dialog_verity_choose).setOnClickListener(cd);
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    private void initDialogViews(String resultString, View view) {
        mQRDataNumber = (TextView) view.findViewById(R.id.qrscanner_data_number);
        mQRDataNumber.setText(resultString);
        mQRDataGroupID = (EditText) view.findViewById(R.id.input_group_name);
        mQRDataValleyValue = (EditText) view.findViewById(R.id.input_valley_value);
        mQRDataTotalValue = (EditText) view.findViewById(R.id.input_total_value);
        mQRDataRadioGroup = (RadioGroup) view.findViewById(R.id.case_name_group);
        mQRDataRadion1 = (RadioButton) view.findViewById(R.id.case_name_1);
        mQRDataRadion2 = (RadioButton) view.findViewById(R.id.case_name_2);
        mCustomSpinner = (CustomSpinner) view.findViewById(R.id.type_spinner);

        //init radiobutton
        mQRDataRadioGroup.setOnCheckedChangeListener(new RadioGroupListener());
        mQRDataRadion1.setTextColor(getResources().getColorStateList(R.color.radiobutton_txt_color));
        mQRDataRadion2.setTextColor(getResources().getColorStateList(R.color.radiobutton_txt_color));
        mDataCaseName = getString(R.string.qrdata_case_name1);
        //init spinner
        String[] types = getResources().getStringArray(R.array.type);
        mCustomSpinner.initializeStringValues(types, getString(R.string.qrdata_case_type_hint));
        mCustomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapterView.getSelectedItem().equals(getString(R.string.qrdata_case_type_hint))) {
                    //TODO
                    mDataCaseType = adapterView.getSelectedItem().toString();
                } else {
                    onNothingSelected(adapterView);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //TODO
            }
        });
    }

    class RadioGroupListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == mQRDataRadion1.getId()) {
                mDataCaseName = getString(R.string.qrdata_case_name1);
            } else if (checkedId == mQRDataRadion2.getId()) {
                mDataCaseName = getString(R.string.qrdata_case_name2);
            }
        }
    }

    private class ConfirmDialogListener implements View.OnClickListener, DialogInterface.OnCancelListener {

        public ConfirmDialogListener() {
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_verity_choose:
                    queryQRData();
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

    private void queryQRData() {
        String columns[] = new String[]{BaseColumns.QRDATA_NUMBER_ID};
        Uri mUri = QRContentProviderMetaData.QRTableMetaData.CONTENT_URI;
        Cursor cursor = mActivity.getContentResolver().query(mUri, columns, BaseColumns.QRDATA_NUMBER_ID + "= ? ", new String[]{mDataNumberID}, null);
        Log.d(TAG, "queryQRData cursor = " + cursor);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(BaseColumns.QRDATA_ID));
            int group_id = cursor.getInt(cursor.getColumnIndex(BaseColumns.QRDATA_FOREIGN_GROUP_ID));
            mDataNumberGroupID = group_id;
            String dataNumber = cursor.getString(cursor.getColumnIndex(BaseColumns.QRDATA_NUMBER_ID));
            Log.d(TAG, "queryQRData id = " + id + ",dataNumber = " + dataNumber + ",group_id = " + group_id);
            mHandler.sendEmptyMessage(OPERATION_CHECK_UPDATE);
        } else {
            mHandler.sendEmptyMessage(OPERATION_CHECK_INSERT);
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private void operationDatabase(int operationAction) {
        QRDataInfo QRDataInfo = new QRDataInfo().invoke();
        String groupId = QRDataInfo.getGroupId();
        String valleyValue = QRDataInfo.getValleyValue();
        String totalValue = QRDataInfo.getTotalValue();

        Log.d(TAG, "groupId = " + groupId + ",valleyValue = " + valleyValue + ",totalValue = " + totalValue + ",mDataNumberID = " + mDataNumberID);
        if (!TextUtils.isEmpty(groupId) && !TextUtils.isEmpty(valleyValue) && !TextUtils.isEmpty(totalValue) && !TextUtils.isEmpty(mDataCaseName)) {
            ContentValues qrGroupValues = new ContentValues();
            qrGroupValues.put(BaseColumns.QRDATA_GROUP_NAME, groupId);

            ContentValues qrDataValues = new ContentValues();
            qrDataValues.put(BaseColumns.QRDATA_DATE, DateUtils.getTodayDate());
            qrDataValues.put(BaseColumns.QRDATA_FOREIGN_GROUP_ID, groupId);
            qrDataValues.put(BaseColumns.QRDATA_NUMBER_ID, mDataNumberID);
            qrDataValues.put(BaseColumns.QRDATA_TOTAL_AMOUNT, Float.valueOf(totalValue));
            qrDataValues.put(BaseColumns.QRDATA_VALLEY_VALUE, Float.valueOf(valleyValue));
            qrDataValues.put(BaseColumns.QRDATA_CASE_NAME, mDataCaseName);
            qrDataValues.put(BaseColumns.QRDATA_CASE_TYPE, mDataCaseType);

            switch (operationAction) {
                case OPERATION_INSERT:
                    mActivity.getContentResolver().insert(QRContentProviderMetaData.QRTableMetaData.GROUP_CONTENT_URI, qrGroupValues);
                    mActivity.getContentResolver().insert(QRContentProviderMetaData.QRTableMetaData.CONTENT_URI, qrDataValues);
                    Toast.makeText(mActivity, "电表数据写入完成", Toast.LENGTH_LONG).show();
                    break;
                case OPERATION_UPDATE:
                    if (Integer.parseInt(groupId) == mDataNumberGroupID) {
                        Toast.makeText(mActivity, "此电表也在组 ：" + mDataNumberGroupID + "内，不可以添加", Toast.LENGTH_LONG).show();
                        return;
                    }
                    mActivity.getContentResolver().update(QRContentProviderMetaData.QRTableMetaData.GROUP_CONTENT_URI, qrGroupValues, null, null);
                    mActivity.getContentResolver().update(QRContentProviderMetaData.QRTableMetaData.CONTENT_URI, qrDataValues, null, null);
                    Toast.makeText(mActivity, "电表数据更新完成", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        } else {
            Toast.makeText(mActivity, "请填写全部数据", Toast.LENGTH_LONG).show();
        }
    }

    private class QRDataInfo {
        private String groupId;
        private String valleyValue;
        private String totalValue;

        public String getGroupId() {
            return groupId;
        }

        public String getValleyValue() {
            return valleyValue;
        }

        public String getTotalValue() {
            return totalValue;
        }


        public QRDataInfo invoke() {
            groupId = mQRDataGroupID.getText().toString();
            valleyValue = mQRDataValleyValue.getText().toString();
            totalValue = mQRDataTotalValue.getText().toString();
            return this;
        }
    }

}
