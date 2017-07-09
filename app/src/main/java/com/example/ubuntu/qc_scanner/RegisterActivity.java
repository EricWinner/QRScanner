package com.example.ubuntu.qc_scanner;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
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
import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.mob.MobSDK;

import java.util.ArrayList;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


/**
 * Created by ubuntu on 17-7-4.
 */

public class RegisterActivity extends AppCompatActivity implements HttpResponeCallBack{

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private static final int MSG_RESEND_CODE = 0x01;
    private static final int MSG_VERIFICATION_CODE = 0x02;
    private static final int MSG_IDLE_CODE = 0x03;

    private EditText mRegisterPhoneNumber;
    private EditText mRegisterPassword;
    private EditText mRegisterVerificationCode;
    private Button mRegisterButton;
    private Button mRegisterCommitButton;
    private RequestQueue mRequestQueue;

    private int i = 30;

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

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_RESEND_CODE:
                    mRegisterButton.setText("重新发送(" + i + ")");
                    break;
                case MSG_VERIFICATION_CODE:
                    mRegisterButton.setText("获取验证中");
                    mRegisterButton.setClickable(true);
                    i = 30;
                    break;
                case MSG_IDLE_CODE:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    Log.d(TAG, "result:" + result + ",,event:" + event);
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            Toast.makeText(RegisterActivity.this, "验证成功",
                                    Toast.LENGTH_SHORT).show();
                            Log.e("LOG", "验证成功-----------------------");
                        } else if (event == cn.smssdk.SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            Toast.makeText(RegisterActivity.this, "验证已发送",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "验证错误",
                                    Toast.LENGTH_SHORT).show();
                            ((Throwable) data).printStackTrace();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "验证失败",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void requestRegisterInfo(String phoneNumber,String userPassword) {
        RequestApiData.getInstance().getLoginData(phoneNumber, userPassword,
                UserBaseInfo.class, RegisterActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phone);
        initViews();
        checkPermission();
        registerSDK();
        mRequestQueue = Volley.newRequestQueue(RegisterActivity.this);
    }

    private void registerSDK() {
        // 在尝试读取通信录时以弹窗提示用户（可选功能）
        SMSSDK.setAskPermisionOnReadContact(true);
        if ("moba6b6c6d6".equalsIgnoreCase(MobSDK.getAppkey())) {
            Log.d(TAG, "smssdk_dont_use_demo_appkey !");
        }

        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    private void showCustomDialog(final String phoneNumber) {
        FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(RegisterActivity.this)
                .setImageRecourse(R.drawable.ic_cloud_computing)
                .setTextTitle("SEND SMS")
                .setBody("是否将短信发送到:" + phoneNumber)
                .setNegativeColor(R.color.colorNegative)
                .setNegativeButtonText("Later")
                .setOnNegativeClicked(new FancyAlertDialog.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        Log.d(TAG, "confirmDialog = false !");
                        dialog.dismiss();
                    }
                })
                .setPositiveButtonText("Send")
                .setPositiveColor(R.color.colorPositive)
                .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                        sendSms(phoneNumber);
                        Toast.makeText(RegisterActivity.this, "Sending", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
        alert.show();
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

    private void initViews() {
        mRegisterPhoneNumber = (EditText) this.findViewById(R.id.register_phone_number);
        mRegisterPassword = (EditText) this.findViewById(R.id.register_password);
        mRegisterVerificationCode = (EditText) this.findViewById(R.id.register_verification_code);

        mRegisterButton = (Button) this.findViewById(R.id.btn_getSMS);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = mRegisterPhoneNumber.getText().toString();
                if (TextUtils.isEmpty(phoneNumber) || !Utils.isChinaPhoneLegal(phoneNumber)) {
                    Toast.makeText(RegisterActivity.this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
                    return;
                }
                showCustomDialog(phoneNumber);
            }
        });

        mRegisterCommitButton = (Button) this.findViewById(R.id.register_commit);
        mRegisterCommitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = mRegisterPhoneNumber.getText().toString();
                String password = mRegisterPassword.getText().toString();
                String verification = mRegisterVerificationCode.getText().toString();

                if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(password)
                        && !TextUtils.isEmpty(verification)) {
                    if (Utils.isChinaPhoneLegal(phoneNumber)) {
                        //TODO
                        requestRegisterInfo(phoneNumber, password);
                    } else {
                        Toast.makeText(RegisterActivity.this, "输入的密码不正确",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "输入信息不完整",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendSms(String phoneNumber) {
        SMSSDK.getVerificationCode("86", phoneNumber);
        mRegisterButton.setClickable(false);
        mRegisterButton.setText("再次输入倒计时" + "(" + i + ")");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; i > 0; i--) {
                    handler.sendEmptyMessage(MSG_RESEND_CODE);
                    if (i <= 0) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(MSG_VERIFICATION_CODE);
            }
        }).start();
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
        SMSSDK.unregisterAllEventHandler();
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
                    //UserPreference.save(KeyConstance.IS_USER_PASSWORD, mPassword.getText().toString());

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
