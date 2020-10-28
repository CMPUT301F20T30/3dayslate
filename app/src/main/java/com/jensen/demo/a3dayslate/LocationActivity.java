package com.jensen.demo.a3dayslate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private MapView locationView;
    ExchangeLocation exchangeLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Button selectLocation = (Button)findViewById(R.id.selectLocationButton);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        locationView = findViewById(R.id.locationView);
        if(locationView != null) {
            locationView.onCreate(null);
            locationView.onResume();
            locationView.getMapAsync(this);
        }

        selectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(exchangeLocation != null) {
                    Intent returnExchangeLocationIntent = new Intent();
                    returnExchangeLocationIntent.putExtra("LOCATION", exchangeLocation);
                    setResult(LocationActivity.RESULT_OK, returnExchangeLocationIntent);
                    finish();
                }
                else {
                    Toast.makeText(LocationActivity.this, "Please select a location.", Toast.LENGTH_SHORT);
                }
            }
        });



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getApplicationContext());
        map = googleMap;
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                map.clear();
                map.addMarker(new MarkerOptions().position(latLng).title("Exchange location"));
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;
                exchangeLocation = new ExchangeLocation(latitude, longitude);
                // Bring this back to the requests activity -> on clicking select select location
            }
        });
    }
}