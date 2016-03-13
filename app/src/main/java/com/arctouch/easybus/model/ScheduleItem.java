package com.arctouch.easybus.model;

import java.io.Serializable;

/**
 * Model class that represents one item of a route's schedule.
 */
public class ScheduleItem implements Serializable {

    public enum Calendar {WEEKDAY, SATURDAY, SUNDAY};

    private long id;
    private Calendar calendar;
    private String time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return calendar + " - " + time;
    }

}
