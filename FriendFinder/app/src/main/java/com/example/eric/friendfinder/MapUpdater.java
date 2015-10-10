package com.example.eric.friendfinder;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Map;

/**
 * Created by kevin on 10/10/15.
 */
public class MapUpdater implements Runnable{

    private GoogleMap map;

    private Map<String, Map<String, String>> rawClientInformation;
    private Map<String, Marker> clientMarkers;

    public MapUpdater(GoogleMap mmap, Map<String, Client> _clients) {
        map = mmap;
        clients = _clients;
        clientMarkers = new HashMap<>();
    }

    private void updateClients(Map<String, Map<String, String>> rawClientInformation) {
        Map<String, Map<String, String>> rawClientInformation = fetchAllClients();

        for (Map.Entry<String, Map<String, String>> entry : rawClientInformation.entrySet()) {
            String username = entry.getKey();
            Map<String, String> clientInfo = entry.getValue();

            float latitude = Float.parseFloat(clientInfo['latitude']);
            float longitude = Float.parseFloat(clientInfo['longitude']);
            addUpdateMarker(username, new LatLng(latitude, longitude));
        }
    }

    private void addUpdateMarker(String key, LatLng coordinates) {
        Marker clientMarker;
        if (clientMarkers.containsKey(key)) {
            clientMarker = clients.get(key);
            clientMarker.setPosition(coordinates);
        }
        else {
            clientMarker = map.addMarker(new MarkerOptions()
                    .position(coordinates)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer)));
            clientMarkers.put(key, clientMarker);
        }
        updateBearing(clientMarker, clientMarker.getRotation() + 1);

    }
}
