package com.varadhismartek.pathshalamanagement.POJO_Classes;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by varadhi5 on 7/12/17.
 */

public class MarkerLists {

    private String stop_name;
    private Double latitude;
    private Double longitude;
    private LatLng latLng;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public MarkerLists(String stop_name, Double latitude, Double longitude, LatLng latLng) {
        this.stop_name = stop_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.latLng=latLng;
    }

    public String getStop_name() {
        return stop_name;
    }

    public void setStop_name(String stop_name) {
        this.stop_name = stop_name;
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
