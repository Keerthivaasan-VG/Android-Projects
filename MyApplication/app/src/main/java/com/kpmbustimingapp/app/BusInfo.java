package com.kpmbustimingapp.app;


public class BusInfo {
    public String time;
    public String type;

    public BusInfo(String time, String type) {
        this.time = time;
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }
}
