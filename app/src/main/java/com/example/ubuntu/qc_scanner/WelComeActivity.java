package com.example.ubuntu.qc_scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
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
 * Created by ubuntu on 17-7-4.
 */

public class WelComeActivity extends Activity implements HttpResponeCallBack{

    private ImageView mWelcomeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_activiy);
        mWelcomeView = (ImageView) this.findViewById(R.id.logo);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 0.1f);
        alphaAnimation.setDuration(2000);
        mWelcomeView.startAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String userAccount = UserPreference.read(KeyConstance.IS_USER_ACCOUNT, null);
                String userPassword = UserPreference.read(KeyConstance.IS_USER_PASSWORD, null);
                String userId = UserPreference.read(KeyConstance.IS_USER_ID, null);

                if (TextUtils.isEmpty(userAccount)) {
                    Intent intent = new Intent();
                    intent.setClass(WelComeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                } else {
                    //用保存的信息直接登录
                    RequestApiData.getInstance().getLoginData(userAccount, userPassword,
                            UserBaseInfo.class, WelComeActivity.this);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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
                intent.setClass(WelComeActivity.this, QCScannerActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                finish();

            } else {
                Toast.makeText(WelComeActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            }
        } else if (UrlConstance.KEY_LOGIN_INFO.equals(apiName)) {//当前接口是登录的接口
            //登录返回数据
            if (object != null && object instanceof UserBaseInfo) {
                UserBaseInfo info = (UserBaseInfo) object;
                if (Constant.KEY_SUCCESS.equals(info.getRet())) {

                    ItLanBaoApplication.getInstance().setBaseUser(info);//将用户信息保存在Application中
                    UserPreference.save(KeyConstance.IS_USER_ID, String.valueOf(info.getUserid()));

                    Intent intent = new Intent();
                    intent.setClass(WelComeActivity.this, QCScannerActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right);
                    finish();

                } else {
                    Toast.makeText(WelComeActivity.this, info.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onFailure(String apiName, Throwable t, int errorNo, String strMsg) {
        Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show();
    }
}
