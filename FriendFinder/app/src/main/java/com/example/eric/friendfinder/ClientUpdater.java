package com.example.eric.friendfinder;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import java.util.Map;

/**
 * Created by kevin on 10/10/15.
 */
public class ClientUpdater {

    private String username;
    private String serverUrl;
    private LocationManager locationManager;
    Context context;

    public ClientUpdater(String _username, String url, Context context) {
        username = _username;
        serverUrl = url;
        this.context = context;
        login();
        start();
    }

    public String getUsername() {
        return username;
    }

    public void login() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = serverUrl + "/login?username=" + (username+(int)(Math.random() * 1000));

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void logout() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = serverUrl + "/logout?username=" + username;

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    // Define a listener that responds to location updates
    private LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            updateClientInformation(location);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };

    private void updateClientInformation(Location location) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String url = String.format("%s/update?username=%s&latitude=%f&longitude=%f", serverUrl, username, latitude, longitude);

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public LatLng getCurrentCoordinates() {
        try {
            double latitude = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
            double longitude = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
            return new LatLng(latitude, longitude);
        } catch (SecurityException exception) {
            return new LatLng(0, 0);
        }

    }

    public void start() {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        map.setMyLocationEnabled(true);

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            updateClientInformation(new Location(LocationManager.NETWORK_PROVIDER));
        } catch (SecurityException exception) {
            //Do something
        }
    }

}
