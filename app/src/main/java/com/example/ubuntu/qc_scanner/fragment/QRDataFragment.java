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
        Log.d(TAG, "onCreateView !");
        mAllQRDataLists = new ArrayList<>();
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
        Log.d(TAG, "initData ");
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
        Log.d(TAG, "queryAllQRData ");
        String columns[] = new String[]{BaseColumns.QRDATA_ID, BaseColumns.QRDATA_FOREIGN_GROUP_ID,
                BaseColumns.QRDATA_CASE_NUMBER,BaseColumns.QRDATA_DATE,
                BaseColumns.QRDATA_TOTAL_AMOUNT,BaseColumns.QRDATA_VALLEY_VALUE,
                BaseColumns.QRDATA_CASE_NAME,BaseColumns.QRDATA_CASE_TYPE
        };
        Uri mUri = QRContentProviderMetaData.QRTableMetaData.CONTENT_URI;
        Cursor cursor = getActivity().getContentResolver().query(mUri, columns, null, null, null);

        if (cursor.moveToFirst()) {
            int id = 0;
            int foreign_id = 0;
            String numberId = null;
            String date = null;
            String case_number;
            String case_name;
            String case_type;
            float peakValue = 0.0f;
            float valleyValue = 0.0f;
            float totalValue = 0.0f;

            do {
                id = cursor.getInt(cursor.getColumnIndex(BaseColumns.QRDATA_ID));
                foreign_id = cursor.getInt(cursor.getColumnIndex(BaseColumns.QRDATA_FOREIGN_GROUP_ID));
                numberId = cursor.getString(cursor.getColumnIndex(BaseColumns.QRDATA_NUMBER_ID));
                //case_number = cursor.getString(cursor.getColumnIndex(BaseColumns.QRDATA_CASE_NUMBER));
                date = cursor.getString(cursor.getColumnIndex(BaseColumns.QRDATA_DATE));
                totalValue = cursor.getFloat(cursor.getColumnIndex(BaseColumns.QRDATA_TOTAL_AMOUNT));
                valleyValue = cursor.getFloat(cursor.getColumnIndex(BaseColumns.QRDATA_VALLEY_VALUE));
                case_name = cursor.getString(cursor.getColumnIndex(BaseColumns.QRDATA_CASE_NAME));
                case_type = cursor.getString(cursor.getColumnIndex(BaseColumns.QRDATA_CASE_TYPE));
                Log.d(TAG, "id = " + id + ",foreign_id = " + foreign_id + ",date = " + date + ",numberId = " + numberId);
                Log.d(TAG, "valleyValue = " + valleyValue + ",totalValue = " + totalValue + ",case_name = " + case_name + ",case_type = " + case_type);

                QRDataItem item = new QRDataItem(foreign_id, numberId, date, valleyValue, totalValue, case_name, case_type);
                mAllQRDataLists.add(item);

            } while (cursor.moveToNext());
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void resetListData() {
        if (mAllQRDataLists != null) {
            mAllQRDataLists.clear();
            mAllQRDataLists = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class QRDataItem {

        private int mForeignGroupID;
        private String mQRDataNumberID;
        private String mQRDataTime;
        private String mQRDataCaseName;
        private String mQRDataCaseType;
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

        public QRDataItem(int foreignGroupID, String qrDataNumberID, String qrDateTime, float valleyValue, float totalValue,String qrDataCaseName,String qrDataCaseType) {
            mForeignGroupID = foreignGroupID;
            mQRDataNumberID = qrDataNumberID;
            mQRDataTime = qrDateTime;
            mValleyValue = valleyValue;
            mTotalValue = totalValue;
            mQRDataCaseName = qrDataCaseName;
            mQRDataCaseType = qrDataCaseType;
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

        public String getmQRDataCaseName() {
            return mQRDataCaseName;
        }

        public String getmQRDataCaseType() {
            return mQRDataCaseType;
        }

        @Override
        public String toString() {
            return "mForeignGroupID = " + mForeignGroupID + ",mQRDataNumberID = " + mQRDataNumberID + ",mQRDataTime = " + mQRDataTime + ",mPEAKValue = " + mPEAKValue + ",mValleyValue = " + mValleyValue + "mTotalValue = " + mTotalValue;
        }

    }
}
