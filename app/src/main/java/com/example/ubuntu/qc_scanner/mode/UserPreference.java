package com.example.ubuntu.qc_scanner.mode;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.ubuntu.qc_scanner.application.ItLanbaoLibApplication;


public class UserPreference {
    private static SharedPreferences mUserPreferences;
    private static final String USER_PREFERENCE = "user_preference";
    
    public static SharedPreferences ensureIntializePreference() {
        if (mUserPreferences == null) {
            mUserPreferences = ItLanbaoLibApplication.getInstance().getSharedPreferences(USER_PREFERENCE, 0);
        }
        return mUserPreferences;
    }

    public static void save(String key, String value) {
        Editor editor = ensureIntializePreference().edit();
        editor.putString(key, value);
        editor.commit(); 
    }

    public static String read(String key, String defaultvalue) {
        return ensureIntializePreference().getString(key, defaultvalue);
    }
    
}
