package com.example.ubuntu.qc_scanner.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ubuntu.qc_scanner.R;

/**
 * Created by EdwardAdmin on 2017/7/16.
 */

public class QRDataFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.qrdatalist_layout, container, false);
        return messageLayout;
    }
}
