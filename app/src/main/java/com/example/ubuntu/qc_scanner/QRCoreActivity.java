package com.example.ubuntu.qc_scanner;

import android.app.Fragment;
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

    private int mCurrentState = QRSCANNER;

    private FragmentManager fragmentManager;
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate !");
        setContentLayout(R.layout.qrcore_layout);

        setTitle("QRScanner");

        fragmentManager = getFragmentManager();
        // 第一次启动时选中第0个tab
        mQCScannerButton = (MagicButton) this.findViewById(R.id.magic_button_scanner);
        mQCDataButton = (MagicButton) this.findViewById(R.id.magic_button_data);
        mQCExcelButton = (MagicButton) this.findViewById(R.id.magic_button_excel);

        mQCScannerButton.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(QRCoreActivity.this, SimpleCaptureActivity.class);
                QRCoreActivity.this.startActivityForResult(i, REQUEST_QR_CODE);
            }
        });
        mQCDataButton.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMaginButtonsInVisible();
                setTabSelection(QRDATA);
            }
        });
        mQCExcelButton.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMaginButtonsInVisible();
                setTabSelection(QREXCEL);
            }
        });

        setTabSelection(QRSCANNER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume !!");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setTabSelection(int selection) {
        Log.d(TAG, "setTabSelection selection = " + selection);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (selection) {
            case QRSCANNER:
                if (mQRScannerFragment == null) {
                    mQRScannerFragment = new QRScannerFragment();
                    if (mFragment != null) {
                        transaction.hide(mFragment);
                    }
                    mFragment = mQRScannerFragment;
                    transaction.add(R.id.qrcore_content, mQRScannerFragment);
                } else {
                    // 如果mQRDataFragment不为空，则直接将它显示出来
                    transaction.show(mQRScannerFragment);
                }
                mCurrentState = QRSCANNER;
                break;
            case QRDATA:
                Log.d(TAG, "mQRDataFragment = " + mQRDataFragment);
                if (mQRDataFragment == null) {
                    mQRDataFragment = new QRDataFragment();
                    if (mFragment != null) {
                        transaction.hide(mFragment);
                    }
                    mFragment = mQRDataFragment;
                    transaction.add(R.id.qrcore_content, mQRDataFragment);
                } else {
                    // 如果mQRDataFragment不为空，则直接将它显示出来
                    Log.d(TAG, "transaction.show(mQRDataFragment) !");
                    transaction.show(mQRDataFragment);
                }
                mCurrentState = QRDATA;
                break;
            case QREXCEL:
                Log.d(TAG, "mQRExcelFragment = " + mQRExcelFragment);
                if (mQRExcelFragment == null) {
                    mQRExcelFragment = new QRExcelFragment();
                    mQRExcelFragment.setQRDataCallback(mQRExcelFragment);
                    mQRExcelFragment.registerFragmentPermission(mQRExcelFragment);
                    if (mFragment != null) {
                        transaction.hide(mFragment);
                    }
                    mFragment = mQRExcelFragment;
                    transaction.add(R.id.qrcore_content, mQRExcelFragment);
                } else {
                    // 如果mQRExcelFragment不为空，则直接将它显示出来
                    Log.d(TAG, "transaction.show(mQRExcelFragment) !");
                    transaction.show(mQRExcelFragment);
                }
                mCurrentState = QREXCEL;
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mQRScannerFragment != null) {
            transaction.hide(mQRScannerFragment);
        }
        if (mQRDataFragment != null) {
            transaction.hide(mQRDataFragment);
        }
        if (mQRExcelFragment != null) {
            transaction.hide(mQRExcelFragment);
        }
    }

    private void setMaginButtonsInVisible() {
        if (mQCScannerButton != null) {
            mQCScannerButton.setVisibility(View.INVISIBLE);
        }
        if (mQCDataButton != null) {
            mQCDataButton.setVisibility(View.INVISIBLE);
        }
        if (mQCExcelButton != null) {
            mQCExcelButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (mCurrentState == QRSCANNER) {
            finish();
        } else {
            setTabSelection(QRSCANNER);
            setMaginButtonsVisible();
        }
    }

    private void setMaginButtonsVisible() {
        if (mQCScannerButton != null) {
            mQCScannerButton.setVisibility(View.VISIBLE);
        }
        if (mQCDataButton != null) {
            mQCDataButton.setVisibility(View.VISIBLE);
        }
        if (mQCExcelButton != null) {
            mQCExcelButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mQRScannerFragment != null) {
            mQRScannerFragment = null;
        }
        if (mQRDataFragment != null) {
            mQRDataFragment = null;
        }
        if (mQRExcelFragment != null) {
            mQRExcelFragment.removeQRDataCallback();
            mQRExcelFragment.removeFragmentPermission();
            mQRExcelFragment = null;
        }
    }
}
