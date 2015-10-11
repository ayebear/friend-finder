package com.example.eric.friendfinder;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Date;

/**
 * Created by kevin on 10/10/15.
 */
public class Client {

    Client(String _username) {
        username = _username;
    }

    public Marker marker;
    public String username;
    public LatLng actualCoordinate;
    public LatLng estimatedCoordinate;
    public Date lastUpdated;

    public float bearing;
}
