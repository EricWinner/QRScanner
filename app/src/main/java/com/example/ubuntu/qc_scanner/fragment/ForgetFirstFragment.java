package com.example.ubuntu.qc_scanner.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.example.ubuntu.qc_scanner.R;
import com.example.ubuntu.qc_scanner.util.Utils;

/**
 * Created by ubuntu on 17-8-1.
 */

public class ForgetFirstFragment extends Fragment{

    private static final String TAG = "ForgetFirstFragment";

    private ForgetFirstButtonClickListener mForgetFirstButtonClickListener;
    private EditText mForgetPhoneNumber;
    private Button mForgetFirstNextButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View forgetFirstLayout = inflater.inflate(R.layout.activity_forget_1, container, false);
        initViews(forgetFirstLayout);
        return forgetFirstLayout;
    }

    private void initViews(View forgetFirstLayout) {
        mForgetPhoneNumber = (EditText)forgetFirstLayout.findViewById(R.id.forget_number);
        mForgetFirstNextButton = (Button)forgetFirstLayout.findViewById(R.id.forget_next_1);

        mForgetPhoneNumber.addTextChangedListener(new EditChangedListener());
        mForgetFirstNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = mForgetPhoneNumber.getText().toString();
                if (TextUtils.isEmpty(phoneNumber) || !Utils.isChinaPhoneLegal(phoneNumber)) {
                    Toast.makeText(getActivity(), "手机号码输入有误！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (mForgetFirstButtonClickListener != null) {
                        mForgetFirstButtonClickListener.onForgetFirstButtonClick(phoneNumber);
                    }
                }
            }
        });
    }

    private class EditChangedListener implements TextWatcher {
        private CharSequence temp;//监听前的文本
        private final int charMaxNum = 11;

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
                mForgetFirstNextButton.setBackgroundColor(Color.YELLOW);
            } else {
                mForgetFirstNextButton.setBackgroundColor(getResources().getColor(R.color.next_background_color));
            }
        }
    }


    public void setFirstButtonClickListener(ForgetFirstButtonClickListener listener) {
        mForgetFirstButtonClickListener = listener;
    }

    public void removeFirstButtonClickListener() {
        mForgetFirstButtonClickListener = null;
    }

    public interface ForgetFirstButtonClickListener {
        void onForgetFirstButtonClick(String phoneNumber);
    }
}
