package com.jensen.demo.a3dayslate;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class  barcodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    /*
       Implements the Barcode scanner activity which will be used in the app to
       scan ISBN codes from the User's camera.

       Contains the basic methods to open the camera, attach it to a view, and scan/return an ISBN code

       @author: Jensen Khemchandani
       @see: Rewrite for .java classes that use it
       @version:1.0.0

   */
    ZXingScannerView scannerView;
    // Result codes
    int gotBook = 1;
    int badResult = 2;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
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