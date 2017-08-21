package com.example.ubuntu.qc_scanner;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.ubuntu.qc_scanner.application.ItLanBaoApplication;
import com.example.ubuntu.qc_scanner.fragment.ForgetFirstFragment;
import com.example.ubuntu.qc_scanner.fragment.ForgetSecondFragment;
import com.example.ubuntu.qc_scanner.fragment.ForgetThirdFragment;
import com.example.ubuntu.qc_scanner.http.HttpResponeCallBack;
import com.example.ubuntu.qc_scanner.http.RequestApiData;
import com.example.ubuntu.qc_scanner.http.UrlConstance;
import com.example.ubuntu.qc_scanner.mode.AnalyticalRegistInfo;
import com.example.ubuntu.qc_scanner.mode.Constant;
import com.example.ubuntu.qc_scanner.mode.KeyConstance;
import com.example.ubuntu.qc_scanner.mode.UserBaseInfo;
import com.example.ubuntu.qc_scanner.mode.UserPreference;

import java.util.ArrayList;

/**
 * Created by ubuntu on 17-7-11.
 */

public class ForgetPasswordActivity extends BaseActivity implements HttpResponeCallBack,
        ForgetFirstFragment.ForgetFirstButtonClickListener,
        ForgetSecondFragment.ForgetSecondButtonClickListener,
        ForgetThirdFragment.ForgetThirdButtonClickListener {

    private static final String TAG = ForgetPasswordActivity.class.getSimpleName();

    private static final int FORGET_FIRST = 0x1;
    private static final int FORGET_SECOND = 0x2;
    private static final int FORGET_THIRD = 0x3;

    private ForgetFirstFragment mForgetFirstFragment;
    private ForgetSecondFragment mForgetSecondFrament;
    private ForgetThirdFragment mForgetThirdFragment;

    private FragmentManager mFragmentManager;
    private RequestQueue mRequestQueue;
    private String mPhoneNumber;
    private String mAccountPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_forget_number);
        checkPermission();
        mRequestQueue = Volley.newRequestQueue(ForgetPasswordActivity.this);
        mFragmentManager = getFragmentManager();
        setTabSelection(FORGET_FIRST);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeListeners();
    }

    private void requestRegisterInfo(String phoneNumber, String userPassword) {
        Log.d(TAG, "requestRegisterInfo getRegisterData !!");
        RequestApiData.getInstance().getRegisterData(phoneNumber, userPassword,
                AnalyticalRegistInfo.class, ForgetPasswordActivity.this);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int readPhone = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            int receiveSms = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
            int readSms = checkSelfPermission(Manifest.permission.READ_SMS);
            int readContacts = checkSelfPermission(Manifest.permission.READ_CONTACTS);
            int readSdcard = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            int requestCode = 0;
            ArrayList<String> permissions = new ArrayList<String>();
            if (readPhone != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 0;
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (receiveSms != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 1;
                permissions.add(Manifest.permission.RECEIVE_SMS);
            }
            if (readSms != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 2;
                permissions.add(Manifest.permission.READ_SMS);
            }
            if (readContacts != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 3;
                permissions.add(Manifest.permission.READ_CONTACTS);
            }
            if (readSdcard != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 4;
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (requestCode > 0) {
                String[] permission = new String[permissions.size()];
                this.requestPermissions(permissions.toArray(permission), requestCode);
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void removeListeners() {
        if (mForgetFirstFragment != null) {
            mForgetFirstFragment.removeFirstButtonClickListener();
        }
        if (mForgetSecondFrament != null) {
            mForgetSecondFrament.removeSecondButtonClickListener();
        }
        if (mForgetThirdFragment != null) {
            mForgetThirdFragment.removeThirdButtonClickListener();
        }
    }

    public void setTabSelection(int selection) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (selection) {
            case FORGET_FIRST:
                if (mForgetFirstFragment == null) {
                    mForgetFirstFragment = new ForgetFirstFragment();
                    mForgetFirstFragment.setFirstButtonClickListener(this);
                    transaction.replace(R.id.register_content, mForgetFirstFragment);
                } else {
                    // 如果mRegisterSecondFragment不为空，则直接将它显示出来
                    transaction.show(mForgetFirstFragment);
                }
                break;
            case FORGET_SECOND:
                if (mForgetSecondFrament == null) {
                    mForgetSecondFrament = new ForgetSecondFragment();
                    mForgetSecondFrament.setPhoneNumber(mPhoneNumber);
                    mForgetSecondFrament.setSecondButtonClickListener(this);
                    transaction.replace(R.id.register_content, mForgetSecondFrament);
                } else {
                    // 如果mRegisterSecondFragment不为空，则直接将它显示出来
                    transaction.show(mForgetSecondFrament);
                }
                break;
            case FORGET_THIRD:
                if (mForgetThirdFragment == null) {
                    mForgetThirdFragment = new ForgetThirdFragment();
                    mForgetThirdFragment.setThirdButtonClickListener(this);
                    transaction.replace(R.id.register_content, mForgetThirdFragment);
                } else {
                    // 如果mRegisterThirdFragment不为空，则直接将它显示出来
                    transaction.show(mForgetThirdFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mForgetFirstFragment != null) {
            transaction.hide(mForgetFirstFragment);
        }
        if (mForgetSecondFrament != null) {
            transaction.hide(mForgetSecondFrament);
        }
        if (mForgetThirdFragment != null) {
            transaction.hide(mForgetThirdFragment);
        }
    }


    @Override
    public void onResponeStart(String apiName) {
        // TODO Auto-generated method stub
        Toast.makeText(ForgetPasswordActivity.this, "正在请求数据...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoading(String apiName, long count, long current) {
        Toast.makeText(ForgetPasswordActivity.this, "开始保存...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(String apiName, Object object) {
        Log.d(TAG, "onSuccess apiName = " + apiName + ",object = " + object);
        if (UrlConstance.KEY_REGIST_INFO.equals(apiName)) {
            if (object != null && object instanceof AnalyticalRegistInfo) {
                mForgetThirdFragment.onSuccessLogin();

                AnalyticalRegistInfo info = (AnalyticalRegistInfo) object;
                String successCode = info.getRet();
                Log.d(TAG, "onSuccess successCode = " + successCode);
                if (successCode.equals(Constant.KEY_SUCCESS)) {
                    UserBaseInfo baseUser = new UserBaseInfo();
                    baseUser.setEmail(info.getEmail());
                    baseUser.setUserid(String.valueOf(info.getUserid()));
                    ItLanBaoApplication.getInstance().setBaseUser(baseUser);
                    UserPreference.save(KeyConstance.IS_USER_ACCOUNT, info.getEmail());
                    UserPreference.save(KeyConstance.IS_USER_PASSWORD, mAccountPassword);

                    Toast.makeText(ForgetPasswordActivity.this, "注册成功...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgetPasswordActivity.this, QRCoreActivity.class);
                    ForgetPasswordActivity.this.startActivity(intent);
                    ForgetPasswordActivity.this.finish();
                }
            } else {
                Toast.makeText(ForgetPasswordActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(String apiName, Throwable t, int errorNo, String strMsg) {
        Toast.makeText(ForgetPasswordActivity.this, strMsg, Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Save failure, apiName = " + apiName + ",errorNo = " + errorNo + ",strMsg = " + strMsg);
    }


    @Override
    public void onForgetFirstButtonClick(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    @Override
    public void onForgetSecondButtonClick() {

    }

    @Override
    public void onForgetThirdButtonClick(String password) {
        mAccountPassword = password;
        requestRegisterInfo(mPhoneNumber,mAccountPassword);
    }
}
