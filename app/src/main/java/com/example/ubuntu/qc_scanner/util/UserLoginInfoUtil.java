package com.example.ubuntu.qc_scanner.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by EdwardAdmin on 2017/9/16.
 */

public class UserLoginInfoUtil {

    private static UserLoginInfoUtil sUserLoginInfoUtil;
    private static final String USERINFO = "userinfo";
    private static final String USERNAME = "username";
    private static final String TAKETIME = "taketime";
    private SharedPreferences mSharedPreferences;


    private UserLoginInfoUtil() {

    }

    public static UserLoginInfoUtil getInstance() {
        if (sUserLoginInfoUtil == null) {
            synchronized (UserLoginInfoUtil.class) {
                if (sUserLoginInfoUtil == null) {
                    sUserLoginInfoUtil = new UserLoginInfoUtil();
                }
            }
        }
        return sUserLoginInfoUtil;
    }

    public void saveUserInfo(String username, String dateTime, Context context) {
        mSharedPreferences = context.getSharedPreferences(USERINFO, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USERNAME, username);
        editor.putString(TAKETIME, dateTime);
        //提交修改
        editor.commit();
    }

    public UserInfo getUserInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE);
        String name = preferences.getString(USERNAME, "EricWinner");
        String time = preferences.getString(TAKETIME, "2017-09-16");
        return new UserInfo(name, time);
    }

    protected class UserInfo {
        protected String username;
        protected String datetime;

        public UserInfo (String username,String datetime) {
            this.username = username;
            this.datetime = datetime;
        }
    }
}
