package com.example.ubuntu.qc_scanner.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ubuntu.qc_scanner.R;
import com.example.ubuntu.qc_scanner.RegisterActivity;
import com.example.ubuntu.qc_scanner.util.Utils;

/**
 * Created by EdwardAdmin on 2017/7/30.
 */

public class RegisterFirstFragment extends Fragment {

    private Button mRegisterFirstNextButton;
    private EditText mRegisterNumber;
    private FirstButtonClickListener mFirstButtonClickListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View registerFirstLayout = inflater.inflate(R.layout.activity_register_1, container, false);
        initViews(registerFirstLayout);
        return registerFirstLayout;
    }

    private void initViews(View rootView) {
        mRegisterNumber = (EditText) rootView.findViewById(R.id.register_number);
        mRegisterNumber.addTextChangedListener(new EditChangedListener());
        mRegisterFirstNextButton = (Button) rootView.findViewById(R.id.register_next_1);
        mRegisterFirstNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = mRegisterNumber.getText().toString();
                if (TextUtils.isEmpty(phoneNumber) || !Utils.isChinaPhoneLegal(phoneNumber)) {
                    Toast.makeText(getActivity(), "手机号码输入有误！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (mFirstButtonClickListener != null) {
                        mFirstButtonClickListener.onFirstButtonClick(phoneNumber);
                    }
                }
            }
        });
    }

    public void setFirstButtonClickListener(FirstButtonClickListener listener) {
        mFirstButtonClickListener = listener;
    }

    public void removeFirstButtonClickListener() {
        mFirstButtonClickListener = null;
    }

    public interface FirstButtonClickListener {
        void onFirstButtonClick(String phoneNumber);
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
                mRegisterFirstNextButton.setBackgroundColor(Color.YELLOW);
            } else {
                mRegisterFirstNextButton.setBackgroundColor(getResources().getColor(R.color.next_background_color));
            }
        }
    }
}
