package com.arctouch.easybus.route.schedule;

import java.io.Serializable;
import java.util.List;

/**
 * Model class that represents a schedule for a specific Calendar.
 */
public class Schedule implements Serializable {

    private List<ScheduleItem> scheduleItems;
    private ScheduleItem.Calendar calendar;

    public Schedule(List<ScheduleItem> scheduleItems, ScheduleItem.Calendar calendar) {
        this.scheduleItems = scheduleItems;
        this.calendar = calendar;
    }

    public List<ScheduleItem> getScheduleItems() {
        return scheduleItems;
    }

    public ScheduleItem.Calendar getCalendar() {
        return calendar;
    }

}
