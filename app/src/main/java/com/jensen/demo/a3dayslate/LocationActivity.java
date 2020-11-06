package com.jensen.demo.a3dayslate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.google.android.gms.maps.model.MarkerOptions;

/* LocationActivity

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
 * An activity that opens up a Google Maps interface for location picking
 * Allows for an owner of a book to specify a location to exchange a book
 * Uses the Google Maps API and functionality with the MapView and GoogleMap objects
 * @author Jensen Khemchandani
 * @version 1.0.0
 * @see IncomingRequestsActivity
 */

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private MapView locationView;
    ExchangeLocation exchangeLocation;

    /**
     * Sets up the GoogleMap interface when the activity is opened, and initializes the selection button and listeners
     * @param savedInstanceState
     * The bundle object passed to the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Button selectLocation = (Button)findViewById(R.id.selectLocationButton);

        // Gets location privilages from the user

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

        // Allows the owner to confirm their selection of location
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

    /**
     * Callback method for getMapAsync
     * When the GoogleMap object is ready to be initialized, this method is called, and sets up the location picking functionality
     * @param googleMap
     * The map object that the user is interacting with
     */
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