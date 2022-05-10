package com.example.hw2updated.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hw2updated.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Fragment_Map extends Fragment {

    private SupportMapFragment supportMapFragment;
    private final float ZOOM = 15.0f;
    private final String MARKER_TEXT = "Player location";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //Initialize map fragment
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);


        return view;
    }

    /**
     * Sets the clicked record location
     * @param lat latitude of the location
     * @param lng longitude of the location
     */
    public void setMapLocation(double lat, double lng) {

        //Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback () {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                googleMap.clear();
                LatLng latlng = new LatLng(lat,lng);
                MarkerOptions markerOptions = new MarkerOptions().position(latlng).title(MARKER_TEXT);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,ZOOM));
                googleMap.addMarker(markerOptions);
            }

        });
    }
}
