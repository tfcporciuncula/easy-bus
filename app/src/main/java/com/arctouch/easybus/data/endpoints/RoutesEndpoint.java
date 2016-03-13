package com.arctouch.easybus.data.endpoints;

import android.content.Context;

import com.arctouch.easybus.R;
import com.arctouch.easybus.route.Route;
import com.arctouch.easybus.search.RoutesResult;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Endpoint responsible for calling "findRoutesByStopName".
 */
public class RoutesEndpoint extends AbstractEndpoint<Route, String> {

    public RoutesEndpoint(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        super(restTemplate, httpHeaders);
    }

    @Override
    public List<Route> call(Context context, String parameter) {
        HttpEntity<String> request = new HttpEntity<>(buildJsonBody(parameter), httpHeaders);

        String endpointUrl = context.getString(R.string.find_routes_by_stop_name_endpoint);
        ResponseEntity<RoutesResult> response = restTemplate.exchange(endpointUrl, HttpMethod.POST, request, RoutesResult.class);
        return response.getBody().getResults();
    }

    private String buildJsonBody(String parameter) {
        parameter = "%" + parameter.replace(".", "").replace(" ", "%") + "%";
        return buildJsonBody("stopName", parameter);
    }

}
