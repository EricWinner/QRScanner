package com.example.ubuntu.qc_scanner.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.ubuntu.qc_scanner.R;
import github.ishaan.buttonprogressbar.ButtonProgressBar;

/**
 * Created by ubuntu on 17-8-1.
 */

public class ForgetThirdFragment extends Fragment{

    private ForgetThirdButtonClickListener mForgetThirdButtonClickListener;
    private EditText mResetPassword;
    private RadioButton mResetVisible;
    private ButtonProgressBar mForgetCommitButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View forgetThirdLayout = inflater.inflate(R.layout.activity_forget_3, container, false);
        initViews(forgetThirdLayout);
        return forgetThirdLayout;
    }

    private void initViews(View forgetThirdLayout) {
        mResetPassword = (EditText)forgetThirdLayout.findViewById(R.id.reset_password);
        mResetVisible = (RadioButton)forgetThirdLayout.findViewById(R.id.reset_password_visible);
        mForgetCommitButton = (ButtonProgressBar)forgetThirdLayout.findViewById(R.id.forget_next_3);

        mResetVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mResetPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mResetPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        final ForgetThirdFragment.GlobalValue globalValue = new ForgetThirdFragment.GlobalValue();
        mResetVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = globalValue.isCheck();
                if (isCheck) {
                    if (v == mResetVisible) mResetVisible.setChecked(false);
                } else {
                    if (v == mResetVisible) mResetVisible.setChecked(true);
                }
                globalValue.setCheck(!isCheck);
            }
        });

        mForgetCommitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mForgetCommitButton.startLoader();
                String password = mResetPassword.getText().toString();
                if (!TextUtils.isEmpty(password)) {
                    mForgetThirdButtonClickListener.onForgetThirdButtonClick(password);
                } else {
                    Toast.makeText(getActivity(), "输入信息不完整", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setThirdButtonClickListener(ForgetThirdButtonClickListener listener) {
        mForgetThirdButtonClickListener = listener;
    }

    public interface ForgetThirdButtonClickListener {
        void onForgetThirdButtonClick(String password);
    }

    public void removeThirdButtonClickListener() {
        mForgetThirdButtonClickListener = null;
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

    public void onSuccessLogin(){
        mForgetCommitButton.stopLoader();
    }
}
