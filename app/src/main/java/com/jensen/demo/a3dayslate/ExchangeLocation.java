package com.jensen.demo.a3dayslate;

import java.io.Serializable;
import java.util.ArrayList;

public class ExchangeLocation implements Serializable {
    private double latitude;
    private double longitude;

    public ExchangeLocation() {
        // Do not delete this!
    }

    public ExchangeLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
