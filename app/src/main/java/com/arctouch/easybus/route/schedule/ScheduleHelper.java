package com.arctouch.easybus.route.schedule;

import com.arctouch.easybus.model.Schedule;
import com.arctouch.easybus.model.ScheduleItem;

import java.util.ArrayList;
import java.util.List;

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
