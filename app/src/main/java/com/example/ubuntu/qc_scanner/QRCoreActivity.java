package com.example.ubuntu.qc_scanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ubuntu.qc_scanner.fragment.QRDataFragment;
import com.example.ubuntu.qc_scanner.fragment.QRExcelFragment;
import com.example.ubuntu.qc_scanner.fragment.QRScannerFragment;

import br.com.bloder.magic.view.MagicButton;

/**
 * Created by EdwardAdmin on 2017/7/16.
 */

public class QRCoreActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "QRCoreActivity";

    private static final int REQUEST_QR_CODE = 1;
    private static final int QRSCANNER = 0x1;
    private static final int QRDATA    = 0x2;
    private static final int QREXCEL   = 0x3;

    private QRScannerFragment mQRScannerFragment;
    private QRDataFragment    mQRDataFragment;
    private QRExcelFragment   mQRExcelFragment;

    private MagicButton mQCScannerButton;
    private MagicButton mQCDataButton;
    private MagicButton mQCExcelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.qrcore_layout);
        mQCScannerButton = (MagicButton) this.findViewById(R.id.magic_button_scanner);
        mQCDataButton = (MagicButton) this.findViewById(R.id.magic_button_data);
        mQCExcelButton = (MagicButton) this.findViewById(R.id.magic_button_excel);

        mQCScannerButton.setMagicButtonClickListener(this);
        mQCDataButton.setMagicButtonClickListener(this);
        mQCExcelButton.setMagicButtonClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.magic_button_scanner:
                Intent i = new Intent(QRCoreActivity.this, SimpleCaptureActivity.class);
                QRCoreActivity.this.startActivityForResult(i, REQUEST_QR_CODE);
                setTabSelection(QRSCANNER);
                break;
            case R.id.magic_button_data:
                setTabSelection(QRDATA);
                break;
            case R.id.magic_button_excel:
                setTabSelection(QREXCEL);
                break;
        }
    }

    private void setTabSelection(int selection) {

    }
}
