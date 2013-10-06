package com.placesandplaces;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Map2Activity extends Activity {
	 
    // Google Map
    private GoogleMap googleMap;
    
    //connection detector
    private ConnectionDetector cd;
    int ARRAYLENGTH = SecondActivity.ARRAYLENGTH;

	public static String reference;
    
	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String MAP_STRING = "maput";
	public Double[] lat = new Double[ARRAYLENGTH];
	public Double[] lng = new Double[ARRAYLENGTH];
	public static List<Double> allMatcheslat = new ArrayList<Double>();
	public static List<Double> allMatcheslng = new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_places);
		Intent i = getIntent();
		// flag for Internet connection status
		Boolean isInternetPresent = false;
		// Alert Dialog Manager
		AlertDialogManager alert = new AlertDialogManager();
		
		cd = new ConnectionDetector(getApplicationContext());
		
	   
		
		// Check if Internet present
				isInternetPresent = cd.isConnectingToInternet();
				if (!isInternetPresent) {
					// Internet Connection is not present
					alert.showAlertDialog(this,
							"Internet Connection Error",
							"Please connect to working Internet connection", false);
					// stop executing code by return
					return;
				}
				
				 //gps tracker
			    GPSTracker gps = new GPSTracker(this);

				// creating GPS Class object
				 gps = new GPSTracker(this);

				// check if GPS location can get
				if (gps.canGetLocation()) {
					Log.d("Your Location", "latitude:" + gps.getLatitude()
							+ ", longitude: " + gps.getLongitude());
				} else {
					// Can't get user's current location
					alert.showAlertDialog(this, "GPS Status",
							"Couldn't get location information. Please enable GPS",
							false);
					// stop executing code by return
					return;
				}
        

			// Place reference id
     		String reference2 = i.getStringExtra(KEY_REFERENCE);
     		//String maput = i.getStringExtra(MAP_STRING);
     		
     		//display 
     		Log.d("Your Location", "This is the maput variable " + reference2);
     		
     		//place the passed string elements into an array
     		String[] maputArray = reference2.split(" "); 
     		
     		int reflen = maputArray[0].length();
     		
     	    reference = maputArray[0].substring(11, reflen-1);
     	    
     	   Log.d("maput", "The location reference is reference: " + reference);

     		for(int p = 0; p < maputArray.length; p++){
     		
     		
     		
     		String str = maputArray[p];
     		
     		//if the first 3 digits are numbers, add the array element to the list
     		if(str.contains("lat:")){
     		   // contains a number
     			try{
     				String coord = maputArray[p+2].replaceAll("^\\[|\\]$", "");
     				allMatcheslat.add(Double.parseDouble(coord));
     			}
     			catch(Exception e){
     				Log.d("exception", "Exception " + e);
     			}
     		} else{
     		   // does not contain a number
     		   // do nothing
     		}
     		
     		//if the first 3 digits are numbers, add the array element to the list
     		if(str.contains("lng:")){
     		   // contains a number
     			try{
     				//turn the string into a double and parse all brackets and spaces
     				String coord = maputArray[p+2].replaceAll("^\\[|\\]$", "");
     				allMatcheslng.add(Double.parseDouble(coord));
     			}
     			catch(Exception e){
     				Log.d("exception", "Exception " + e);
     			}
     		} else{
     		   // does not contain a number
     		   // do nothing
     		}
     		
     		
     		Log.d("Your Location", "This is the maput array " + p + " " + maputArray[p].toString());
     		}//end for
     		
     		Log.d("matches", "These are all the lat matches " + allMatcheslat.toString());
     		Log.d("matches", "These are all the lng matches " + allMatcheslng.toString());
     		
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
        googleMap.setMapType(googleMap.MAP_TYPE_HYBRID);
        googleMap.setTrafficEnabled(true);
        CameraPosition cameraPosition = new CameraPosition.Builder()
        .zoom(10)                   // Sets the zoom
        .target(new LatLng(latitude, longitude))
        .bearing(90)                // Sets the orientation of the camera to east
        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
        .build();                   // Creates a CameraPosition from the builder
        //googleMap.setMapTypeId(googleMap.maps.MapTypeId.ROADMAP);
        //mapTypeId: googleMap.maps.MapTypeId.ROADMAP;
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));	
        
      //  Double lat1 = SinglePlaceActivity.placeDetails.result.geometry.location.lat;
       // Double lng1 = SinglePlaceActivity.placeDetails.result.geometry.location.lng;
        
        //gps tracker
	    GPSTracker gps = new GPSTracker(this);
	    
	   
        //--------------need to put this in a loop for induction purposes
	    for(int point = 0; point < allMatcheslat.size(); point++){
	    	
	    	lat[point] = allMatcheslat.get(point);
	    	lng[point] = allMatcheslng.get(point);
	   

    
      //set the longitude and latitude for the marker for the place in the single view
        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat[point], lng[point])).
        		title("Info Screen Location"));
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap.setTrafficEnabled(true);
	    }
        
    	// button show on map
    	Button next = (Button) findViewById(R.id.infoButton);
    	/** Button click event for shown on map */
    	next.setOnClickListener(new View.OnClickListener() {

    		@Override
    		public void onClick(View arg0) {
                //Perform action on click
     			// Starting new intent
     			Intent in = new Intent(getApplicationContext(),
     					SinglePlaceActivity.class);
     			
     			// Sending place reference id to single place activity
     			// place reference id used to get "Place full details"
     			in.putExtra(KEY_REFERENCE, reference);
     			// in.putExtra(KEY_REFERENCE, arrayreferences);
     			startActivity(in);
    		}
    	});
	
    }
    
    

 
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
        
        
    }
    
    
    
 

 
}