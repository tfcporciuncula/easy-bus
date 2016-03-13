package com.arctouch.easybus.data.endpoints;

import android.content.Context;
import android.util.Log;

import com.arctouch.easybus.R;
import com.arctouch.easybus.model.ScheduleItem;
import com.arctouch.easybus.route.schedule.ScheduleResult;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Endpoint responsible for calling "findDeparturesByRouteId".
 */
public class ScheduleEndpoint extends AbstractEndpoint<ScheduleItem, Long> {

    public ScheduleEndpoint(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        super(restTemplate, httpHeaders);
    }

    @Override
    public List<ScheduleItem> call(Context context, Long parameter) {
        HttpEntity<String> request = new HttpEntity<>(buildJsonBody(parameter), httpHeaders);

        String endpointUrl = context.getString(R.string.find_departures_by_route_id_endpoint);
        ResponseEntity<ScheduleResult> response = restTemplate.exchange(endpointUrl, HttpMethod.POST, request, ScheduleResult.class);

        Log.d("TAG", "result: " + response.getBody().getResults());
        return response.getBody().getResults();
    }

    private String buildJsonBody(Long parameter) {
        return buildJsonBody("routeId", parameter);
    }

}
