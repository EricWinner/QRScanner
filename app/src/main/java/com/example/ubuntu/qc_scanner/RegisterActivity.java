package com.example.ubuntu.qc_scanner;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.ubuntu.qc_scanner.application.ItLanBaoApplication;
import com.example.ubuntu.qc_scanner.fragment.RegisterFirstFragment;
import com.example.ubuntu.qc_scanner.fragment.RegisterSecondFragment;
import com.example.ubuntu.qc_scanner.fragment.RegisterThirdFragment;
import com.example.ubuntu.qc_scanner.http.HttpResponeCallBack;
import com.example.ubuntu.qc_scanner.http.RequestApiData;
import com.example.ubuntu.qc_scanner.http.UrlConstance;
import com.example.ubuntu.qc_scanner.mode.AnalyticalRegistInfo;
import com.example.ubuntu.qc_scanner.mode.Constant;
import com.example.ubuntu.qc_scanner.mode.KeyConstance;
import com.example.ubuntu.qc_scanner.mode.UserBaseInfo;
import com.example.ubuntu.qc_scanner.mode.UserPreference;

import java.util.ArrayList;

import cn.smssdk.SMSSDK;


/**
 * Created by ubuntu on 17-7-4.
 */

public class RegisterActivity extends BaseActivity implements HttpResponeCallBack,
        RegisterFirstFragment.FirstButtonClickListener,
        RegisterSecondFragment.SecondButtonClickListener,
        RegisterThirdFragment.ThirdButtonClickListener {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private static final int REGISTER_FIRST = 0x1;
    private static final int REGISTER_SECOND = 0x2;
    private static final int REGISTER_THIRD = 0x3;

    private RegisterFirstFragment mRegisterFirstFragment;
    private RegisterSecondFragment mRegisterSecondFragment;
    private RegisterThirdFragment mRegisterThirdFragment;

    private FragmentManager mFragmentManager;

    private RequestQueue mRequestQueue;
    private String mPhoneNumber;
    private String mAccountPassword;

    // 短信注册，随机产生头像
    private static final String[] AVATARS = {
            "http://tupian.qqjay.com/u/2011/0729/e755c434c91fed9f6f73152731788cb3.jpg",
            "http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
            "http://img1.touxiang.cn/uploads/allimg/111029/2330264224-36.png",
            "http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
            "http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
            "http://img1.touxiang.cn/uploads/20121224/24-054837_708.jpg",
            "http://img1.touxiang.cn/uploads/20121212/12-060125_658.jpg",
            "http://img1.touxiang.cn/uploads/20130608/08-054059_703.jpg",
            "http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
            "http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
            "http://img1.touxiang.cn/uploads/20130515/15-080722_514.jpg",
            "http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg"
    };

    private void requestRegisterInfo(String phoneNumber, String userPassword) {
        Log.d("jiangsu", "requestRegisterInfo getRegisterData !!");
        RequestApiData.getInstance().getRegisterData(phoneNumber, userPassword,
                AnalyticalRegistInfo.class, RegisterActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_register_phone);
        mFragmentManager = getFragmentManager();
        checkPermission();
        mRequestQueue = Volley.newRequestQueue(RegisterActivity.this);
        setTabSelection(REGISTER_FIRST);
    }

    public void setTabSelection(int selection) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (selection) {
            case REGISTER_FIRST:
                if (mRegisterFirstFragment == null) {
                    mRegisterFirstFragment = new RegisterFirstFragment();
                    mRegisterFirstFragment.setFirstButtonClickListener(this);
                    transaction.replace(R.id.register_content, mRegisterFirstFragment);
                } else {
                    // 如果mRegisterSecondFragment不为空，则直接将它显示出来
                    transaction.show(mRegisterSecondFragment);
                }
                break;
            case REGISTER_SECOND:
                if (mRegisterSecondFragment == null) {
                    mRegisterSecondFragment = new RegisterSecondFragment();
                    mRegisterSecondFragment.setPhoneNumber(mPhoneNumber);
                    mRegisterSecondFragment.setSecondButtonClickListener(this);
                    transaction.replace(R.id.register_content, mRegisterSecondFragment);
                } else {
                    // 如果mRegisterSecondFragment不为空，则直接将它显示出来
                    transaction.show(mRegisterSecondFragment);
                }
                break;
            case REGISTER_THIRD:
                if (mRegisterThirdFragment == null) {
                    mRegisterThirdFragment = new RegisterThirdFragment();
                    mRegisterThirdFragment.setThirdButtonClickListener(this);
                    transaction.replace(R.id.register_content, mRegisterThirdFragment);
                } else {
                    // 如果mRegisterThirdFragment不为空，则直接将它显示出来
                    transaction.show(mRegisterThirdFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mRegisterFirstFragment != null) {
            transaction.hide(mRegisterFirstFragment);
        }
        if (mRegisterSecondFragment != null) {
            transaction.hide(mRegisterSecondFragment);
        }
        if (mRegisterThirdFragment != null) {
            transaction.hide(mRegisterThirdFragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeListener();
        SMSSDK.unregisterAllEventHandler();
    }

    private void removeListener() {
        if (mRegisterFirstFragment != null) {
            mRegisterFirstFragment.removeFirstButtonClickListener();
        }
        if (mRegisterSecondFragment != null) {
            mRegisterSecondFragment.removeSecondButtonClickListener();
        }
        if (mRegisterThirdFragment != null) {
            mRegisterThirdFragment.removeThirdButtonClickListener();
        }
    }

    @Override
    public void onResponeStart(String apiName) {
        // TODO Auto-generated method stub
        Toast.makeText(RegisterActivity.this, "正在请求数据...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoading(String apiName, long count, long current) {
        Toast.makeText(RegisterActivity.this, "开始保存...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(String apiName, Object object) {
        //注册接口
        Log.d("jiangsu", "onSuccess apiName = " + apiName + ",object = " + object);
        if (UrlConstance.KEY_REGIST_INFO.equals(apiName)) {
            if (object != null && object instanceof AnalyticalRegistInfo) {
                mRegisterThirdFragment.onSuccessLogin();

                AnalyticalRegistInfo info = (AnalyticalRegistInfo) object;
                String successCode = info.getRet();
                Log.d("jiangsu", "onSuccess successCode = " + successCode);
                if (successCode.equals(Constant.KEY_SUCCESS)) {
                    UserBaseInfo baseUser = new UserBaseInfo();
                    baseUser.setEmail(info.getEmail());
                    baseUser.setUserid(String.valueOf(info.getUserid()));
                    ItLanBaoApplication.getInstance().setBaseUser(baseUser);
                    UserPreference.save(KeyConstance.IS_USER_ACCOUNT, info.getEmail());
                    UserPreference.save(KeyConstance.IS_USER_PASSWORD, mAccountPassword);

                    Toast.makeText(RegisterActivity.this, "注册成功...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, QCScannerActivity.class);
                    RegisterActivity.this.startActivity(intent);
                    RegisterActivity.this.finish();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(String apiName, Throwable t, int errorNo, String strMsg) {
        Toast.makeText(RegisterActivity.this, strMsg, Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Save failure, apiName = " + apiName + ",errorNo = " + errorNo + ",strMsg = " + strMsg);
    }

    @Override
    public void onFirstButtonClick(String phoneNumber) {
        mPhoneNumber = phoneNumber;
        setTabSelection(REGISTER_SECOND);
    }

    @Override
    public void onSecondButtonClick() {
        setTabSelection(REGISTER_THIRD);
    }

    @Override
    public void onThirdButtonClick(String password) {
        mAccountPassword = password;
        requestRegisterInfo(mPhoneNumber, mAccountPassword);
    }
}
