package com.varadhismartek.pathshalamanagement.POJO_Classes;

import java.io.Serializable;
import java.util.List;

/**
 * Created by varadhi5 on 30/11/17.
 */

public class Submit implements Serializable {

    private String routeno,busno,busname,seating,starting,destiny,driver_name,driver_mobno,assit_name,caretaker_name,trspt_mgr_name,trspt_mgr_mobno,gps_details,stop_counts,stop_distance,stop_time;
    private List<AddStop> addStops;

    public Submit() {

    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }

    public Submit(String label, String datas) {
        this.label = label;
        this.datas = datas;
    }

    private String label,datas;

    public String getRouteno() {
        return routeno;
    }

    public void setRouteno(String routeno) {
        this.routeno = routeno;
    }

    public String getBusno() {
        return busno;
    }

    public void setBusno(String busno) {
        this.busno = busno;
    }

    public String getBusname() {
        return busname;
    }

    public void setBusname(String busname) {
        this.busname = busname;
    }

    public String getSeating() {
        return seating;
    }

    public void setSeating(String seating) {
        this.seating = seating;
    }

    public String getStarting() {
        return starting;
    }

    public void setStarting(String starting) {
        this.starting = starting;
    }

    public String getDestiny() {
        return destiny;
    }

    public void setDestiny(String destiny) {
        this.destiny = destiny;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_mobno() {
        return driver_mobno;
    }

    public void setDriver_mobno(String driver_mobno) {
        this.driver_mobno = driver_mobno;
    }

    public String getAssit_name() {
        return assit_name;
    }

    public void setAssit_name(String assit_name) {
        this.assit_name = assit_name;
    }

    public String getCaretaker_name() {
        return caretaker_name;
    }

    public void setCaretaker_name(String caretaker_name) {
        this.caretaker_name = caretaker_name;
    }

    public String getTrspt_mgr_name() {
        return trspt_mgr_name;
    }

    public void setTrspt_mgr_name(String trspt_mgr_name) {
        this.trspt_mgr_name = trspt_mgr_name;
    }

    public String getTrspt_mgr_mobno() {
        return trspt_mgr_mobno;
    }

    public void setTrspt_mgr_mobno(String trspt_mgr_mobno) {
        this.trspt_mgr_mobno = trspt_mgr_mobno;
    }

    public String getGps_details() {
        return gps_details;
    }

    public void setGps_details(String gps_details) {
        this.gps_details = gps_details;
    }

    public String getStop_counts() {
        return stop_counts;
    }

    public void setStop_counts(String stop_counts) {
        this.stop_counts = stop_counts;
    }

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

    public List<AddStop> getAddStops() {
        return addStops;
    }

    public void setAddStops(List<AddStop> addStops) {
        this.addStops = addStops;
    }

    public Submit(String route_no, String bus_no, String bus_name, String seating_cpcity, String starting_point, String destiny_point, String driver_name, String driver_mobno, String assistant_name, String care_taker, String transport_mgr_name, String transport_mgr_mobno, String gps_details, String stop_counts) {

       this.routeno=route_no;
       this.busno=bus_no;
       this.seating=seating_cpcity;
       this.busname=bus_name;
       this.starting=starting_point;
       this.destiny=destiny_point;
       this.driver_name=driver_name;
       this.driver_mobno=driver_mobno;
       this.assit_name=assistant_name;
       this.caretaker_name=care_taker;
       this.trspt_mgr_name=transport_mgr_name;
       this.trspt_mgr_mobno=transport_mgr_mobno;
       this.gps_details=gps_details;
       this.stop_counts=stop_counts;

    }
}
