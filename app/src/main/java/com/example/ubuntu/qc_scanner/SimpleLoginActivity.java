package com.example.ubuntu.qc_scanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ubuntu.qc_scanner.util.UserLoginInfoUtil;
import com.shitij.goyal.slidebutton.SwipeButton;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import ru.katso.livebutton.LiveButton;

/**
 * Created by EdwardAdmin on 2017/9/16.
 */

public class SimpleLoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SimpleLoginActivity";

    private EditText mInputUsernameText;
    private LiveButton mChooseDateButton;
    private SwipeButton mSubmitButton;
    private Context mContext;

    private String mUserName;
    private String mDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.login_layout);

        //setTitle(getString(R.string.action_sign_in_short));
        mContext = this;
        mInputUsernameText = (EditText) this.findViewById(R.id.input_username);
        mChooseDateButton = (LiveButton) this.findViewById(R.id.choose_date_button);
        mSubmitButton = (SwipeButton) this.findViewById(R.id.simple_submit);

        mInputUsernameText.addTextChangedListener(new EditChangedListener());
        mChooseDateButton.setOnClickListener(this);
        mSubmitButton.addOnSwipeCallback(new SwipeButton.Swipe() {
            @Override
            public void onButtonPress() {
                //Toast.makeText(SimpleLoginActivity.this, "Pressed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeCancel() {
            }

            @Override
            public void onSwipeConfirm() {
                //Toast.makeText(SimpleLoginActivity.this, "Confirmd!", Toast.LENGTH_LONG).show();
                submitToQR();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_date_button:
                onYearMonthDayPicker();
                break;
            default:
                break;
        }
    }

    private class EditChangedListener implements TextWatcher {
        private final int charMaxNum = 11;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mUserName = charSequence.toString();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mUserName = charSequence.toString();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    private void submitToQR() {
        Log.d(TAG, "submitToQR mUserName = " + mUserName + ",mDateTime = " + mDateTime);
        if (!TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mDateTime)) {
            UserLoginInfoUtil.getInstance().saveUserInfo(mUserName, mDateTime, mContext);
            Intent intent = new Intent();
            intent.setClass(SimpleLoginActivity.this, QRCoreActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            finish();
        } else {
            showToast("请输入完整的信息");
        }
    }

    public void onYearMonthDayPicker() {
        final DatePicker picker = new DatePicker(this);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(this, 10));
        picker.setRangeEnd(2111, 1, 11);
        picker.setRangeStart(2016, 8, 29);
        picker.setSelectedItem(2050, 10, 14);
        picker.setResetWhileWheel(false);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                String time = year + "-" + month + "-" + day;
                showToast(time);
                mChooseDateButton.setText(time);
                mDateTime = time;
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
