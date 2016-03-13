package com.arctouch.easybus.map;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.arctouch.easybus.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Activity that holds and manages the map.
 */
@SuppressWarnings("ResourceType")
public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 10;
    private static final int CURRENT_LOCATION_ZOOM = 16;

    private static final String KEY_CURRENT_STREET = "currentStreet";
    private static final String EXTRA_STREET_NAME = "com.arctouch.easybus.street_name";

    private GoogleMap map;
    private GoogleApiClient googleApiClient;

    private String currentStreet;

    public static String getSelectedStreetName(Intent data) {
        return data.getStringExtra(EXTRA_STREET_NAME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        FloatingActionButton searchFab = (FloatingActionButton) findViewById(R.id.search_fab);
        searchFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                triggerSearch();
            }
        });

        if (savedInstanceState != null) {
            currentStreet = savedInstanceState.getString(KEY_CURRENT_STREET);
        }

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.setRetainInstance(true);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CURRENT_STREET, currentStreet);
    }

    private void triggerSearch() {
        if (currentStreet != null) {
            Intent data = new Intent();
            data.putExtra(EXTRA_STREET_NAME, currentStreet);
            setResult(Activity.RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (currentStreet == null) {
            zoomOnCurrentLocationAndAddStreetMark();
        }
    }

    private void zoomOnCurrentLocationAndAddStreetMark() {
        if (hasLocationAccessPermission()) {
//            Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//            if (lastKnownLocation != null) {
//                zoomOnLocationAndAddStreetMark(lastKnownLocation);
//                return;
//            }
            LocationRequest request = buildCurrentLocationRequest();
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    zoomOnLocationAndAddStreetMark(location);
                }
            });
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        }
    }

    private boolean hasLocationAccessPermission() {
        return ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private LocationRequest buildCurrentLocationRequest() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);
        return request;
    }

    private void zoomOnLocationAndAddStreetMark(Location location) {
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        addNewStreetMarker(currentLocation);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, CURRENT_LOCATION_ZOOM));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED) ||
                    (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                zoomOnCurrentLocationAndAddStreetMark();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, R.string.on_connection_suspended_message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, R.string.on_connection_failed_message, Toast.LENGTH_LONG).show();
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
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

}
