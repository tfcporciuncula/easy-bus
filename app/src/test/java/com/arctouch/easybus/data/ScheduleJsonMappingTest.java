package com.arctouch.easybus.data;

import com.arctouch.easybus.route.schedule.ScheduleItem;
import com.arctouch.easybus.route.schedule.ScheduleResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * It tests the mapping for the classes ScheduleItem and ScheduleResult with their JSON equivalents.
 */
public class ScheduleJsonMappingTest {

    // this is part of the response when querying 'findDeparturesByRouteId' with '35' as parameter
    private static final String SOME_DEPARTURES_RESPONSE =
            "{\"rows\":[" +
                    "{\"id\":472,\"calendar\":\"WEEKDAY\",\"time\":\"06:00\"}," +
                    "{\"id\":711,\"calendar\":\"WEEKDAY\",\"time\":\"06:09\"}," +
                    "{\"id\":712,\"calendar\":\"WEEKDAY\",\"time\":\"06:18\"}," +
                    "{\"id\":713,\"calendar\":\"WEEKDAY\",\"time\":\"06:28\"}," +
                    "{\"id\":714,\"calendar\":\"WEEKDAY\",\"time\":\"06:37\"}," +
                    "{\"id\":715,\"calendar\":\"WEEKDAY\",\"time\":\"06:43\"}]," +
            "\"rowsAffected\":0}";

    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    private List<ScheduleItem> departures;
    private ScheduleResult someScheduleResult;

    @Before
    public void setup() {
        departures = Arrays.asList(
                new ScheduleItem(472, ScheduleItem.Calendar.WEEKDAY, "06:00"),
                new ScheduleItem(711, ScheduleItem.Calendar.WEEKDAY, "06:09"),
                new ScheduleItem(712, ScheduleItem.Calendar.WEEKDAY, "06:18"),
                new ScheduleItem(713, ScheduleItem.Calendar.WEEKDAY, "06:28"),
                new ScheduleItem(714, ScheduleItem.Calendar.WEEKDAY, "06:37"),
                new ScheduleItem(715, ScheduleItem.Calendar.WEEKDAY, "06:43")
        );

        someScheduleResult = new ScheduleResult();
        someScheduleResult.setRows(departures);
        someScheduleResult.setRowsAffected(0);
    }

    @Test
    public void testSerialization() {
        String serialized = gson.toJson(someScheduleResult);
        assertThat(serialized, equalTo(SOME_DEPARTURES_RESPONSE));
    }

    @Test
    public void testDeserialization() {
        ScheduleResult deserialized = gson.fromJson(SOME_DEPARTURES_RESPONSE, ScheduleResult.class);
        assertThat(deserialized, equalTo(someScheduleResult));
    }

}
