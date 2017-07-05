package com.example.ubuntu.qc_scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * Created by ubuntu on 17-7-4.
 */

public class QCScannerActivity extends Activity{
    private static final int REQUEST_QR_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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