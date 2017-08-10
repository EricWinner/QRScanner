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

public class QRScannerFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View qrScannerLayout = inflater.inflate(R.layout.qrscanner_layout, container, false);
        return qrScannerLayout;
    }
}
