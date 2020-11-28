package com.mariash.countrycapitals;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

// Google Map Class
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String EXTRA_LATITUDE = "latitude";
    private static final String EXTRA_LONGITUDE = "longitude";
    private static final String EXTRA_CAPITAL = "capital";

    private GoogleMap mMap;
    private double mLatitude;
    private double mLongitude;
    private String mCapital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLatitude = (double) getIntent().getDoubleExtra(EXTRA_LATITUDE, 0);
        mLongitude = (double) getIntent().getDoubleExtra(EXTRA_LONGITUDE, 0);
        mCapital = (String) getIntent().getStringExtra(EXTRA_CAPITAL);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng city = new LatLng(mLatitude, mLongitude);
        mMap.addMarker(new MarkerOptions().position(city).title(mCapital));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(city, 8));
    }

    public static Intent newIntent(Context packageContext, double latitude, double longitude, String capital) {
        Intent intent = new Intent(packageContext, MapsActivity.class);
        intent.putExtra(EXTRA_LATITUDE, latitude);
        intent.putExtra(EXTRA_LONGITUDE, longitude);
        intent.putExtra(EXTRA_CAPITAL, capital);
        return intent;
    }
}