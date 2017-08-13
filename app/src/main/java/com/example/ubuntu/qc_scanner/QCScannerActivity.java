package com.example.ubuntu.qc_scanner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.ubuntu.qc_scanner.adapter.CardStackAdapter;
import com.loopeer.cardstack.AllMoveDownAnimatorAdapter;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.UpDownAnimatorAdapter;
import com.loopeer.cardstack.UpDownStackAnimatorAdapter;

import java.util.Arrays;


/**
 * Created by ubuntu on 17-7-4.
 */

public class QCScannerActivity extends BaseActivity implements CardStackView.ItemExpendListener {
    private static final int REQUEST_QR_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.qrdatalist_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onItemExpend(boolean expend) {
    }


    private void startQRLoader() {
        final Button qrCodeBtn = (Button) findViewById(R.id.qrcode_btn);
        qrCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QCScannerActivity.this, SimpleCaptureActivity.class);
                QCScannerActivity.this.startActivityForResult(i, REQUEST_QR_CODE);
            }
        });
    }
}