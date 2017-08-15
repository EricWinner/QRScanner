package com.example.ubuntu.qc_scanner.fragment;


import android.app.Fragment;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ubuntu.qc_scanner.R;
import com.example.ubuntu.qc_scanner.database.BaseColumns;
import com.example.ubuntu.qc_scanner.database.QRContentProviderMetaData;
import com.example.ubuntu.qc_scanner.mode.ExcelConstant;
import com.example.ubuntu.qc_scanner.mode.ExcelData;
import com.example.ubuntu.qc_scanner.mode.QRDataCallback;
import com.example.ubuntu.qc_scanner.util.ExcelUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EdwardAdmin on 2017/7/16.
 */

public class QRExcelFragment extends Fragment implements QRDataCallback {

    private static final String TAG = "QRExcelFragment";

    private QRDataCallback mCallback;
    private Button mExcelInsertButton;
    private Button mExcelExportButton;
    private ExcelConstant mExcelConstant;
    private List<ExcelData> mAllDatas = new ArrayList<ExcelData>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View qrExcelLayout = inflater.inflate(R.layout.qrexcel_layout, container, false);
        initViews(qrExcelLayout);
        return qrExcelLayout;
    }

    public void setQRDataCallback(QRDataCallback callback) {
        mCallback = callback;
    }

    public void removeQRDataCallback() {
        mCallback = null;
    }

    private void initViews(View qrExcelLayout) {
        mExcelInsertButton = (Button) qrExcelLayout.findViewById(R.id.excel_button2);
        mExcelExportButton = (Button) qrExcelLayout.findViewById(R.id.excel_button3);

        mExcelInsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        mExcelExportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mCallback != null) {
                        mExcelConstant = new ExcelConstant(getActivity(), mCallback);
                        mExcelConstant.queryAllQRData();
                    }
                } catch (Exception e) {
                    Log.d(TAG, "fail to write excel , " + e);
                }
            }
        });
    }

    private void add() {
        ContentValues values = new ContentValues();
        values.put(BaseColumns.QRDATA_DATE, "20170812");
        values.put(BaseColumns.QRDATA_FOREIGN_GROUP_ID, 1);
        values.put(BaseColumns.QRDATA_NUMBER_ID, "3330001000100099845745");
        values.put(BaseColumns.QRDATA_PEAK_VALUE, 700.00);
        values.put(BaseColumns.QRDATA_VALLEY_VALUE, 200.00);
        values.put(BaseColumns.QRDATA_TOTAL_AMOUNT, 1000.00);
        getActivity().getContentResolver().insert(QRContentProviderMetaData.QRTableMetaData.CONTENT_URI, values);
        Toast.makeText(getActivity(), "add success !", Toast.LENGTH_LONG).show();
    }

    @Override
    public void writeToExcel() {
        if (mExcelConstant.mAllQRDataLists != null) {
            for (ExcelData excelData : mExcelConstant.mAllQRDataLists) {
                mAllDatas.add(excelData);
            }
            try {
                ExcelUtil.writeExcel(getActivity(), mAllDatas, "excel_");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
