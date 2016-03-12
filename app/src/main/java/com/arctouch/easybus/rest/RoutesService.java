package com.arctouch.easybus.rest;

import com.arctouch.easybus.model.Route;

import java.util.ArrayList;
import java.util.List;

public class RoutesService {

    public static List<Route> findRoutesByStopName(String query) {
        query = "%" + query + "%";
        List<Route> routes = new ArrayList<>();
        routes.add(new Route(1, "AGROGOMICA"));
        routes.add(new Route(2, "CANASVIEIRAS"));
        routes.add(new Route(3, "GAMA DEÇA"));
        return routes;
    }

}
