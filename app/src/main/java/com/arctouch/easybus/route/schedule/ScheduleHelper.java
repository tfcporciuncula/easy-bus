package com.arctouch.easybus.route.schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper that builds a Schedule for a specific Calendar given the complete list of ScheduleItem.
 */
public class ScheduleHelper {

    public static Schedule buildSpecificSchedule(List<ScheduleItem> scheduleItems, ScheduleItem.Calendar calendar) {
        List<ScheduleItem> specificSchedule = new ArrayList<>();
        for (ScheduleItem scheduleItem : scheduleItems) {
            if (scheduleItem.getCalendar().equals(calendar)) {
                specificSchedule.add(scheduleItem);
            }
        }
        return new Schedule(specificSchedule, calendar);
    }

}
