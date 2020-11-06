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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/* ReviewLocationActivity

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
 * An activity that opens up a Google Maps interface for reviewing a location set by a book's owner
 * Uses the Google Maps API and functionality with the MapView and GoogleMap objects
 * @author Jensen Khemchandani
 * @version 1.0.0
 * @see OutgoingRequestsActivity
 */

public class ReviewLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private MapView locationView;
    ExchangeLocation exchangeLocation;

    /**
     * Sets up the GoogleMap interface when the activity is opened
     * @param savedInstanceState
     * The bundle object passed to the activity
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_location);

        // Getting permissions for location

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        // Get the data ready for the map
        Intent locationData = getIntent();
        exchangeLocation = (ExchangeLocation) locationData.getSerializableExtra("LOCATION");
        locationView = findViewById(R.id.reviewLocationView);
        if(locationView != null) {
            locationView.onCreate(null);
            locationView.onResume();
            locationView.getMapAsync(this);
        }

    }

    /**
     * Callback method for getMapAsync
     * When the GoogleMap object is ready to be initialized, this method is called, and sets up the location picking functionality
     * @param googleMap
     * The map object that the user is interacting with
     */

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