package com.example.ubuntu.qc_scanner.fragment;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ubuntu.qc_scanner.R;
import com.example.ubuntu.qc_scanner.adapter.CardStackAdapter;
import com.example.ubuntu.qc_scanner.database.BaseColumns;
import com.example.ubuntu.qc_scanner.database.QRContentProviderMetaData;
import com.example.ubuntu.qc_scanner.mode.IQRDataItem;
import com.example.ubuntu.qc_scanner.mode.QRDataItem;
import com.example.ubuntu.qc_scanner.task.QRQueryDataTask;
import com.example.ubuntu.qc_scanner.task.QRSuperDataTask;
import com.loopeer.cardstack.CardStackView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by EdwardAdmin on 2017/7/16.
 */

public class QRDataFragment extends Fragment implements CardStackView.ItemExpendListener {

    private static final String TAG = "QRDataFragment";
    private static final int MSG_DATA_TIMEOUT = 300;
    private ArrayList<IQRDataItem> mAllQRDataLists = null;
    private CardStackView mStackView;
    private CardStackAdapter mCardStackAdapter;
    private Context mContext;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView !");
        mAllQRDataLists = new ArrayList<>();
        mContext = getActivity();
        View qrDataLayout = inflater.inflate(R.layout.qrdatalist_layout, container, false);

        mStackView = (CardStackView) qrDataLayout.findViewById(R.id.stackview_main);
        mStackView.setItemExpendListener(this);

        initData();
        return qrDataLayout;
    }

    @Override
    public void onItemExpend(boolean expend) {
    }

    private void initData() {
        Log.d(TAG, "initData start ");
        mCardStackAdapter = new CardStackAdapter(getActivity());
        mStackView.setAdapter(mCardStackAdapter);
        QRSuperDataTask task = new QRQueryDataTask(mContext);
        if (task.isExistData()) {
            task.clearQRDataList();
            mAllQRDataLists.clear();
            task.addArrayList(mAllQRDataLists, new QRDataItem());
            task.queryAllData(task.getCursor());
            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "initData has data updateData > mAllQRDataLists.size = " + mAllQRDataLists.size());
                            mCardStackAdapter.updateData(mAllQRDataLists);
                        }
                    }
                    , MSG_DATA_TIMEOUT
            );
        } else {
            Log.d(TAG, "initData no data");
            task.clearQRDataList();
            mAllQRDataLists.clear();
            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "initData no data updateData > mAllQRDataLists.size = " + mAllQRDataLists.size());
                            mCardStackAdapter.updateData(mAllQRDataLists);
                        }
                    }
                    , MSG_DATA_TIMEOUT
            );
            Toast.makeText(mContext, "没有显示的数据", Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "initData end ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(TAG, "onHiddenChanged hidden= " + hidden);
        if (!hidden) {
            initData();
        }
    }
}
