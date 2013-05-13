package com.androidhive.googleplacesandmaps;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class SecondActivity extends Activity {

	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;
	
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	// Google Places
	GooglePlaces googlePlaces;

	// Places List
	PlacesList nearPlaces;
	PlacesList nearPlaces2;
	PlacesList[] nearPlaces_i = new PlacesList[100];

	// GPS Location
	GPSTracker gps;

	// Button
	Button btnShowOnMap;

	// Progress dialog
	ProgressDialog pDialog;
	
	//name of place 2
	public static String name;
	
	//name of second place - well reference to it
	public static String secondPlaceReference;
	
	public static int m = 0; //counter
	public static int s = 0; //counter
	
	
	// Places ListView
	ListView lv;
	ListView lv2;
	
	// ListItems data
	ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();
	// ListItems data to hold secondary list
	ArrayList<HashMap<String, String>> placesListItems2 = new ArrayList<HashMap<String,String>>();
		
	public static String types;
	
	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name
	public static String KEY_INFORMATION = "information"; // Place area name
	//declare a variable array to hold places
	public static Double[] placesArrayLat =  new Double[1000];
	public static Double[] placesArrayLng =  new Double[1000];
	public Double[] p_i_lat =  new Double[1000];
	public Double[] p_i_lng =  new Double[1000];
	public static String[] placesName =  new String[1000];
	public String name_i;
	public String[] distanceVariable_i = new String[100];
	public Globals g = Globals.getInstance();
	public Place place_i[][] = new Place[100][100];
	public static Double[] distance_i = new Double[100];
	DecimalFormat[] myFormat_i = new DecimalFormat[100];
	public static String[] types_i =  new String[100];
	public static String strMapPut;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		isInternetPresent = cd.isConnectingToInternet();
		if (!isInternetPresent) {
			// Internet Connection is not present
			alert.showAlertDialog(SecondActivity.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}

		// creating GPS Class object
		gps = new GPSTracker(this);

		// check if GPS location can get
		if (gps.canGetLocation()) {
			Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
		} else {
			// Can't get user's current location
			alert.showAlertDialog(SecondActivity.this, "GPS Status",
					"Couldn't get location information. Please enable GPS",
					false);
			// stop executing code by return
			return;
		}

		// Getting listview
		lv = (ListView) findViewById(R.id.list);
		// Getting listview2
		lv2 = (ListView) findViewById(R.id.list2);
		
		// button show on map
		btnShowOnMap = (Button) findViewById(R.id.btn_show_map);

		// calling background Async task to load Google Places
		// After getting places from Google all the data is shown in listview
		new LoadPlaces().execute();

		/** Button click event for shown on map */
		btnShowOnMap.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(),
						PlacesMapActivity.class);
				// Sending user current geo location
				i.putExtra("user_latitude", Double.toString(gps.getLatitude()));
				i.putExtra("user_longitude", Double.toString(gps.getLongitude()));
				
				// passing near places to map activity
				i.putExtra("near_places", nearPlaces);
				// staring activity
				startActivity(i);
			}
		});
		
		
		/**
		 * ListItem click event
		 * On selecting a listitem SinglePlaceActivity is launched
		 * */
		lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	// getting values from selected ListItem
                String reference = ((TextView) view.findViewById(R.id.reference)).getText().toString();
                
                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        SinglePlaceActivity.class);
                
                // Sending place reference id to single place activity
                // place reference id used to get "Place full details"
                in.putExtra(KEY_REFERENCE, reference);
                startActivity(in);
            }
            
        });
		
		lv2.setOnItemClickListener(new OnItemClickListener() {
			 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	// getting values from selected ListItem
                String reference = ((TextView) view.findViewById(R.id.reference)).getText().toString();
                
                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        SinglePlaceActivity.class);
                
                // Sending place reference id to single place activity
                // place reference id used to get "Place full details"
                in.putExtra(KEY_REFERENCE, reference);
                startActivity(in);
            }
            
        });
		
		
	}

	/**
	 * Background Async Task to Load Google places
	 * */
	class LoadPlaces extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SecondActivity.this);
			pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
			// creating Places class object
			googlePlaces = new GooglePlaces();
			
			try {
			    types = g.getLocation1();
				String types2 = g.getLocation2();
				
				int u = 0; //should be the number of generated texts
				
				nearPlaces = googlePlaces.search(gps.getLatitude(),
						gps.getLongitude(), types);
				
				nearPlaces2 = googlePlaces.search(gps.getLatitude(),
						gps.getLongitude(), types2);
				
				
				try{
				for(int r = 0; r < g.getLocationVari(); r++){
					
					//this needs to be made into an array for INDUCTION
					types_i[r] = g.getLocationi(r);
					
					//this needs to be made into an array for INDUCTION
					nearPlaces_i[r] = googlePlaces.search(gps.getLatitude(),
							gps.getLongitude(), types_i[r]);
				
				}//end for 
				}//end try
				catch(Exception e){
					Log.d("one", "this is a null pointer exception");
					
				}//end catch
				

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * and show the data in UI
		 * Always use runOnUiThread(new Runnable()) to update UI from background
		 * thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				@SuppressWarnings("null")
				public void run() {
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					// Get json response status
					String status1 = nearPlaces.status;
					String status2 = nearPlaces2.status;
					
					
					int y = 0; //counter variable for array
					
					// Check for all possible status
					if(status1.equals("OK")){
						// Successfully got places details
						if (nearPlaces.results != null) {
							// loop through each place
							for (Place p : nearPlaces.results) {
								HashMap<String, String> map = new HashMap<String, String>();
							
								
								//assigns the place to the places array variable
								placesArrayLat[y] = p.geometry.location.lat;
								placesArrayLng[y] = p.geometry.location.lng;
								placesName[y] = p.name; 
								
								
								
								// Place reference won't display in listview - it will be hidden
								// Place reference is used to get "place full details"
								map.put(KEY_REFERENCE, p.reference);
								
								Double distance = DistanceCalculation.distFrom(gps.getLatitude(), gps.getLongitude(), p.geometry.location.lat, p.geometry.location.lng);
								
								DecimalFormat myFormat = new DecimalFormat("0.0");
								String distanceVariable = myFormat.format(distance);
			
								// Place name
								//--- so one mile needs to be replaced with a variable that tells the 
								//distance from one point to another
								 map.put(KEY_NAME, p.name + " " + distanceVariable);
								
								
								
								// adding HashMap to ArrayList
								placesListItems.add(map);
								//increment counter
								y = y + 1;
							}
							// list adapter
							ListAdapter adapter = new SimpleAdapter(SecondActivity.this, placesListItems,
					                R.layout.list_item,
					                new String[] { KEY_REFERENCE, KEY_NAME}, new int[] {
					                        R.id.reference, R.id.name });
							
								
							// Adding data into listview
							// lv.setAdapter(adapter);
							lv.setVisibility(View.INVISIBLE);
							
							int i = 0; //places counter
							
							
							try{
							for(int aa =0; aa < 4; aa++){
								
                            //--- THIS NEEDS TO RUN ON A LOOP FOR INDUCTION PURPOSES
								if (nearPlaces_i[aa].results != null) {
									// loop through each place
									for (Place p_i : nearPlaces_i[aa].results) {
										
										
										//set Place p_i to a variable every time
										place_i[m][s] = p_i;
										Log.d("array", "variable value" + Integer.toString(m) +
												Integer.toString(s));
										m++;
										s++;

									}//end for
									
								}//end if
							}//end for
							
							}//end try
							catch(Exception e){
								Log.d("one", "this is an induction error");
							}//end catch
						
						int n = 0;
						int yy = 0;
					
						// Successfully got places details
						if (nearPlaces2.results != null) {
							// loop through each place
							for (Place p : nearPlaces2.results) {
								
								if(i < 20){
								HashMap<String, String> map2 = new HashMap<String, String>();
								
								// Place reference won't display in listview - it will be hidden
								// Place reference is used to get "place full details"
								map2.put(KEY_REFERENCE, p.reference);
								
								
								secondPlaceReference = p.reference;

								Double distance2 = DistanceCalculation.distFrom(gps.getLatitude(), gps.getLongitude(), 
										p.geometry.location.lat, p.geometry.location.lng);
								DecimalFormat myFormat = new DecimalFormat("0.0");
								String distanceVariable2 = myFormat.format(distance2);
							
							    Double distancefrom = DistanceCalculation.distFrom(p.geometry.location.lat, p.geometry.location.lng, 
							    		placesArrayLat[yy], placesArrayLng[yy]);
								DecimalFormat myFormatp = new DecimalFormat("0.0");
								String distanceFr = myFormatp.format(distancefrom);
								name = placesName[yy].toString();
								
								
								
								
								//--- THIS NEEDS TO RUN ON A LOOP FOR INDUCTION PURPOSES
							   strMapPut = (p.name + " " +  distanceVariable2 + "\n" + name + " " 
										+ distanceFr + "\n");
								
							   int ww = i;
							   for(int aa =0; aa < g.buttonPushCount; aa++){
								  // adfadf
								   //for(int bb=0; bb < m; bb++){
									   //method to place places on string
								      
									   StringPut(n, ww, ww);
									   ww++;
									   Log.d("array", "aa is " + aa);
									   //Log.d("array", "m is " + m + " s is " + s);
								 // }//end for
							   }//end for
								
								
								map2.put(KEY_NAME, strMapPut);
								

								// adding HashMap to ArrayList
								placesListItems2.add(map2);
								n++;
								i = i + 1;
								yy++;
								}//end for
								else{
									
								}//do nothing
							}//end map
								
							
							// list adapter
							ListAdapter adapter2 = new SimpleAdapter(SecondActivity.this, placesListItems2,
					                R.layout.list_item,
					                new String[] { KEY_REFERENCE, KEY_NAME, KEY_INFORMATION}, new int[] {
					                        R.id.reference, R.id.name });
							
							// Adding data into listview
							lv2.setAdapter(adapter2);
						}
					}
					}
					
					else if(status1.equals("ZERO_RESULTS")){
						// Zero results found
						alert.showAlertDialog(SecondActivity.this, "Near Places",
								"Sorry no places found. Try to change the types of places",
								false);
					}
					else if(status1.equals("UNKNOWN_ERROR"))
					{
						alert.showAlertDialog(SecondActivity.this, "Places Error",
								"Sorry unknown error occured.",
								false);
					}
					else if(status1.equals("OVER_QUERY_LIMIT"))
					{
						alert.showAlertDialog(SecondActivity.this, "Places Error",
								"Sorry query limit to google places is reached",
								false);
					}
					else if(status1.equals("REQUEST_DENIED"))
					{
						alert.showAlertDialog(SecondActivity.this, "Places Error",
								"Sorry error occured. Request is denied",
								false);
					}
					else if(status1.equals("INVALID_REQUEST"))
					{
						alert.showAlertDialog(SecondActivity.this, "Places Error",
								"Sorry error occured. Invalid Request",
								false);
					}
					else if(status2.equals("ZERO_RESULTS")){
						// Zero results found
						alert.showAlertDialog(SecondActivity.this, "Near Places",
								"Sorry no places found. Try to change the types of places",
								false);
					}
					else if(status2.equals("UNKNOWN_ERROR"))
					{
						alert.showAlertDialog(SecondActivity.this, "Places Error",
								"Sorry unknown error occured.",
								false);
					}
					else if(status2.equals("OVER_QUERY_LIMIT"))
					{
						alert.showAlertDialog(SecondActivity.this, "Places Error",
								"Sorry query limit to google places is reached",
								false);
					}
					else if(status2.equals("REQUEST_DENIED"))
					{
						alert.showAlertDialog(SecondActivity.this, "Places Error",
								"Sorry error occured. Request is denied",
								false);
					}
					else if(status2.equals("INVALID_REQUEST"))
					{
						alert.showAlertDialog(SecondActivity.this, "Places Error",
								"Sorry error occured. Invalid Request",
								false);
					}
					
					
					else
					{
						alert.showAlertDialog(SecondActivity.this, "Places Error",
								"Sorry error occured.",
								false);
					}
				}
			});

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void StringPut(int n, int m, int s){
		
		
		
		try{
		    //this distance variable needs to loop????
			distance_i[n] = DistanceCalculation.distFrom(placesArrayLat[n],  placesArrayLng[n], 
			place_i[n][n].geometry.location.lat, place_i[n][n].geometry.location.lng);
			Log.d("filter", "  place distance calc " + distance_i[n].toString());
			
			 myFormat_i[n] = new DecimalFormat("0.0");
			distanceVariable_i[n] = myFormat_i[n].format(distance_i[n]);
			
			Log.d("filter", "this is the place name");
			Log.d("filter", place_i[n][n].name);
			Log.d("filter", distanceVariable_i[n]);
			Log.d("filter", "this app has looped " + n + " times");
			
			}//end try
			catch(Exception e){
				Log.d("one", "this is an error");
			}//end catch
		
		
		try{
				//this is different places with the same distance variable
		
			
			strMapPut = strMapPut + place_i[m][s].name.toString()  
					+ " " + distanceVariable_i[n] + "\n";
			
		}//end try
		catch(Exception e){
			Log.d("one", "This is a null variable");
		}//end catch
		
	}//end String put method

	

}
