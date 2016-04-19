package com.nscc.jared.biz;


public class Cell {
    public int id;
    public double lat;
    public double lng;
    public String name;

    public Cell(int id, double lat, double lng, String name)
    {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
    }
}
