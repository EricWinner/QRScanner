package com.example.ubuntu.qc_scanner.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.ubuntu.qc_scanner.R;
import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by ubuntu on 17-8-1.
 */

public class ForgetSecondFragment extends Fragment {

    private static final String TAG = "ForgetSecondFragment";

    private ForgetSecondButtonClickListener mForgetSecondButtonClickListener;

    private static final int MSG_RESEND_CODE = 0x01;
    private static final int MSG_VERIFICATION_CODE = 0x02;
    private static final int MSG_IDLE_CODE = 0x03;

    private String mPhoneNumber;
    private String mVerificationCodeString;

    private TextView mSecondNumber;
    private EditText mForgetVerificationCode;
    private Button mForgetSendVerificationCode;
    private Button mForgetSecondNextButton;
    private int i = 30;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View forgetSecondLayout = inflater.inflate(R.layout.activity_forget_2, container, false);
        initViews(forgetSecondLayout);
        registerSDK();
        return forgetSecondLayout;
    }

    private void initViews(View forgetSecondLayout) {
        mSecondNumber = (TextView)forgetSecondLayout.findViewById(R.id.forget_second_number);
        mForgetVerificationCode = (EditText)forgetSecondLayout.findViewById(R.id.forget_verification_code);
        mForgetSendVerificationCode = (Button)forgetSecondLayout.findViewById(R.id.forget_send_verification_code);
        mForgetSecondNextButton =  (Button)forgetSecondLayout.findViewById(R.id.forget_next_2);

        String info = getString(R.string.register_phone_prefix) + mPhoneNumber + getString(R.string.register_phone_suffix);
        mSecondNumber.setText(info);
        mForgetVerificationCode.addTextChangedListener(new EditChangedListener());
        mForgetSendVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog(mPhoneNumber);
            }
        });

        mForgetSecondNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVerificationCodeString = mForgetVerificationCode.getText().toString();
                if (mForgetVerificationCode == null || mForgetVerificationCode.length() <= 0) {
                    Toast.makeText(getActivity(), "验证码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.submitVerificationCode("86", mPhoneNumber, mVerificationCodeString);
            }
        });
    }

    private class EditChangedListener implements TextWatcher {
        private CharSequence temp;//监听前的文本
        private final int charMaxNum = 4;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            temp = charSequence;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (temp.length() >= charMaxNum) {
                mForgetSecondNextButton.setBackgroundColor(Color.YELLOW);
            } else {
                mForgetSecondNextButton.setBackgroundColor(getResources().getColor(R.color.next_background_color));
            }
        }
    }

    private void showCustomDialog(final String phoneNumber) {
        FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(getActivity())
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
                        Toast.makeText(getActivity(), "Sending", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
        alert.show();
    }

    private void sendSms(String phoneNumber) {
        SMSSDK.getVerificationCode("86", phoneNumber);
        mForgetSendVerificationCode.setClickable(false);
        mForgetSendVerificationCode.setText("再次输入倒计时" + "(" + i + ")");
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

    Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_RESEND_CODE:
                    mForgetSendVerificationCode.setText("重新发送(" + i + ")");
                    break;
                case MSG_VERIFICATION_CODE:
                    mForgetSendVerificationCode.setText("获取验证中");
                    mForgetSendVerificationCode.setClickable(true);
                    i = 30;
                    break;
                case MSG_IDLE_CODE:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    Log.d(TAG, "result:" + result + ",event:" + event);
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            Toast.makeText(getActivity(), "验证成功",
                                    Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "验证成功!");
                            if (mForgetSecondButtonClickListener != null) {
                                mForgetSecondButtonClickListener.onForgetSecondButtonClick();
                            }
                        } else if (event == cn.smssdk.SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            Toast.makeText(getActivity(), "验证已发送",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "验证错误",
                                    Toast.LENGTH_SHORT).show();
                            ((Throwable) data).printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "验证失败",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void registerSDK() {
        if ("moba6b6c6d6".equalsIgnoreCase(MobSDK.getAppkey())) {
            Log.d(TAG, "smssdk_dont_use_demo_appkey !");
        }
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Log.d("jiangsu", "afterEvent event = " + event + ",result = " + result + ",data = " + data);
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                msg.what = MSG_IDLE_CODE;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }


    public void setSecondButtonClickListener(ForgetSecondButtonClickListener listener) {
        mForgetSecondButtonClickListener = listener;
    }

    public void removeSecondButtonClickListener() {
        mForgetSecondButtonClickListener = null;
    }

    public interface ForgetSecondButtonClickListener {
        void onForgetSecondButtonClick();
    }
}
