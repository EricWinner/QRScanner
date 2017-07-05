package com.example.ubuntu.qc_scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ubuntu.qc_scanner.application.ItLanBaoApplication;
import com.example.ubuntu.qc_scanner.http.HttpResponeCallBack;
import com.example.ubuntu.qc_scanner.http.RequestApiData;
import com.example.ubuntu.qc_scanner.http.UrlConstance;
import com.example.ubuntu.qc_scanner.mode.AnalyticalRegistInfo;
import com.example.ubuntu.qc_scanner.mode.Constant;
import com.example.ubuntu.qc_scanner.mode.KeyConstance;
import com.example.ubuntu.qc_scanner.mode.UserBaseInfo;
import com.example.ubuntu.qc_scanner.mode.UserPreference;
import com.example.ubuntu.qc_scanner.util.Utils;


/**
 * Created by ubuntu on 17-7-4.
 */

public class RegisterActivity extends Activity implements HttpResponeCallBack{

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText mLoginNick;
    private EditText mEmail;
    private EditText mPassword;
    private Button mRegistButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        initViews();
    }

    private void initViews() {
        mLoginNick = (EditText) this.findViewById(R.id.regist_nick);
        mEmail = (EditText) this.findViewById(R.id.regist_account);
        mPassword = (EditText) this.findViewById(R.id.regist_password);
        mRegistButton = (Button) this.findViewById(R.id.regist_btn);

        mRegistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName     = mLoginNick.getText().toString();
                String emailAddress = mEmail.getText().toString();
                String password     = mPassword.getText().toString();

                if (!TextUtils.isEmpty(nickName) && !TextUtils.isEmpty(emailAddress)
                        && !TextUtils.isEmpty(password)) {
                    if (Utils.isEmail(emailAddress)) {
                        RequestApiData.getInstance().getRegistData(nickName, emailAddress,
                                password, AnalyticalRegistInfo.class, RegisterActivity.this);
                    } else {
                        Toast.makeText(RegisterActivity.this, "输入的邮箱有误",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "输入信息不完整",Toast.LENGTH_LONG).show();
                }
            }
        });
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
        if (UrlConstance.KEY_REGIST_INFO.equals(apiName)) {
            if (object != null && object instanceof AnalyticalRegistInfo) {
                AnalyticalRegistInfo info = (AnalyticalRegistInfo) object;
                String successCode = info.getRet();
                if (successCode.equals(Constant.KEY_SUCCESS)) {
                    UserBaseInfo baseUser = new UserBaseInfo();
                    baseUser.setEmail(info.getEmail());
                    baseUser.setNickname(info.getNickname());
                    baseUser.setUserhead(info.getUserhead());
                    baseUser.setUserid(String.valueOf(info.getUserid()));
                    ItLanBaoApplication.getInstance().setBaseUser(baseUser);
                    UserPreference.save(KeyConstance.IS_USER_ID, String.valueOf(info.getUserid()));
                    UserPreference.save(KeyConstance.IS_USER_ACCOUNT, info.getEmail());
                    UserPreference.save(KeyConstance.IS_USER_PASSWORD, mPassword.getText().toString());

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
        Toast.makeText(RegisterActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Save failure, apiName = " + apiName + ",errorNo = " + errorNo + ",strMsg = " + strMsg);
    }
}
