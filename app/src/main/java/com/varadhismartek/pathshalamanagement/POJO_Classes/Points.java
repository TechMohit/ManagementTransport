package com.varadhismartek.pathshalamanagement.POJO_Classes;

/**
 * Created by varadhi5 on 14/12/17.
 */

public class Points {
    private String start;
    private String destiny;

    public Points(String start, String destiny) {
        this.start = start;
        this.destiny = destiny;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDestiny() {
        return destiny;
    }

    public void setDestiny(String destiny) {
        this.destiny = destiny;
    }
}
