package com.arctouch.easybus.map;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.arctouch.easybus.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Activity that holds and manages the map.
 */
// TODO: start map at current location?
// TODO: keep map state?
@SuppressWarnings("ResourceType")
public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final String KEY_CURRENT_STREET = "currentStreet";
    private static final String EXTRA_STREET_NAME = "com.arctouch.easybus.street_name";

    private GoogleMap map;
//    private GoogleApiClient googleApiClient;

    private String currentStreet;

    public static String getSelectedStreetName(Intent data) {
        return data.getStringExtra(EXTRA_STREET_NAME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (savedInstanceState != null) {
            currentStreet = savedInstanceState.getString(KEY_CURRENT_STREET);
        }

//        googleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addConnectionCallbacks(this)
//                .addApi(LocationServices.API)
//                .build();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.setRetainInstance(true);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CURRENT_STREET, currentStreet);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (hasLocationAccessPermission()) {
            map.setMyLocationEnabled(true);
        }

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addNewStreetMarker(latLng);
            }
        });
    }

    private boolean hasLocationAccessPermission() {
        return ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void addNewStreetMarker(final LatLng latLng) {
        ResultReceiver receiver = new ResultReceiver(new Handler()){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == Activity.RESULT_OK) {
                    currentStreet = FetchStreetNameIntentService.getStreetRetrieved(resultData);
                    addMarker(latLng, currentStreet);
                } else {
                    addMarker(latLng, getString(R.string.address_not_found));
                }
            }
        };
        FetchStreetNameIntentService.startFetchStreetNameService(this, receiver, latLng);
        addMarker(latLng, getString(R.string.fetching_address));
    }

    private void addMarker(LatLng latLng, String title) {
        map.clear();

        Marker marker = map.addMarker(new MarkerOptions().position(latLng).title(title));
        marker.showInfoWindow();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        setActivityResult();
        super.onBackPressed();
    }

    private void setActivityResult() {
        if (currentStreet == null) {
            setResult(Activity.RESULT_CANCELED);
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_STREET_NAME, currentStreet);
        setResult(Activity.RESULT_OK, data);
    }

}
