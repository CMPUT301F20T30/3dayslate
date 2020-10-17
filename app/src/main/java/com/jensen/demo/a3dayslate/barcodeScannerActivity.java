package com.jensen.demo.a3dayslate;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class  barcodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    // Result codes
    int gotBook = 1;
    int badResult = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }

    @Override
    public void handleResult(Result result) {
        //getBookByISBN.ISBNResult.setText(result.getText());
        Bundle bundle = new Bundle();
        bundle.putString("ISBN", result.getText().toString());
        Intent intent = new Intent();
        intent.putExtra("bundle", bundle);
        setResult(gotBook, intent);
        finish();
        //onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}