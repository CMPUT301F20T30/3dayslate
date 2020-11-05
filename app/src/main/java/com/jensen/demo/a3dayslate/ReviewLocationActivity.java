package com.jensen.demo.a3dayslate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ReviewLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private MapView locationView;
    ExchangeLocation exchangeLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_location);
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        Intent locationData = getIntent();
        exchangeLocation = (ExchangeLocation) locationData.getSerializableExtra("LOCATION");
        locationView = findViewById(R.id.reviewLocationView);
        if(locationView != null) {
            locationView.onCreate(null);
            locationView.onResume();
            locationView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getApplicationContext());
        LatLng latLng = new LatLng(exchangeLocation.getLatitude(), exchangeLocation.getLongitude());
        map = googleMap;
        map.setMinZoomPreference(14.0f);
        map.getUiSettings().setScrollGesturesEnabled(false);
        map.addMarker(new MarkerOptions().position(latLng).title("Exchange location!"));
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}