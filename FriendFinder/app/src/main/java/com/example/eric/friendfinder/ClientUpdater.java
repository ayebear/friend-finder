package com.example.eric.friendfinder;

import java.util.Map;

/**
 * Created by kevin on 10/10/15.
 */
public class ClientUpdater extends FragmentActivity {

    private String username;
    private String serverUrl;
    private LocationManager locationManager;

    private ClientUpdater(String _username, String url) {
        username = _username;
        serverUrl = url;
        login();
        start();
    }

    public void login() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = serverUrl + "/login?username=" + username;

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
        RequestQueue queue = Volley.newRequestQueue(this);
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
            updateCurrentLocationIfNeeded(location);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };

    private void updateClientInformation(Location location) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        float latitude = location.getLatitude();
        float longitude = location.getLongitude();
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

    public void start() {
        map = googleMap;
        clientMarkers = new HashMap<String, Marker>();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        map.setMyLocationEnabled(true);

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            double latitude = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
            double longitude = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
            addUpdateMarker(username, new LatLng(latitude, longitude));

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 18);
            map.animateCamera(cameraUpdate);
        } catch (SecurityException exception) {
            //Do something
        }
    }

}
