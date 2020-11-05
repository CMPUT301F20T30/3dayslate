package com.jensen.demo.a3dayslate;

import java.io.Serializable;
import java.util.ArrayList;

/** Implements the Location class which holds a longitude and latitude to represent a location.
 Has both getter and setter methods for all the aspects.

 @author Jensen Khemchandani
 @see LocationActivity
 @see ReviewLocationActivity
 @version 1.0.0

 */

/**
 * Constructs ExchangeLocation instances
 */

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

    /**
     * Gets the current latitude of the Location
     * @return
     * Return the latitude as a double
     */

    public double getLatitude() {
        return latitude;
    }

    /**
     * This sets the current latitude of the Location
     * @param latitude
     * This is the latitude to be set
     */

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the current longitude of the Location
     * @return
     * Return the longitude as a double
     */

    public double getLongitude() {
        return longitude;
    }

    /**
     * This sets the current longitude of the Location
     * @param longitude
     * This is the latitude to be set
     */

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
