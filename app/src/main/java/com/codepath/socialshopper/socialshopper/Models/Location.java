package com.codepath.socialshopper.socialshopper.Models;

import org.parceler.Parcel;

/**
 * Created by saripirala on 10/21/17.
 */

@Parcel(analyze={Location.class})
public class Location {

    Double latitude;
    Double longitude;

    public  Location(){
    }

    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
