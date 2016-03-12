package com.arctouch.easybus.model;

import java.io.Serializable;

public class Route implements Serializable {

    private long id;
    private String name;

    public Route(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
