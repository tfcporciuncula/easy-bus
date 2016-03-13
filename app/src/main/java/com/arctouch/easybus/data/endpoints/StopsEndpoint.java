package com.arctouch.easybus.data.endpoints;

import android.content.Context;

import com.arctouch.easybus.R;
import com.arctouch.easybus.model.Stop;
import com.arctouch.easybus.route.stops.StopsResult;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Endpoint responsible for calling "findStopsByRouteId".
 */
public class StopsEndpoint extends AbstractEndpoint<Stop, Long> {

    public StopsEndpoint(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        super(restTemplate, httpHeaders);
    }

    @Override
    public List<Stop> call(Context context, Long parameter) {
        HttpEntity<String> request = new HttpEntity<>(buildJsonBody(parameter), httpHeaders);

        String endpointUrl = context.getString(R.string.find_stops_by_route_id_endpoint);
        ResponseEntity<StopsResult> response = restTemplate.exchange(endpointUrl, HttpMethod.POST, request, StopsResult.class);
        return response.getBody().getResults();
    }

    private String buildJsonBody(Long parameter) {
        return buildJsonBody("routeId", parameter);
    }

}
