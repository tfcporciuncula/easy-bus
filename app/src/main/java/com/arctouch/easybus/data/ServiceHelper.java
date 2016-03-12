package com.arctouch.easybus.data;

import android.content.Context;
import android.util.Log;

import com.arctouch.easybus.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Helper responsible for building the compatible RestTemplate, headers, and parameters to be used when
 * calling the AppGlu REST API.
 */
public class ServiceHelper {

    private static final String TAG = ServiceHelper.class.getSimpleName();

    private static final String APP_GLU_CUSTOM_HEADER_NAME = "X-AppGlu-Environment";
    private static final String APP_GLU_CUSTOM_HEADER_VALUE = "staging";

    public static RestTemplate buildRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> converters = Arrays.<HttpMessageConverter<?>>asList(
                new StringHttpMessageConverter(Charset.forName("UTF-8")),
                new GsonHttpMessageConverter()
        );
        restTemplate.setMessageConverters(converters);

        return restTemplate;
    }

    public static HttpHeaders buildHttpHeaders(Context context) {
        HttpHeaders httpHeaders = new HttpHeaders();

        String username = context.getString(R.string.app_glu_username);
        String password = context.getString(R.string.app_glu_password);
        httpHeaders.setAuthorization(new HttpBasicAuthentication(username, password));

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(APP_GLU_CUSTOM_HEADER_NAME, APP_GLU_CUSTOM_HEADER_VALUE);

        return httpHeaders;
    }

    public static String buildJsonStopNameParameter(String stopName) {
        return buildJsonParameter("stopName", "%" + stopName + "%");
    }

    public static String buildJsonRouteIdParameter(String routeId) {
        return buildJsonParameter("routeId", routeId);
    }

    // {"params": {"parameter": "value"}}
    public static String buildJsonParameter(String parameter, String value) {
        JSONObject innerParam = new JSONObject();
        JSONObject params = new JSONObject();

        try {
            innerParam.put(parameter, value);
            params.put("params", innerParam);
        } catch (JSONException e) {
            Log.e(TAG, "Error building service parameters.", e);
        }

        return params.toString();
    }

}
