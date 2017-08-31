package com.example.ubuntu.qc_scanner.fragment;


import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ubuntu.qc_scanner.R;
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

public class QRExcelFragment extends Fragment implements QRDataCallback,FragmentPermission {

    private static final String TAG = "QRExcelFragment";

    private static final int PERMISSION_REQUEST_STORAGE = 1;

    private QRDataCallback mCallback;
    private FButton mExcelExportButton;
    private LiquidButton mActionButton;
    private ExcelConstant mExcelConstant;
    private List<ExcelData> mAllDatas = new ArrayList<ExcelData>();
    private FragmentPermission mFragmentPermission;

    private boolean permissionGranted = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView !");
        View qrExcelLayout = inflater.inflate(R.layout.qrexcel_layout, container, false);
        initViews(qrExcelLayout);
        return qrExcelLayout;
    }

    private void checkStoragePermission(){
        Log.d(TAG, "checkStoragePermission !");
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkSelfPermission");
            //申请WRITE_EXTERNAL_STORAGE权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] permissions = {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                };
                Log.d(TAG, "requestPermissions");
                requestPermissions(permissions, PERMISSION_REQUEST_STORAGE);
            }
        } else {
            //WRITE STORAGE 权限已经存在
            startWriteExcelTask();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionGranted = checkPermissionGrantResults(grantResults);
        Log.d(TAG , "permissionGranted = " + permissionGranted);
        switch (requestCode) {
            case PERMISSION_REQUEST_STORAGE: {
                if (permissionGranted) {
                    onGetPermissionsSuccess();
                } else {
                    onGetPermissionsFailure();
                }
            }
        }
    }

    private boolean checkPermissionGrantResults(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void setQRDataCallback(QRDataCallback callback) {
        mCallback = callback;
    }

    public void removeQRDataCallback() {
        mCallback = null;
    }

    private void initViews(View qrExcelLayout) {
        Log.d(TAG, "initViews");
        mExcelExportButton = (FButton) qrExcelLayout.findViewById(R.id.primary_excel_button);
        mActionButton = (LiquidButton) qrExcelLayout.findViewById(R.id.liquid_button);

        mExcelExportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
            }
        });

        mActionButton.setPourFinishListener(new LiquidButton.PourFinishListener() {
            @Override
            public void onPourFinish() {
                Toast.makeText(getActivity(), "写入成功", Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "存储路径为:QRData/qrdata.xls", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProgressUpdate(float progress) {
            }
        });
    }

    @Override
    public void writeToExcel() {
        Log.d(TAG, "writeToExcel ");
        if (mExcelConstant.mAllQRDataLists != null) {
            for (ExcelData excelData : mExcelConstant.mAllQRDataLists) {
                mAllDatas.add(excelData);
            }
            try {
                ExcelUtil.writeExcel(getActivity(), mAllDatas, "qrdata");
                mActionButton.finishPour();
            } catch (Exception e) {
                Log.e(TAG, "e = " + e);
            }
        }
    }

    public void registerFragmentPermission(FragmentPermission fragmentPermission) {
        mFragmentPermission = fragmentPermission;
    }


    public void removeFragmentPermission() {
        mFragmentPermission = null;
    }

    @Override
    public void onGetPermissionsSuccess() {
        Log.d(TAG, "onGetPermissionsSuccess !");
        startWriteExcelTask();
    }

    private void startWriteExcelTask() {
        try {
            Log.d(TAG, "mCallback = " + mCallback);
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

    @Override
    public void onGetPermissionsFailure() {
        Log.d(TAG, "onGetPermissionsFailure !");
        Toast.makeText(getActivity(), "没有写入权限", Toast.LENGTH_LONG).show();
    }
}
