package com.example.ubuntu.qc_scanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.ubuntu.qc_scanner.application.ItLanBaoApplication;
import com.example.ubuntu.qc_scanner.http.HttpResponeCallBack;
import com.example.ubuntu.qc_scanner.http.RequestApiData;
import com.example.ubuntu.qc_scanner.http.UrlConstance;
import com.example.ubuntu.qc_scanner.mode.Constant;
import com.example.ubuntu.qc_scanner.mode.KeyConstance;
import com.example.ubuntu.qc_scanner.mode.UserBaseInfo;
import com.example.ubuntu.qc_scanner.mode.UserPreference;

/**
 * Created by EdwardAdmin on 2017/7/15.
 */

public class SplashActivity extends AppCompatActivity implements HttpResponeCallBack {

    private static final String TAG = "SplashActivity";

    private static final int TIME_DELAY = 500; //ms
    private static final String LOGIN_COUNT = "loginCount";

    private final Handler mHandler = new Handler();

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirstLogin()) {
                    startActivity(new Intent(SplashActivity.this, WelComeActivity.class));
                } else {
                    checkAccount();
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        },TIME_DELAY);
    }

    private boolean isFirstLogin() {
        mSharedPreferences = getSharedPreferences(LOGIN_COUNT, MODE_WORLD_READABLE);
        int count = mSharedPreferences.getInt(LOGIN_COUNT, 0);
        if (count == 0) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            //存入数据
            editor.putInt(LOGIN_COUNT, ++count);
            //提交修改
            editor.commit();
            return true;
        }  else  {
            return false;
        }
    }

    private void checkAccount() {
        String userAccount = UserPreference.read(KeyConstance.IS_USER_ACCOUNT, null);
        String userPassword = UserPreference.read(KeyConstance.IS_USER_PASSWORD, null);
        String userId = UserPreference.read(KeyConstance.IS_USER_ID, null);

        Log.d(TAG, "checkAccount userAccount = " + userAccount + ",userPassword = " + userPassword + ",userId = " + userId);
        if (TextUtils.isEmpty(userAccount)) {
            Intent intent = new Intent();
            intent.setClass(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } else {
            //用保存的信息直接登录
            RequestApiData.getInstance().getLoginData(userAccount, userPassword,
                    UserBaseInfo.class, SplashActivity.this);
        }
    }

    @Override
    public void onResponeStart(String apiName) {
        Toast.makeText(this, "Start...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoading(String apiName, long count, long current) {
        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(String apiName, Object object) {
        //当前接口是否是获取用户的基本信息的接口
        if (UrlConstance.KEY_USER_BASE_INFO.equals(apiName)) {
            if (object != null && object instanceof UserBaseInfo) {
                UserBaseInfo info = (UserBaseInfo) object;
                ItLanBaoApplication.getInstance().setBaseUser(info);//把数据放入到Application里面，全局
                UserPreference.save(KeyConstance.IS_USER_ID, String.valueOf(info.getUserid()));

                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, QCScannerActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                finish();

            } else {
                Toast.makeText(SplashActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            }
        } else if (UrlConstance.KEY_LOGIN_INFO.equals(apiName)) {//当前接口是登录的接口
            //登录返回数据
            if (object != null && object instanceof UserBaseInfo) {
                UserBaseInfo info = (UserBaseInfo) object;
                if (Constant.KEY_SUCCESS.equals(info.getRet())) {

                    ItLanBaoApplication.getInstance().setBaseUser(info);//将用户信息保存在Application中
                    UserPreference.save(KeyConstance.IS_USER_ID, String.valueOf(info.getUserid()));

                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, QCScannerActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right);
                    finish();

                } else {
                    Toast.makeText(SplashActivity.this, info.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onFailure(String apiName, Throwable t, int errorNo, String strMsg) {
        Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show();
    }
}
