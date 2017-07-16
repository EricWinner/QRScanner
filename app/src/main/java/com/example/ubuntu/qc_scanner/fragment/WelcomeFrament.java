package com.example.ubuntu.qc_scanner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by EdwardAdmin on 2017/7/15.
 */

public class WelcomeFrament extends Fragment{

    private static String LAYOUT_ID = "layout_id";

    public static WelcomeFrament newInstance (int layoutId) {
        WelcomeFrament mProductFragment = new WelcomeFrament();
        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_ID, layoutId);
        mProductFragment.setArguments(bundle);
        return mProductFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(getArguments().getInt(LAYOUT_ID, -1), container, false);
        return rootView;
    }
}
