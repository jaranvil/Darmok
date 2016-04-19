package com.nscc.jared.biz;


import android.graphics.Color;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.nscc.jared.landgrab.R;

public class CellSupport {

    private String username;
    private int supporters;
    private int cellId;
    public double lat;
    public double lng;

    public int R = 0;
    public int G = 0;
    public int B = 0;
    public String name;
    public BitmapDescriptor icon;

    public CellSupport(String username, int supporters, int cellId, double lat, double lng, String name, String color)
    {
        this.username = username;
        this.supporters = supporters;
        this.cellId = cellId;
        this.lat = lat;
        this.lng = lng;
        this.name = name;

        if (color.equals("Red"))
        {
            R = 255;
            icon = BitmapDescriptorFactory.fromResource(com.nscc.jared.landgrab.R.drawable.red_marker);
        }
        else if (color.equals("Blue"))
        {
            B = 255;
            icon = BitmapDescriptorFactory.fromResource(com.nscc.jared.landgrab.R.drawable.blue_marker);
        }
        else if (color.equals("Yellow"))
        {
            R = 255;
            G = 255;
            icon = BitmapDescriptorFactory.fromResource(com.nscc.jared.landgrab.R.drawable.yellow_marker);
        }
        else if (color.equals("Green"))
        {
            G = 255;
            icon = BitmapDescriptorFactory.fromResource(com.nscc.jared.landgrab.R.drawable.green_marker);
        }

    }





    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public int getSupporters() {
        return supporters;
    }

    public void setSupporters(int supporters) {
        this.supporters = supporters;
    }

    public int getCellId() {
        return cellId;
    }

    public void setCellId(int cellId) {
        this.cellId = cellId;
    }


}
