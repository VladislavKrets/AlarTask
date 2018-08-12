package com.alarstudios.alartask;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ItemDataActivity extends AppCompatActivity {
    private String id, name, country;
    private Double lat, lon;
    private TextView idTextView, nameTextView, countryTextView;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_data);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initializeVariables();
        initializeWidgets(savedInstanceState);


    }

    private void initializeVariables() {
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        country = getIntent().getStringExtra("country");
        lat = getIntent().getDoubleExtra("lat", 0);
        lon = getIntent().getDoubleExtra("lon", 0);
    }

    private void initializeWidgets(Bundle savedInstanceState) {
        idTextView = findViewById(R.id.idTextView);
        idTextView.setText(id);
        nameTextView = findViewById(R.id.nameTextView);
        nameTextView.setText(name);
        countryTextView = findViewById(R.id.countryTextView);
        countryTextView.setText(country);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng latLng = new LatLng(lat, lon);
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                final CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(10)
                        .bearing(0)
                        .tilt(30)
                        .build();
                googleMap.addMarker(markerOptions);
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mapView.onResume();
            }
        });

    }
}
