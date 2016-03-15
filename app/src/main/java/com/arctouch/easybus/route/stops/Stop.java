package com.arctouch.easybus.route.stops;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Model class that represents a stop.
 */
public class Stop implements Serializable, Comparable<Stop> {

    private long id;
    private String name;
    private int sequence;

    @SerializedName("route_id")
    private long routeId;

    public Stop(long id, String name, int sequence, long routeId) {
        this.id = id;
        this.name = name;
        this.sequence = sequence;
        this.routeId = routeId;
    }

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

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, sequence, routeId);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Stop) {
            Stop that = (Stop) o;
            return Objects.equal(this.id,        that.id)       &&
                    Objects.equal(this.name,     that.name)     &&
                    Objects.equal(this.sequence, that.sequence) &&
                    Objects.equal(this.routeId,  that.routeId);
        }
        return false;
    }

}
