package com.example.eric.friendfinder;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevin on 10/10/15.
 */
public class MapUpdater {

    private GoogleMap map;

    private Map<String, Map<String, String>> rawClientInformation;
    private Map<String, Marker> clientMarkers;

    public MapUpdater(GoogleMap mmap, Map<String, Map<String, String>> _clients) {
        map = mmap;
        rawClientInformation = _clients;
        clientMarkers = new HashMap<>();
    }

    public void updateClientMarkers() {
//        Map<String, Map<String, String>> rawClientInformation = fetchAllClients();

        for (Map.Entry<String, Map<String, String>> entry : rawClientInformation.entrySet()) {
            String username = entry.getKey();
            Map<String, String> clientInfo = entry.getValue();
            System.out.println(username);

            float latitude = Float.parseFloat(clientInfo.get("latitude"));
            float longitude = Float.parseFloat(clientInfo.get("longitude"));
            addUpdateMarker(username, new LatLng(latitude, longitude));
        }
    }

    private void addUpdateMarker(String key, LatLng coordinates) {
        Marker clientMarker;
        if (clientMarkers.containsKey(key)) {
            clientMarker = clientMarkers.get(key);
            clientMarker.setPosition(coordinates);
        }
        else {
            clientMarker = map.addMarker(new MarkerOptions()
                    .position(coordinates)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer)));
            clientMarkers.put(key, clientMarker);
        }
    }

    public void moveCamera(LatLng coordinates) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coordinates, 18);
        map.animateCamera(cameraUpdate);
    }
}
