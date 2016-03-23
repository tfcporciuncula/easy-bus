package com.arctouch.easybus.map;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * IntentService responsible for fetching the street name of a location tapped on the map.
 *
 * If no street (thoroughfare) is found, the service will return the first line of the address fetched.
 */
public class FetchStreetNameIntentService extends IntentService {

    private static final String TAG = FetchStreetNameIntentService.class.getSimpleName();

    private static final String EXTRA_RECEIVER  = "com.arctouch.easybus.receiver";
    private static final String EXTRA_LAT_LNG   = "com.arctouch.easybus.lat_lng";

    private static final String KEY_STREET_NAME = "com.arctouch.easybus.street_name";

    public FetchStreetNameIntentService() {
        super(TAG);
    }

    public static void startFetchStreetNameService(Context context, ResultReceiver receiver, LatLng location) {
        Intent intent = new Intent(context, FetchStreetNameIntentService.class);
        intent.putExtra(EXTRA_RECEIVER, receiver);
        intent.putExtra(EXTRA_LAT_LNG, location);
        context.startService(intent);
    }

    public static String getStreetRetrieved(Bundle resultData) {
        return resultData.getString(KEY_STREET_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LatLng location = intent.getParcelableExtra(EXTRA_LAT_LNG);
        ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RECEIVER);

        try {
            fetchAddress(location, receiver);
        } catch (IOException e) {
            Log.e(TAG, "Error fetching address from location.", e);
            receiver.send(Activity.RESULT_CANCELED, new Bundle());
        }
    }

    private void fetchAddress(LatLng location, ResultReceiver receiver) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
        if (addresses.size() > 0) {
            Address address = addresses.get(0);
            String streetName = extractStreetName(address);

            Bundle bundle = new Bundle();
            bundle.putString(KEY_STREET_NAME, streetName);
            receiver.send(Activity.RESULT_OK, bundle);
        } else {
            receiver.send(Activity.RESULT_CANCELED, new Bundle());
        }
    }

    private String extractStreetName(Address address) {
        String thoroughfare = address.getThoroughfare();
        if (thoroughfare != null) {
            return thoroughfare;
        }
        return address.getAddressLine(0);
    }

}
