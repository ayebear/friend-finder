package com.example.eric.friendfinder;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Map<String, Map<String, String>> clientRawInformation;
    private Map<String, Client> clients;
    private String serverUrl ="http://104.131.102.127:5000";
    private String username;
    private MapUpdater mapUpdater;
    private ClientUpdater clientUpdater;
    Handler h = new Handler();
    private static final String TAG = MapsActivity.class.getSimpleName();


    private void updateClients() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = serverUrl + "/getallclients";

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Update all clientRawInformation
                    // Parse JSON string, and place values into clientRawInformationmap
                    try {
                        // Iterate through all users
                        // {"Kevin": {"longitude": "4557", "latitude": "345456"}, "Eric": {}}
                        JSONObject json = new JSONObject(response);
                        clientRawInformation.clear();
                        for (Iterator<String> iter = json.keys(); iter.hasNext();) {
                            String user = iter.next();

                            // Setup user data map
                            clientRawInformation.put(user, new HashMap<String, String>());

                            // Parse and store user data
                            JSONObject userData = json.getJSONObject(user);

                            for (Iterator<String> userDataIter = json.keys(); userDataIter.hasNext();) {
                                String fieldName = userDataIter.next();
                                String fieldValue = json.getString(fieldName);
                                clientRawInformation.get(user).put(fieldName, fieldValue);
                            }
                        }
                    } catch (JSONException e) {
//                            e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void updatePosition() {
        // Post new position
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        System.out.println("Testing");
        Log.d(TAG, "**************** Starting up!!!");

        clientRawInformation = new HashMap<>();
        clientRawInformation.put(username, new HashMap<String, String>());
        clients = new HashMap<>();
        username = "User" + (int)(Math.random() * 1000);
        clientUpdater = new ClientUpdater(username, serverUrl, this);

    }

    @Override
    protected void onDestroy() {
        clientUpdater.logout();
        super.onDestroy();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mapUpdater = new MapUpdater(googleMap, clientRawInformation);


        final int delay = 500; //milliseconds

        h.postDelayed(new Runnable() {
            public void run() {
                updateClients();
                mapUpdater.updateClientMarkers();
                h.postDelayed(this, delay);
            }
        }, delay);
    }
}
