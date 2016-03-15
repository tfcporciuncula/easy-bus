package com.arctouch.easybus.route.schedule;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Model class that represents one item of a route's schedule.
 */
public class ScheduleItem implements Serializable {

    public enum Calendar {WEEKDAY, SATURDAY, SUNDAY}

    private long id;
    private Calendar calendar;
    private String time;

    public ScheduleItem(long id, Calendar calendar, String time) {
        this.id = id;
        this.calendar = calendar;
        this.time = time;
    }

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

    @Override
    public int hashCode() {
        return Objects.hashCode(id, calendar, time);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ScheduleItem) {
            ScheduleItem that = (ScheduleItem) o;
            return Objects.equal(this.id,        that.id)       &&
                    Objects.equal(this.calendar, that.calendar) &&
                    Objects.equal(this.time,     that.time);
        }
        return false;
    }

}
