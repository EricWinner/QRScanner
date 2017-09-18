package com.example.ubuntu.qc_scanner;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ubuntu.qc_scanner.fragment.QRDataFragment;
import com.example.ubuntu.qc_scanner.fragment.QRExcelFragment;
import com.example.ubuntu.qc_scanner.fragment.QRScannerFragment;
import com.example.ubuntu.qc_scanner.task.QRClearDataTask;
import com.example.ubuntu.qc_scanner.task.QRSuperDataTask;
import com.example.ubuntu.qc_scanner.util.UserLoginInfoUtil;

import br.com.bloder.magic.view.MagicButton;

/**
 * Created by EdwardAdmin on 2017/7/16.
 */

public class QRCoreActivity extends BaseActivity {

    private static final String TAG = "QRCoreActivity";

    private static final int REQUEST_QR_CODE = 1;
    private static final int QRSCANNER = 0x1;
    private static final int QRDATA = 0x2;
    private static final int QREXCEL = 0x3;
    private static final int REQUEST_CAMERA = 1;

    private QRScannerFragment mQRScannerFragment;
    private QRDataFragment mQRDataFragment;
    private QRExcelFragment mQRExcelFragment;

    private MagicButton mQRScannerButton;
    private MagicButton mQCDataButton;
    private MagicButton mQRExcelButton;
    private MagicButton mQRClearButton;
    private MagicButton mQRCameraButton;
    private TextView    mQRTimeTextView;

    private FragmentManager fragmentManager;
    private Fragment mFragment;
    private Context mContext;

    private String mUserName;
    private String mDateTime;
    private int mCurrentState = QRSCANNER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate !");
        mContext = this;
        setContentLayout(R.layout.qrcore_layout);
        setBackArrow();

        fragmentManager = getFragmentManager();
        // 第一次启动时选中第0个tab
        mQRScannerButton = (MagicButton) this.findViewById(R.id.magic_button_scanner);
        mQCDataButton = (MagicButton) this.findViewById(R.id.magic_button_data);
        mQRExcelButton = (MagicButton) this.findViewById(R.id.magic_button_excel);
        mQRClearButton = (MagicButton) this.findViewById(R.id.magic_button_clear);
        mQRCameraButton = (MagicButton) this.findViewById(R.id.magic_button_camera);
        mQRTimeTextView = (TextView) this.findViewById(R.id.qrcore_time_view);

        initString();
        mQRScannerButton.setMagicButtonClickListener(new View.OnClickListener() {
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
        mQRExcelButton.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMaginButtonsInVisible();
                setTabSelection(QREXCEL);
            }
        });

        mQRClearButton.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QRSuperDataTask task = new QRClearDataTask(mContext);
                if (task.isExistData()) {
                    task.clearAllData();
                } else {
                    Toast.makeText(mContext, "没有可以清除的数据！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mQRCameraButton.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCameraActivity(mContext);
            }
        });
        setTabSelection(QRSCANNER);

        commonTitleTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUpper();
            }
        });
    }

    private void initString() {
        UserLoginInfoUtil.UserInfo userinfo = UserLoginInfoUtil.getInstance().getUserInfo(mContext);
        mUserName = userinfo.getUsername();
        mDateTime = userinfo.getDateTime();
        Log.d(TAG, "mUserName = " + mUserName + ",mDateTime = " + mDateTime);
        setTitle(mUserName);
        mQRTimeTextView.setText(mDateTime);
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
        if (mQRScannerButton != null) {
            mQRScannerButton.setVisibility(View.INVISIBLE);
        }
        if (mQCDataButton != null) {
            mQCDataButton.setVisibility(View.INVISIBLE);
        }
        if (mQRExcelButton != null) {
            mQRExcelButton.setVisibility(View.INVISIBLE);
        }
        if (mQRClearButton != null) {
            mQRClearButton.setVisibility(View.INVISIBLE);
        }
        if (mQRCameraButton != null) {
            mQRCameraButton.setVisibility(View.INVISIBLE);
        }
        if (mQRTimeTextView != null) {
            mQRTimeTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        goToUpper();
    }

    private void goToUpper() {
        if (mCurrentState == QRSCANNER) {
            finish();
        } else {
            setTabSelection(QRSCANNER);
            setMaginButtonsVisible();
        }
    }

    private void setMaginButtonsVisible() {
        if (mQRScannerButton != null) {
            mQRScannerButton.setVisibility(View.VISIBLE);
        }
        if (mQCDataButton != null) {
            mQCDataButton.setVisibility(View.VISIBLE);
        }
        if (mQRExcelButton != null) {
            mQRExcelButton.setVisibility(View.VISIBLE);
        }
        if (mQRClearButton != null) {
            mQRClearButton.setVisibility(View.VISIBLE);
        }
        if (mQRCameraButton != null) {
            mQRCameraButton.setVisibility(View.VISIBLE);
        }
        if (mQRTimeTextView != null) {
            mQRTimeTextView.setVisibility(View.VISIBLE);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public static void startCameraActivity(Context context) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // This will only occur if Camera was disabled while Gallery is open
            // since we cache our availability check. Just abort the attempt.
            Log.e(TAG, "Camera activity previously detected but cannot be found", e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_camera:
                startCameraActivity(mContext);
                return true;
            case android.R.id.home:
                goToUpper();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
