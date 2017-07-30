package com.example.ubuntu.qc_scanner;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.ubuntu.qc_scanner.fragment.QRDataFragment;
import com.example.ubuntu.qc_scanner.fragment.QRExcelFragment;
import com.example.ubuntu.qc_scanner.fragment.QRScannerFragment;

import br.com.bloder.magic.view.MagicButton;

/**
 * Created by EdwardAdmin on 2017/7/16.
 */

public class QRCoreActivity extends BaseActivity {

    private static final String TAG = "QRCoreActivity";

    private static final int REQUEST_QR_CODE = 1;
    private static final int QRSCANNER = 0x1;
    private static final int QRDATA    = 0x2;
    private static final int QREXCEL   = 0x3;

    private QRScannerFragment mQRScannerFragment;
    private QRDataFragment    mQRDataFragment;
    private QRExcelFragment   mQRExcelFragment;

    private MagicButton mQCScannerButton;
    private MagicButton mQCDataButton;
    private MagicButton mQCExcelButton;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.qrcore_layout);

        fragmentManager = getFragmentManager();
        // 第一次启动时选中第0个tab
        mQCScannerButton = (MagicButton) this.findViewById(R.id.magic_button_scanner);
        mQCDataButton = (MagicButton) this.findViewById(R.id.magic_button_data);
        mQCExcelButton = (MagicButton) this.findViewById(R.id.magic_button_excel);

        mQCScannerButton.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("jiangsu", "111 magic_button_scanner !!");
                Intent i = new Intent(QRCoreActivity.this, SimpleCaptureActivity.class);
                QRCoreActivity.this.startActivityForResult(i, REQUEST_QR_CODE);
            }
        });
        mQCDataButton.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTabSelection(QRDATA);
            }
        });
        mQCExcelButton.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTabSelection(QREXCEL);
            }
        });
    }

    private void setTabSelection(int selection) {
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        Log.d("jiangsu", "transaction = " + transaction);
        //hideFragments(transaction);
        switch (selection) {
            case QRSCANNER:
                break;
            case QRDATA:
                if (mQRDataFragment != null) {
                    mQRDataFragment = new QRDataFragment();
                    transaction.add(R.id.content, mQRDataFragment);
                } else {
                    // 如果mQRDataFragment不为空，则直接将它显示出来
                    transaction.show(mQRDataFragment);
                }
                break;
            case QREXCEL:
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void clearSelection() {
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mQRDataFragment != null) {
            transaction.hide(mQRDataFragment);
        }
        if (mQCExcelButton != null) {
            transaction.hide(mQRExcelFragment);
        }
    }
}
