package com.jensen.demo.a3dayslate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import androidx.appcompat.app.AppCompatActivity;

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
 * Will return any external information needed if a parent activity requires it
 *
 * @author Jensen Khemchandani
 * @version 1.0.0
 */
public class BarcodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView scannerView;

    // Result codes
    int gotBook = 1;
    int badResult = 2;

    // Variables
    Book book;
    String requester;
    String action;

    /**Starts the camera and starts scanning for ISBN barcodes.
     *
     * Also unbundles any objects if the parent activity requires it
     * through the action in the bundle.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        Intent intent = getIntent();
        action = (String) intent.getStringExtra("action");
        Log.w("ACTION", action);
        if(action.equals("borrow")) {
            Log.w("SCAN", "got here1");
            book = (Book) intent.getSerializableExtra("book");
            requester = (String) intent.getStringExtra("requester");
        }
        else if(action.equals("return")) {
            book = (Book) intent.getSerializableExtra("book");
        }
    }

    /**Gets and sends a barcode string when one has been recognized.
     *
     * Sends back different results depending on which action (parent activity)
     * called on the activity.
     *
     * @param result
     */
    @Override
    public void handleResult(Result result) {
        //getBookByISBN.ISBNResult.setText(result.getText());
        Log.w("ACTION", action);
        Bundle bundle = new Bundle();
        if(action.equals("borrow")) {
            Log.w("SCAN", "got here2");
            bundle.putSerializable("book", book);
            bundle.putString("requester", requester);
            bundle.putString("ISBN", result.getText().toString());
            Intent intent = new Intent();
            intent.putExtra("bundle", bundle);
            setResult(gotBook, intent);
            finish();
        }else if(action.equals("return")){
            bundle.putSerializable("book", book);
            bundle.putString("ISBN", result.getText().toString());
            Intent intent = new Intent();
            intent.putExtra("bundle", bundle);
            setResult(gotBook, intent);
            finish();
        }
        else {
            bundle.putString("ISBN", result.getText().toString());
            Log.w("JENSENTESTING", "Hello");
            Intent intent = new Intent();
            intent.putExtra("bundle", bundle);
            setResult(gotBook, intent);
            finish();
        }
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