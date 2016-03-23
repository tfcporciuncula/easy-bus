package com.arctouch.easybus.data;

import android.content.Context;

import com.arctouch.easybus.R;
import com.arctouch.easybus.data.endpoints.Endpoint;
import com.arctouch.easybus.route.Route;
import com.arctouch.easybus.route.schedule.ScheduleItem;
import com.arctouch.easybus.route.stops.Stop;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * It tests the ServiceApi's correctness regarding the endpoints' calls.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ServiceHelper.class)
public class ServiceApiTest {

    @Mock
    private Context context;

    @Mock
    private Endpoint<Route, String> routesEndpoint;

    @Mock
    private Endpoint<Stop, Long> stopsEndpoint;

    @Mock
    private Endpoint<ScheduleItem, Long> scheduleEndpoint;

    private ServiceApi instance;

    @Before
    public void setup() throws Exception {
        PowerMockito.mockStatic(ServiceHelper.class);
        avoidRestTemplateCreationDuringTestExecution();

        when(context.getString(R.string.app_glu_username)).thenReturn("what");
        when(context.getString(R.string.app_glu_password)).thenReturn("ever");
        instance = ServiceApi.instance(context);

        Whitebox.setInternalState(instance, "routesEndpoint", routesEndpoint);
        Whitebox.setInternalState(instance, "stopsEndpoint", stopsEndpoint);
        Whitebox.setInternalState(instance, "scheduleEndpoint", scheduleEndpoint);
    }

    private void avoidRestTemplateCreationDuringTestExecution() {
        when(ServiceHelper.buildRestTemplate()).thenReturn(null);
    }

    @Test
    public void veryfyRoutesEndpointCalling() {
        String query = "some query";
        instance.findRoutesByStopName(query);
        verify(routesEndpoint, times(1)).call(context, query);
    }

    @Test
    public void veryfyStopsEndpointCalling() {
        Long routeId = 11L;
        instance.findStopsByRouteId(routeId);
        verify(stopsEndpoint, times(1)).call(context, routeId);
    }

    @Test
    public void veryfyScheduleEndpointCalling() {
        Long routeId = 19L;
        instance.findDeparturesByRouteId(routeId);
        verify(scheduleEndpoint, times(1)).call(context, routeId);
    }

}
