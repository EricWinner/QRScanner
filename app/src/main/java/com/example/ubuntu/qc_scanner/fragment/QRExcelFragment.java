package com.example.ubuntu.qc_scanner.fragment;


import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ubuntu.qc_scanner.R;
import com.example.ubuntu.qc_scanner.database.BaseColumns;
import com.example.ubuntu.qc_scanner.database.QRContentProviderMetaData;

/**
 * Created by EdwardAdmin on 2017/7/16.
 */

public class QRExcelFragment extends Fragment {

    private static final String TAG = "QRExcelFragment";

    private Button mExcelQueryButton;
    private Button mExcelInsertButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View qrExcelLayout = inflater.inflate(R.layout.qrexcel_layout, container, false);
        initViews(qrExcelLayout);
        return qrExcelLayout;
    }

    private void initViews(View qrExcelLayout) {

        mExcelQueryButton = (Button) qrExcelLayout.findViewById(R.id.excel_button1);
        mExcelInsertButton = (Button) qrExcelLayout.findViewById(R.id.excel_button2);

        mExcelInsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
    }

    private void add() {
        ContentValues values = new ContentValues();
        values.put(BaseColumns.QRDATA_DATE, "20170812");
        values.put(BaseColumns.QRDATA_FOREIGN_GROUP_ID, 1);
        values.put(BaseColumns.QRDATA_NUMBER_ID, "3330001000100099845745");
        values.put(BaseColumns.QRDATA_PEAK_VALUE, 200.00);
        values.put(BaseColumns.QRDATA_VALLEY_VALUE, 700.00);
        values.put(BaseColumns.QRDATA_TOTAL_AMOUNT, 1000.00);

        getActivity().getContentResolver().insert(QRContentProviderMetaData.QRTableMetaData.CONTENT_URI, values);

        Toast.makeText(getActivity(), "add success !", Toast.LENGTH_LONG).show();
    }

}
