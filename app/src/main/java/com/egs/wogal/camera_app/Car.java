package com.egs.wogal.camera_app;

import java.io.Serializable;

/**
 * Created by wogal on 3/1/2017.
 */

public class Car  implements Serializable{
    private String make;
    private int year;
    private int iconID;
    private String condition;

    public String getCondition() {
        return condition;
    }

    public int getIconID() {
        return iconID;
    }

    public String getMake() {
        return make;
    }

    public int getYear() {
        return year;
    }

    public Car(String make, int year, int iconID, String condition) {
        this.make = make;
        this.year = year;
        this.iconID = iconID;
        this.condition = condition;
    }
}
