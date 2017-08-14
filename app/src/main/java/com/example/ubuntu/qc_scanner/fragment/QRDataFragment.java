package com.example.ubuntu.qc_scanner.fragment;

import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ubuntu.qc_scanner.R;
import com.example.ubuntu.qc_scanner.adapter.CardStackAdapter;
import com.example.ubuntu.qc_scanner.database.BaseColumns;
import com.example.ubuntu.qc_scanner.database.QRContentProviderMetaData;
import com.loopeer.cardstack.CardStackView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by EdwardAdmin on 2017/7/16.
 */

public class QRDataFragment extends Fragment implements CardStackView.ItemExpendListener {

    private static final String TAG = "QRDataFragment";
    private List<QRDataItem> mAllQRDataLists = null;
    private CardStackView mStackView;
    private CardStackAdapter mCardStackAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAllQRDataLists = new ArrayList<QRDataItem>();
        View qrDataLayout = inflater.inflate(R.layout.qrdatalist_layout, container, false);

        mStackView = (CardStackView) qrDataLayout.findViewById(R.id.stackview_main);
        mStackView.setItemExpendListener(this);
        mCardStackAdapter = new CardStackAdapter(getActivity());
        mStackView.setAdapter(mCardStackAdapter);

        initData();
        return qrDataLayout;
    }

    @Override
    public void onItemExpend(boolean expend) {

    }

    private void initData() {
        queryAllQRData();
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        mCardStackAdapter.updateData(mAllQRDataLists);
                    }
                }
                , 200
        );
    }


    private void queryAllQRData() {
        String columns[] = new String[]{BaseColumns.QRDATA_ID, BaseColumns.QRDATA_FOREIGN_GROUP_ID, BaseColumns.QRDATA_NUMBER_ID,
                BaseColumns.QRDATA_DATE, BaseColumns.QRDATA_PEAK_VALUE,
                BaseColumns.QRDATA_VALLEY_VALUE, BaseColumns.QRDATA_TOTAL_AMOUNT};
        Uri mUri = QRContentProviderMetaData.QRTableMetaData.CONTENT_URI;
        Cursor cursor = getActivity().getContentResolver().query(mUri, columns, null, null, null);

        if (cursor.moveToFirst()) {
            int id = 0;
            int foreign_id = 0;
            String numberId = null;
            String date = null;
            float peakValue = 0.0f;
            float valleyValue = 0.0f;
            float totalValue = 0.0f;

            do {
                id = cursor.getInt(cursor.getColumnIndex(BaseColumns.QRDATA_ID));
                foreign_id = cursor.getInt(cursor.getColumnIndex(BaseColumns.QRDATA_FOREIGN_GROUP_ID));
                numberId = cursor.getString(cursor.getColumnIndex(BaseColumns.QRDATA_NUMBER_ID));
                date = cursor.getString(cursor.getColumnIndex(BaseColumns.QRDATA_DATE));
                peakValue = cursor.getFloat(cursor.getColumnIndex(BaseColumns.QRDATA_PEAK_VALUE));
                valleyValue = cursor.getFloat(cursor.getColumnIndex(BaseColumns.QRDATA_VALLEY_VALUE));
                totalValue = cursor.getFloat(cursor.getColumnIndex(BaseColumns.QRDATA_TOTAL_AMOUNT));
                Log.d(TAG, "id = " + id + ",foreign_id = " + foreign_id + ",date = " + date + ",numberId = " + numberId);
                Log.d(TAG, "peakValue = " + peakValue + ",valleyValue = " + valleyValue + ",totalValue = " + totalValue);

                QRDataItem item = new QRDataItem(foreign_id, numberId, date, peakValue, valleyValue, totalValue);
                mAllQRDataLists.add(item);

            } while (cursor.moveToNext());
        }
    }

    private void resetListData() {
        if (mAllQRDataLists != null) {
            mAllQRDataLists.clear();
            mAllQRDataLists = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        resetListData();
    }

    public class QRDataItem {

        private int mForeignGroupID;
        private String mQRDataNumberID;
        private String mQRDataTime;
        private float mPEAKValue;
        private float mValleyValue;
        private float mTotalValue;

        public QRDataItem(int foreignGroupID, String qrDataNumberID, String qrDateTime, float peakValue, float valleyValue, float totalValue) {
            mForeignGroupID = foreignGroupID;
            mQRDataNumberID = qrDataNumberID;
            mQRDataTime = qrDateTime;
            mPEAKValue = peakValue;
            mValleyValue = valleyValue;
            mTotalValue = totalValue;
        }

        public String getmQRDataNumberID() {
            return mQRDataNumberID;
        }

        public int getmForeignGroupID() {
            return mForeignGroupID;
        }

        public String getmQRDataTime() {
            return mQRDataTime;
        }

        public float getmPEAKValue() {
            return mPEAKValue;
        }

        public float getmValleyValue() {
            return mValleyValue;
        }

        public float getmTotalValue() {
            return mTotalValue;
        }

        @Override
        public String toString() {
            return "mForeignGroupID = " + mForeignGroupID + ",mQRDataNumberID = " + mQRDataNumberID + ",mQRDataTime = " + mQRDataTime + ",mPEAKValue = " + mPEAKValue + ",mValleyValue = " + mValleyValue + "mTotalValue = " + mTotalValue;
        }

    }
}
