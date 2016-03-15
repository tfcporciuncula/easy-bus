package com.arctouch.easybus.data;

import com.arctouch.easybus.route.Route;
import com.arctouch.easybus.search.RoutesResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * It tests the mapping for the classes Route and RoutesResults with their JSON equivalents.
 */
public class RouteJsonMappingTest {

    // this is the response when querying 'findRoutesByStopName' with '%delminda%' as parameter
    private static final String SOME_ROUTES_RESPONSE =
            "{\"rows\":[" +

                    "{\"id\":22,\"shortName\":\"131\",\"longName\":\"AGRONÔMICA VIA GAMA D'EÇA\"," +
                    "\"lastModifiedDate\":\"2009-10-26T02:00:00+0000\",\"agencyId\":9}," +

                    "{\"id\":32,\"shortName\":\"133\"," +
                    "\"longName\":\"AGRONÔMICA VIA MAURO RAMOS\",\"lastModifiedDate\":\"2012-07-23T03:00:00+0000\"," +
                    "\"agencyId\":9}]," +

            "\"rowsAffected\":0}";

    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    private Route route1;
    private Route route2;
    private RoutesResult someRoutesResult;

    @Before
    public void setup() {
        route1 = new Route();
        route1.setId(22);
        route1.setShortName("131");
        route1.setLongName("AGRONÔMICA VIA GAMA D'EÇA");
        route1.setLastModifiedDate("2009-10-26T02:00:00+0000");
        route1.setAgencyId(9);

        route2 = new Route();
        route2.setId(32);
        route2.setShortName("133");
        route2.setLongName("AGRONÔMICA VIA MAURO RAMOS");
        route2.setLastModifiedDate("2012-07-23T03:00:00+0000");
        route2.setAgencyId(9);

        someRoutesResult = new RoutesResult();
        someRoutesResult.setRows(Arrays.asList(route1, route2));
        someRoutesResult.setRowsAffected(0);
    }

    @Test
    public void testSerialization() {
        String serialized = gson.toJson(someRoutesResult);
        assertThat(serialized, equalTo(SOME_ROUTES_RESPONSE));
    }

    @Test
    public void testDeserialization() {
        RoutesResult deserialized = gson.fromJson(SOME_ROUTES_RESPONSE, RoutesResult.class);
        assertThat(deserialized, equalTo(someRoutesResult));
    }

}
