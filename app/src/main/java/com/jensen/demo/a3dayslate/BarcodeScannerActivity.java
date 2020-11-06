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

/* Barcode Scanner Activity

   Version 1.0.0

   November 5 2020

   Copyright [2020] [Jensen Khemchandani]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

/**
 * Barcode Scanner Activity
 * Implements the Barcode scanner activity which will be used in the app to
 * scan ISBN codes from the User's camera.
 *
 * Contains the basic methods to open the camera, attach it to a view, and scan/return an ISBN code
 *
 * @author: Jensen Khemchandani
 * @version:1.0.0
 */
public class BarcodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView scannerView;
    // Result codes
    int gotBook = 1;
    int badResult = 2;

    /**
     * Starts the camera and starts scanning for ISBN barcodes
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }

    /**
     * Gets and sends a barcode string when one has been recognized
     */
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

    /**
     * Stops the camera when the activity is paused/in the background
     */
    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }
    /**
     * Resumes the activity when the activity is brought back to the foreground
     */
    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}