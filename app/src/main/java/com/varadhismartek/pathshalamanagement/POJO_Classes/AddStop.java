package com.varadhismartek.pathshalamanagement.POJO_Classes;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by varadhi5 on 30/11/17.
 */

public class AddStop implements Serializable {

    public AddStop() {

    }

    public String getStop_number() {
        return stop_number;
    }

    public void setStop_number(String stop_number) {
        this.stop_number = stop_number;
    }

    public String getStop_name() {
        return stop_name;
    }

    public void setStop_name(String stop_name) {
        this.stop_name = stop_name;
    }

    private String stop_number;
    private String stop_name;
    private String stop_distance;
    private String stop_time;
    private String Origin,Dest,latLng;


    public String getStop_distance() {
        return stop_distance;
    }

    public void setStop_distance(String stop_distance) {
        this.stop_distance = stop_distance;
    }

    public String getStop_time() {
        return stop_time;
    }

    public void setStop_time(String stop_time) {
        this.stop_time = stop_time;
    }

    public String getOrigin() {
        return Origin;
    }

    public String getDest() {
        return Dest;
    }

    public String getLatLng() {
        return latLng;
    }

    /*public AddStop(int stop_number, String stop_name) {
            this.stop_number = stop_number;
            this.stop_name = stop_name;
        }*/
    public AddStop(String stop_number,String stop_name,String stop_distance,String stop_time,String origin,String dest,String latlng){
        this.stop_number = stop_number;
        this.stop_name = stop_name;
        this.stop_distance=stop_distance;
        this.stop_time=stop_time;
        this.Origin=origin;
        this.Dest = dest;
        this.latLng = latlng;

    }
}
