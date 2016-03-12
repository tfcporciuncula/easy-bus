package com.arctouch.easybus.data;

import android.content.Context;
import android.util.Log;

import com.arctouch.easybus.R;
import com.arctouch.easybus.model.Route;
import com.arctouch.easybus.search.RoutesResult;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Singleton that handles the calls to the AppGlu REST API.
 */
public class ServiceApi {

    private static final String TAG = ServiceApi.class.getSimpleName();

    private static ServiceApi instance;

    private Context context;

    private RestTemplate restTemplate;
    private HttpHeaders httpHeaders;

    public static ServiceApi instance(Context context) {
        if (instance == null) {
            instance = new ServiceApi(context);
        }
        instance.context = context;
        return instance;
    }

    private ServiceApi(Context context) {
        restTemplate = ServiceHelper.buildRestTemplate();
        httpHeaders = ServiceHelper.buildHttpHeaders(context);
    }

    public List<Route> findRoutesByStopName(String query) {
        Log.d(TAG, "findRoutesByStopName(" + query + ") was called.");
        String body = ServiceHelper.buildJsonStopNameParameter(query);
        HttpEntity<String> request = new HttpEntity<>(body, httpHeaders);

        String endpoint = context.getString(R.string.find_routes_by_stop_name_endpoint);
        ResponseEntity<RoutesResult> response = restTemplate.exchange(endpoint, HttpMethod.POST, request, RoutesResult.class);

        List<Route> routes = response.getBody().getResults();
        Log.d(TAG, "-- routes found: " + routes.toString());
        return routes;
    }

}
