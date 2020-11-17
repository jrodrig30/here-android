package br.unisc.pdm.cliente;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class QrCodeReaderActivity extends ActionBarActivity implements ZBarScannerView.ResultHandler {

    static public final String EXTRA_READER_RESULT = "reader_result";

    private ZBarScannerView mScannerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QRCODE);

        mScannerView = new ZBarScannerView(this);
        mScannerView.setFormats(formats);
        mScannerView.setKeepScreenOn(true);

        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Intent activityResult = new Intent();
        activityResult.putExtra(EXTRA_READER_RESULT, result.getContents());
        setResult(RESULT_OK, activityResult);
        finish();
    }
}