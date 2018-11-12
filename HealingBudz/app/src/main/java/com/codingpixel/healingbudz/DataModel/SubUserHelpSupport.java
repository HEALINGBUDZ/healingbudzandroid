package com.codingpixel.healingbudz.DataModel;

/**
 * Created by incubasyss on 02/04/2018.
 */

public class SubUserHelpSupport {
    int id;
    String name;

    public SubUserHelpSupport() {
    }

    public SubUserHelpSupport(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
