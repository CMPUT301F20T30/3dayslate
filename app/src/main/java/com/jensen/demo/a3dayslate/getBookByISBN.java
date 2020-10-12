package com.jensen.demo.a3dayslate;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.android.gms.common.api.PendingResults;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class getBookByISBN extends AppCompatActivity {

    private TextView bookText;
    public static TextView ISBNResult;
    private Button scan_button;
    String bodyText;

    // Barcode stuff
    private static final int REQUEST_CAMERA_PERMISSION = 1144;
    private BarcodeScanner scanner;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getbookbyisbnlayout);
        bookText = findViewById(R.id.bookInfo);
        // Do all the stuff here for now
        OkHttpClient httpClient = new OkHttpClient();
        String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:9781782808084";
        Request request = new Request.Builder()
                .url(url)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    bodyText = response.body().string();
                    try {
                        // This code will be refactored later and does not currently accomplish anything
                        JSONObject jsonObject = new JSONObject(bodyText);
                        JSONArray itemsArray = jsonObject.getJSONArray("items");
                        JSONObject items = itemsArray.getJSONObject(0);
                        JSONObject volInfo = items.getJSONObject("volumeInfo");
                        String titleText = volInfo.getString("title");
                        JSONArray authors = volInfo.getJSONArray("authors");
                        String author1 = authors.getString(0);
                        Log.d("TAG", "TESTING JENSEN " + titleText);
                        bookText.setText(titleText + " " + author1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        // Camera stuff starts here!
        ISBNResult = (TextView)findViewById(R.id.ISBNresult);
        scan_button = (Button) findViewById(R.id.scanISBNButton);
        scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), barcodeScannerActivity.class));
            }
        });
    }
}

