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
import com.gospelware.liquidbutton.LiquidButton;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;

/**
 * Created by EdwardAdmin on 2017/7/16.
 */

public class QRExcelFragment extends Fragment implements QRDataCallback {

    private static final String TAG = "QRExcelFragment";

    private QRDataCallback mCallback;
    private FButton mExcelExportButton;
    private LiquidButton mActionButton;
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
        mExcelExportButton = (FButton) qrExcelLayout.findViewById(R.id.primary_excel_button);
        mActionButton = (LiquidButton) qrExcelLayout.findViewById(R.id.liquid_button);

        mExcelExportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mCallback != null) {
                        mExcelConstant = new ExcelConstant(getActivity(), mCallback);
                        if (mExcelConstant.checkQRData()) {
                            mExcelConstant.queryAllQRData();
                            mExcelExportButton.setVisibility(View.GONE);
                            mActionButton.setVisibility(View.VISIBLE);
                            mActionButton.startPour();
                            mActionButton.setAutoPlay(true);
                        } else {
                            Toast.makeText(getActivity(), "没有可以导出的数据！", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Log.d(TAG, "fail to write excel , " + e);
                }
            }
        });

        mActionButton.setPourFinishListener(new LiquidButton.PourFinishListener() {
            @Override
            public void onPourFinish() {
                Toast.makeText(getActivity(), "写入成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProgressUpdate(float progress) {
            }
        });
    }

    @Override
    public void writeToExcel() {
        if (mExcelConstant.mAllQRDataLists != null) {
            for (ExcelData excelData : mExcelConstant.mAllQRDataLists) {
                mAllDatas.add(excelData);
            }
            try {
                ExcelUtil.writeExcel(getActivity(), mAllDatas, "excel_");
                mActionButton.finishPour();
            } catch (Exception e) {
                Log.e(TAG, "e = " + e);
            }
        }
    }
}
