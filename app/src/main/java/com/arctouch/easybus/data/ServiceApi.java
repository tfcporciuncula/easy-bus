package com.arctouch.easybus.data;

import android.content.Context;

import com.arctouch.easybus.data.endpoints.Endpoint;
import com.arctouch.easybus.data.endpoints.RoutesEndpoint;
import com.arctouch.easybus.data.endpoints.ScheduleEndpoint;
import com.arctouch.easybus.data.endpoints.StopsEndpoint;
import com.arctouch.easybus.route.Route;
import com.arctouch.easybus.route.schedule.ScheduleItem;
import com.arctouch.easybus.route.stops.Stop;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Singleton that handles the calls to the AppGlu REST API.
 */
public class ServiceApi {

    private static ServiceApi instance;

    private Context context;

    private Endpoint<Route, String> routesEndpoint;
    private Endpoint<Stop, Long> stopsEndpoint;
    private Endpoint<ScheduleItem, Long> scheduleEndpoint;

    public static ServiceApi instance(Context context) {
        if (instance == null) {
            instance = new ServiceApi(context);
        }
        instance.context = context;
        return instance;
    }

    private ServiceApi(Context context) {
        RestTemplate restTemplate = ServiceHelper.buildRestTemplate();
        HttpHeaders httpHeaders = ServiceHelper.buildHttpHeaders(context);

        routesEndpoint = new RoutesEndpoint(restTemplate, httpHeaders);
        stopsEndpoint = new StopsEndpoint(restTemplate, httpHeaders);
        scheduleEndpoint = new ScheduleEndpoint(restTemplate, httpHeaders);
    }

    public List<Route> findRoutesByStopName(String query) {
        return routesEndpoint.call(context, query);
    }

    public List<Stop> findStopsByRouteId(long routeId) {
        return stopsEndpoint.call(context, routeId);
    }

    public List<ScheduleItem> findDeparturesByRouteId(long routeId) {
        return scheduleEndpoint.call(context, routeId);
    }

}
