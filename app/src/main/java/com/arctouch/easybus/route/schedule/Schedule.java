package com.arctouch.easybus.route.schedule;

import com.google.common.base.Objects;

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

    @Override
    public String toString() {
        return calendar + " - " + scheduleItems.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(scheduleItems, calendar);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Schedule) {
            Schedule that = (Schedule) o;
            return Objects.equal(this.scheduleItems, that.scheduleItems) &&
                    Objects.equal(this.calendar,     that.calendar);
        }
        return false;
    }

}
