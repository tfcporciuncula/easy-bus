package com.arctouch.easybus.data.endpoints;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

/**
 * Base abstract implementation of an Endpoint that holds the RestTemplate and the HttpHeader.
 * It also handles the request body building (heheh).
 *
 * @param <T> The type returned by the endpoint.
 * @param <P> The type of the parameter needed by the endpoint.
 */
public abstract class AbstractEndpoint<T, P> implements Endpoint<T, P> {

    private static final String TAG = AbstractEndpoint.class.getSimpleName();

    protected RestTemplate restTemplate;
    protected HttpHeaders httpHeaders;

    public AbstractEndpoint(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
    }

    protected String buildJsonBody(String paramName, P paramValue) {
        JSONObject innerParam = new JSONObject();
        JSONObject params = new JSONObject();

        try {
            innerParam.put(paramName, paramValue);
            params.put("params", innerParam);
        } catch (JSONException e) {
            Log.e(TAG, "Error building service parameters.", e);
        }

        return params.toString();
    }

}
