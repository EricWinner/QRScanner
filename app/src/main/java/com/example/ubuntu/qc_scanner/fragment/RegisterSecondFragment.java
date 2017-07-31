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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ubuntu.qc_scanner.R;
import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by EdwardAdmin on 2017/7/30.
 */

public class RegisterSecondFragment extends Fragment {

    private static final String TAG = "RegisterSecondFragment";

    private static final int MSG_RESEND_CODE = 0x01;
    private static final int MSG_VERIFICATION_CODE = 0x02;
    private static final int MSG_IDLE_CODE = 0x03;

    private SecondButtonClickListener mSecondButtonClickListener;

    private String mPhoneNumber;
    private String mVerificationCodeString;
    private TextView mSecondNumber;
    private EditText mVerificationCode;
    private Button mSendVerificationCode;
    private Button mSecondNextButton;
    private int i;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View registerSecondLayout = inflater.inflate(R.layout.activity_register_2, container, false);
        initViews(registerSecondLayout);
        registerSDK();
        return registerSecondLayout;
    }

    private void initViews(View registerSecondLayout) {
        mSecondNumber = (TextView) registerSecondLayout.findViewById(R.id.register_second_number);
        mVerificationCode = (EditText) registerSecondLayout.findViewById(R.id.register_verification_code);
        mSendVerificationCode = (Button) registerSecondLayout.findViewById(R.id.send_verification_code);
        mSecondNextButton = (Button) registerSecondLayout.findViewById(R.id.register_next_2);

        String info = getString(R.string.register_phone_prefix) + mPhoneNumber + getString(R.string.register_phone_suffix);
        mSecondNumber.setText(info);
        mVerificationCode.addTextChangedListener(new EditChangedListener());
        mSendVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog(mPhoneNumber);
            }
        });

        mSecondNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVerificationCodeString = mVerificationCode.getText().toString();
                if (mVerificationCode == null || mVerificationCode.length() <= 0) {
                    Toast.makeText(getActivity(), "验证码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.submitVerificationCode("86", mPhoneNumber, mVerificationCodeString);
            }
        });
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
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
                mSecondNextButton.setBackgroundColor(Color.YELLOW);
            } else {
                mSecondNextButton.setBackgroundColor(getResources().getColor(R.color.next_background_color));
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
        mSendVerificationCode.setClickable(false);
        mSendVerificationCode.setText("再次输入倒计时" + "(" + i + ")");
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
                    mSendVerificationCode.setText("重新发送(" + i + ")");
                    break;
                case MSG_VERIFICATION_CODE:
                    mSendVerificationCode.setText("获取验证中");
                    mSendVerificationCode.setClickable(true);
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
                            if (mSecondButtonClickListener != null) {
                                mSecondButtonClickListener.onSecondButtonClick();
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

    public void setSecondButtonClickListener(SecondButtonClickListener listener) {
        mSecondButtonClickListener = listener;
    }

    public void removeSecondButtonClickListener() {
        mSecondButtonClickListener = null;
    }

    public interface SecondButtonClickListener {
        void onSecondButtonClick();
    }
}
