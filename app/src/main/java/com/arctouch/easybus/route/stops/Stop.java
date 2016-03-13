package com.arctouch.easybus.route.stops;

import java.io.Serializable;

/**
 * Model class that represents a stop.
 */
public class Stop implements Serializable, Comparable<Stop> {

    private long id;
    private String name;
    private int sequence;
    private long routeId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public long getRouteId() {
        return routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Stop another) {
        if (sequence < another.sequence) {
            return -1;
        } else if (sequence > another.sequence) {
            return 1;
        }
        return 0;
    }

}
