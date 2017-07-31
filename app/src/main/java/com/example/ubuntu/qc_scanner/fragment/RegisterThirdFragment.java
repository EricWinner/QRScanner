package com.example.ubuntu.qc_scanner.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import com.example.ubuntu.qc_scanner.R;

import github.ishaan.buttonprogressbar.ButtonProgressBar;

/**
 * Created by EdwardAdmin on 2017/7/30.
 */

public class RegisterThirdFragment extends Fragment {

    private static final String TAG = "RegisterThirdFragment";

    private EditText mRegisterPasswordET;
    private EditText mRegisterConfirmPasswordET;
    private RadioButton mPasswordVisible;
    private ButtonProgressBar mRegisterCommitButton;

    private ThirdButtonClickListener mThirdButtonClickListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View registerThirdLayout = inflater.inflate(R.layout.activity_register_3, container, false);
        initViews(registerThirdLayout);
        return registerThirdLayout;
    }

    private void initViews(View registerThirdLayout) {
        mRegisterPasswordET = (EditText) registerThirdLayout.findViewById(R.id.login_password);
        mRegisterConfirmPasswordET = (EditText) registerThirdLayout.findViewById(R.id.confirm_password);
        mRegisterCommitButton = (ButtonProgressBar) registerThirdLayout.findViewById(R.id.register_next_3);
        mPasswordVisible = (RadioButton) registerThirdLayout.findViewById(R.id.radio_visible);

        mRegisterCommitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRegisterCommitButton.startLoader();
                String password = mRegisterPasswordET.getText().toString();
                String confirm_password = mRegisterConfirmPasswordET.getText().toString();
                if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirm_password)) {
                    if (password.equals(confirm_password)) {
                        mThirdButtonClickListener.onThirdButtonClick(password);
                    } else {
                        Toast.makeText(getActivity(), "两次输入的密码不同", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "输入信息不完整", Toast.LENGTH_LONG).show();
                }
            }
        });

        mPasswordVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRegisterPasswordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mRegisterConfirmPasswordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mRegisterPasswordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mRegisterConfirmPasswordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        final GlobalValue globalValue = new GlobalValue();
        mPasswordVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = globalValue.isCheck();
                if (isCheck) {
                    if (v == mPasswordVisible) mPasswordVisible.setChecked(false);
                } else {
                    if (v == mPasswordVisible) mPasswordVisible.setChecked(true);
                }
                globalValue.setCheck(!isCheck);
            }
        });
    }

    public void onSuccessLogin() {
        mRegisterCommitButton.stopLoader();
    }

    public void setThirdButtonClickListener(ThirdButtonClickListener listener) {
        mThirdButtonClickListener = listener;
    }

    public interface ThirdButtonClickListener {
        void onThirdButtonClick(String password);
    }

    public void removeThirdButtonClickListener() {
        mThirdButtonClickListener = null;
    }

    private class GlobalValue {
        private boolean isCheck = true;
        private boolean isCheck() {
            return isCheck;
        }

        private void setCheck(boolean check) {
            isCheck = check;
        }
    }
}
