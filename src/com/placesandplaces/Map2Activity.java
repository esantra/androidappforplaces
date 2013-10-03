package com.placesandplaces;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class Map2Activity extends Activity {
	 
    // Google Map
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_places);
 
        try {
            // Loading map
            initilizeMap();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
 
    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
 
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        
        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        
        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        
        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);
        
        googleMap.setMyLocationEnabled(true); // false to disable
        // Sets the map type to be "hybrid"
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        
        LatLngBounds US = new LatLngBounds(
        		  new LatLng(-44, 113), new LatLng(-10, 154));
        
        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();
        
        SecondActivity places = new SecondActivity();
        
        
        //latitude = places.placesListItems.get(1)

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(US.getCenter(), 10));
        		
        
        //set the longitude and latitude for each marker
        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here."));
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap.setTrafficEnabled(true);
        CameraPosition cameraPosition = new CameraPosition.Builder()
        .zoom(17)                   // Sets the zoom
        .target(new LatLng(latitude, longitude))
        .bearing(90)                // Sets the orientation of the camera to east
        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
        .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));		
        
    
      //set the longitude and latitude for the marker for the place in the single view
        googleMap.addMarker(new MarkerOptions().position(new LatLng(SinglePlaceActivity.placeDetails.result.geometry.location.lat, SinglePlaceActivity.placeDetails.result.geometry.location.lng)).title("Info Screen Location"));
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap.setTrafficEnabled(true);
	
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
        
        
    }
 
}