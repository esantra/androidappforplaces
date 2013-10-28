package com.placesandplaces;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.placesandplaces.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

@SuppressLint("UseSparseArrays")
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
	PlacesList[] nearPlaces_i = new PlacesList[ARRAYLENGTH];
	
	// GPS Location
	public static GPSTracker gps;
	// Button
	Button btnShowOnMap;
	// Progress dialog
	ProgressDialog pDialog;
	// name of place 2
	public static String name;
	// Places ListViews
	ListView lv;
	public static ListView lv2;
	// Variable to hold place types such as cafe or bar
	public static String types;
	// New object to access global variables
	// mostly counter variables used for induction
	public Globals g = Globals.getInstance();
	// Constant value to hold size of arrays so that induction is true
	// this setting is multiplied each time the user presses the button to add a
	// location
	// so that induction is truly possible without using lists and collections -
	// some are
	// still utilized
	public static final Integer ARRAYLENGTH = (Globals.buttonPushCount + 1) * 1000;
	// counter and increment variables
	public static int rr;
	public static int wcounter = 0;
	private int hh = 0;
	public static int nameCount = 2;
	public static int lvid;
	public static String reference;
	int counter = 0;
	public ListAdapter adapter2;
	public static String referenceBefore;

	// KEY Strings - strings that hold references for API access
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_LATTITUDE = "lattitude"; // id of the place
	public static String KEY_LONGITUDE = "longitude"; // id of the place
	public static String MAP_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name
	public static String KEY_INFORMATION = "information"; // Place area name
	private boolean[] near = new boolean[ARRAYLENGTH];
	private boolean near1 = false;
	private int nearcounter = 0;
	private String n1 = "";
	private String n2 = "";
	private String n3 = "";
	private String n4 = "";
	private boolean[] nearArray = new boolean[ARRAYLENGTH];
	private HashMap<Integer, Place> nearHm = new HashMap<Integer, Place>();

	// ARRAYS are all sized via the constant so that the size can be altered
	// declare a variable array to hold places
	// these variables perform better as arrays and lists because of the way
	// they
	// are accessed for induction
	public static Double[] placesArrayLat = new Double[ARRAYLENGTH];
	public static Double[] placesArrayLng = new Double[ARRAYLENGTH];
	public static Double[] placesArrayLat2 = new Double[ARRAYLENGTH];
	public static Double[] placesArrayLng2 = new Double[ARRAYLENGTH];
	public Double[] p_i_lat = new Double[ARRAYLENGTH];
	public Double[] p_i_lng = new Double[ARRAYLENGTH];
	public static String[] placesName = new String[ARRAYLENGTH];
	public static String[] placesName2 = new String[ARRAYLENGTH];
	public static String nameArr[] = new String[ARRAYLENGTH];
	public static String latArr[] = new String[ARRAYLENGTH];
	public static String lngArr[] = new String[ARRAYLENGTH];
	public static String[] distanceVariable_i = new String[ARRAYLENGTH];
	public static Place place_i[] = new Place[ARRAYLENGTH];
	public Place place_ii[] = new Place[ARRAYLENGTH];
	// name of second place - well reference to it
	public static String[] secondPlaceReference = new String[ARRAYLENGTH];
	public static Double[] distance_i = new Double[ARRAYLENGTH];
	private Double nearMaxDistance = 3.0;
	

	// ListItems data
	ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String, String>>();
	// ListItems data to hold secondary list
	ArrayList<HashMap<String, String>> placesListItems2 = new ArrayList<HashMap<String, String>>();
	// hashmaps and collections to hold names and references of searched places
	@SuppressLint("UseSparseArrays")
	public static HashMap<Integer, ArrayList<String>> arraynames = new HashMap<Integer, ArrayList<String>>();
	@SuppressLint("UseSparseArrays")
	public static HashMap<Integer, ArrayList<String>> arrayreferences = new HashMap<Integer, ArrayList<String>>();
	@SuppressLint("UseSparseArrays")
	public static HashMap<Integer, ArrayList<Double>> arrayplaceslng = new HashMap<Integer, ArrayList<Double>>();
	@SuppressLint("UseSparseArrays")
	public static HashMap<Integer, ArrayList<Double>> arrayplaceslat = new HashMap<Integer, ArrayList<Double>>();
	public static HashMap<Integer, Place> hm = new HashMap<Integer, Place>();
	// placeholder string list variable
	public static List<String> mapstring = new ArrayList<String>();
	public static List<String> types_i = new ArrayList<String>();
	// public static DecimalFormat[] myFormat_i = new
	// DecimalFormat[ARRAYLENGTH];
	public static List<DecimalFormat> myFormat_i = new ArrayList<DecimalFormat>();
	public static HashMap<Integer, Place> hmp = new HashMap<Integer, Place>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		cd = new ConnectionDetector(getApplicationContext());
		
		TextView white = (TextView) findViewById(R.id.name);
		//white.setTextColor(Color.WHITE);

		// Check if Internet present
		isInternetPresent = cd.isConnectingToInternet();
		if (!isInternetPresent) {
			// Internet Connection is not present
			alert.showAlertDialog(SecondActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}

		// creating GPS Class object
		gps = new GPSTracker(this);

		// check if GPS location can get
		if (gps.canGetLocation()) {
			Log.d("Your Location", "latitude:" + gps.getLatitude()
					+ ", longitude: " + gps.getLongitude());
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

	

		// calling background Async task to load Google Places
		// After getting places from Google all the data is shown in listview
		new LoadPlaces().execute();



		/**
		 * ListItem click event On selecting a listitem SinglePlaceActivity is
		 * launched
		 * */
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}

		});
		
		


		lv2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				lvid = position;

				Log.d("position", "The position is:" + position);
				Log.d("position", "The counter is:" + counter);
				counter++;


				Log.d("position", "The position is:" + counter + " key ref is "
						+ KEY_REFERENCE.toString() + " Reference is "
						+ reference);
				counter++;
				
				
				
				referenceBefore = lv2.getItemAtPosition(position).toString();
				String referenceAfter[] = lv2.getItemAtPosition(position).toString().split("\n");
				reference = referenceAfter[0].toString();
				
				
	     		//display in short period of time
	     		Log.d("Your Location", "This is the maput original variable " + reference);

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						Map2Activity.class);
				
				// Sending place reference id to single place activity
				// place reference id used to get "Place full details"
				in.putExtra(KEY_REFERENCE, referenceBefore);
				//in.putExtra(MAP_REFERENCE, maput);		
				// in.putExtra(KEY_REFERENCE, arrayreferences);
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
			pDialog.setMessage(Html
					.fromHtml("<b>Search</b><br/>Loading Places..."));
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
				int u = 0; // should be the number of generated texts

				nearPlaces = googlePlaces.search(gps.getLatitude(),
						gps.getLongitude(), types);

				nearPlaces2 = googlePlaces.search(gps.getLatitude(),
						gps.getLongitude(), types2);

				
					for (int r = 0; r < MainActivity.buttonCounter; r++) {

						// this needs to be made into an array for INDUCTION
						types_i.add(g.getLocationi(r));

						// this needs to be made into an array for INDUCTION
						nearPlaces_i[r] = googlePlaces.search(
								gps.getLatitude(), gps.getLongitude(),
								types_i.get(r));

					}// end for
				
		  // typesCompare(types, types2, types_i);
				

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog and show
		 * the data in UI Always use runOnUiThread(new Runnable()) to update UI
		 * from background thread, otherwise you will get error
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

					int y = 0; // counter variable for array

					// Check for all possible status
					if (status1.equals("OK")) {
						// Successfully got places details
						if (nearPlaces.results != null) {
							// loop through each place
							//#### first set of place results
							for (Place p : nearPlaces.results) {
								HashMap<String, String> map = new HashMap<String, String>();

								// assigns the place to the places array
								// variable
								placesArrayLat[y] = p.geometry.location.lat;
								placesArrayLng[y] = p.geometry.location.lng;
								placesName[y] = p.name;

								// again you may have to use a hash map here
								secondPlaceReference[y] = p.reference;

								// Place reference won't display in listview -
								// it will be hidden
								// Place reference is used to get
								// "place full details"
								map.put(KEY_REFERENCE, p.reference);
								mapstring.add(KEY_REFERENCE.toString());
								mapstring.add(p.reference.toString());
								
								Double distance = DistanceCalculation.distFrom(
										gps.getLatitude(), gps.getLongitude(),
										p.geometry.location.lat,
										p.geometry.location.lng);

								DecimalFormat myFormat = new DecimalFormat(
										"0.0");
								String distanceVariable = myFormat
										.format(distance);

								// Place name
								// --- so one mile needs to be replaced with a
								// variable that tells the
								// distance from one point to another a
								
								near1 = nearInd(distanceVariable, 0);
								n1 = "";
								nearArray[0] = near1;
								
								if(near1 = true){
										 n1 = "*Near*";
										 
								}//end if 
								
								String location1 = p.name + " " + distanceVariable + " miles \n"
										+ "lat:  " + p.geometry.location.lat + " " 
										+ "lng:  " + p.geometry.location.lng + " \n";
										
								
								map.put(KEY_NAME, location1);
								mapstring.add(KEY_REFERENCE);
								mapstring.add(p.reference);
								mapstring.add("lat:  " + p.geometry.location.lat 
												+ "lng:  " + p.geometry.location.lng + " \n"  +distanceVariable.toString());

								// adding HashMap to ArrayList
								placesListItems.add(map);
								Log.d("map1",
										"This is map  of items "
												+ map.toString());

								// increment counter
								y = y + 1;
							}
							// list adapter --deleted code from here

							// Adding data into listview
							// lv.setAdapter(adapter);
							lv.setVisibility(View.INVISIBLE);

							int i = 0; // places counter
							int m = 0; // counter
							try {
								for (int aa = 0; aa < Globals.counterVar; aa++) {
									int placecount = 0;

									// --- THIS NEEDS TO RUN ON A LOOP FOR
									// INDUCTION PURPOSES
									if (nearPlaces_i[aa].results != null) {
										// loop through each place
										for (Place p_i : nearPlaces_i[aa].results) {

											int uniqueKey = aa * 100
													+ placecount;
											arraynames.put(uniqueKey,
													new ArrayList<String>());
											
											arrayplaceslat.put(uniqueKey, new ArrayList<Double>());
											arrayplaceslng.put(uniqueKey, new ArrayList<Double>());
											
											hm.put(uniqueKey, p_i);

											// references skipping next
											// iteration for some reason
											int uniqueKeyRef = aa * 100
													+ placecount;
											arrayreferences.put(uniqueKeyRef,
													new ArrayList<String>());

											// assigns the place to the places
											// array variable
											placesArrayLat2[uniqueKey] = p_i.geometry.location.lat;
											placesArrayLng2[uniqueKey] = p_i.geometry.location.lng;
											placesName2[uniqueKey] = p_i.name;

											String pi = p_i.name.toString();
											String pi_ref = p_i.reference
													.toString();

											// then simply access it with
											arraynames.get(uniqueKey).add(pi);
											arrayplaceslat.get(uniqueKey).add(p_i.geometry.location.lat);
											arrayplaceslng.get(uniqueKey).add(p_i.geometry.location.lng);
											arrayreferences.get(uniqueKeyRef)
													.add(pi_ref);
											place_i[m] = p_i;

											m++;
											// placecount was above unique key
											placecount++;

											Log.d("key",
													"this is the unique key "
															+ uniqueKey);

										}// end for
									}// end if
								}// end for

							}// end try
							catch (Exception e) {
								Log.d("one", "this is an induction error");
							}// end catch

							int n = 0;
							int yy = 0;

							// ### Second set of places results --- loops for induction
							if (nearPlaces2.results != null) {
								// loop through each place
								for (Place p : nearPlaces2.results) {
									if (i < 10) {
									
										HashMap<String, String> map2 = new HashMap<String, String>();

										// Place reference won't display in
										// listview - it will be hidden
										// Place reference is used to get
										// "place full details"
										map2.put(KEY_REFERENCE, p.reference);
										mapstring.add(KEY_REFERENCE.toString());
										mapstring.add(p.reference);
										mapstring.add("lat:  " + p.geometry.location.lat 
												+ "lng:  " + p.geometry.location.lng + " \n" );


										Double distance2 = DistanceCalculation
												.distFrom(
														gps.getLatitude(),
														gps.getLongitude(),
														p.geometry.location.lat,
														p.geometry.location.lng);
										DecimalFormat myFormat = new DecimalFormat(
												"0.0");
										String distanceVariable2 = myFormat
												.format(distance2);

										Double distancefrom = DistanceCalculation
												.distFrom(
														p.geometry.location.lat,
														p.geometry.location.lng,
														placesArrayLat[yy],
														placesArrayLng[yy]);
										DecimalFormat myFormatp = new DecimalFormat(
												"0.0");
										String distanceFr = myFormatp
												.format(distancefrom);
										nameArr[0] = placesName[yy].toString();
										name = placesName[yy].toString();

										try {
											// this distance variable needs to
											// loop????
											distance_i[n] = DistanceCalculation
													.distFrom(
															placesArrayLat[i],
															placesArrayLng[i],
															place_i[n].geometry.location.lat,
															place_i[n].geometry.location.lng);
											Log.d("filter",
													"  place distance calc "
															+ distance_i[n]
																	.toString());

											myFormat_i.add(new DecimalFormat(
													"0.0"));
											distanceVariable_i[n] = myFormat_i
													.get(n).format(
															distance_i[n]);
											

											Log.d("filter",
													"this is the place name"
															+ place_i[n].name
															+ distanceVariable_i[n]
															+ "this app has looped "
															+ n + " times");

										}// end try
										catch (Exception e) {
											Log.d("one", "this is an error");
										}// end catch
										
										
										near1 = nearInd(distanceVariable2, 1);
										nearArray[1] = near1;
										String n1 = "";
										if(near1 = true){
												 n1 = "*Near*";
										}//end if 
										
										near1 = nearInd(distanceFr, 2);
										nearArray[2] = near1;
										 n2 = "";
										if(near1 = true){
												 n2 = "*Near*";
										}//end if 
										

										// --- THIS NEEDS TO RUN ON A LOOP FOR
										// INDUCTION PURPOSES
										String strMapPut = (p.name + " " + distanceVariable2 + " miles \n"
										        + "lat:  " + p.geometry.location.lat + " " 
												+ "lng:  " + p.geometry.location.lng + " \n" 
											
												+ name + " " + distanceFr + " miles \n"
												+ "lat:  " + placesArrayLat[yy].toString() + " "
												+ "lng:  " + placesArrayLng[yy].toString()  + " \n" 
											     );
										
									

										//--just commented this out mapstring.add(strMapPut);

										
										
										
										
										int multiply = 100;
										try {
											// this is different places with the
											// same distance variable
											// --------------------------------------------added
											
											near1 = nearInd(distanceVariable_i[wcounter], 3);
											 n3 = "";
											 nearArray[3] = near1;
											if(near1 = true){
													 n3 = "*Near*";
											}//end if
											

											if (arraynames.get(wcounter) != null) {
												distanceVariable_i[wcounter] = distanceVar(
														n, i, wcounter);
												if (distanceVariable_i[wcounter] != null) {
													strMapPut = strMapPut
															+ arraynames.get( 
																	wcounter)
																	.toString().replaceAll("^\\[|\\]$", "")
															+ " " + distanceVariable_i[wcounter].toString().replaceAll("^\\[|\\]$", "") + " miles \n"
															+ "lat:  " + arrayplaceslat.get(wcounter).toString().replaceAll("^\\[|\\]$", "") + " "
															+ "lng:  " + arrayplaceslng.get(wcounter).toString().replaceAll("^\\[|\\]$", "") + " \n"
															
															;
													
												}// end if
												else {
													strMapPut = strMapPut
															+ arraynames.get(wcounter).toString().replaceAll("^\\[|\\]$", "") + " " 
															+ distanceVariable_i[wcounter].toString().replaceAll("^\\[|\\]$", "") + " miles \n"
															+ "lat:  " + arrayplaceslat.get(wcounter).toString().replaceAll("^\\[|\\]$", "") + " " 
															+ "lng:  " + arrayplaceslng.get(wcounter).toString().replaceAll("^\\[|\\]$", "")  + " \n"
															
															;
															
												}// end else

												Log.d("key",
														"this is the second unique key "
																+ wcounter);
											}// end if
											
											
											//hmp.add(wcounter, Place);
											
											for (int loop = 1; loop < MainActivity.buttonCounter; loop++) {
												int uk =  100;
												uk = uk * loop + hh;
												
												near1 = nearInd(distanceVariable_i[wcounter], 4);
												nearArray[4] = near1;
												 n4 = "";
												if(near1 = true){
														 n4 = "*Near*";
												}//end if
												
													if (arraynames.get(uk) != null) {
														distanceVariable_i[wcounter] = distanceVar(
																n, i, uk);
														if (distanceVariable_i[wcounter] != null) {
															strMapPut +=  arraynames.get(uk).toString().replaceAll("^\\[|\\]$", "")  + " " + distanceVariable_i[wcounter].toString().replaceAll("^\\[|\\]$", "") + " miles \n"
																	+ "lat:  " + arrayplaceslat.get(uk).toString().replaceAll("^\\[|\\]$", "") + " " 
																	+ "lng:  " + arrayplaceslng.get(uk).toString().replaceAll("^\\[|\\]$", "") + " \n"
															        ;
														}// end if
														else {
	
															strMapPut += arraynames.get(uk).toString().replaceAll("^\\[|\\]$", "")   + " " + distanceVariable_i[wcounter].toString().replaceAll("^\\[|\\]$", "") + " miles \n"
																	+ "lat:  " + arrayplaceslat.get(uk).toString().replaceAll("^\\[|\\]$", "")  + " " 
																	+ "lng:  " + arrayplaceslng.get(uk).toString().replaceAll("^\\[|\\]$", "") + " \n"
																   ;
														}// end else
	
														Log.d("key", "this is the third unique key " + uk);
													}// end if
													
											}// end for
												

											
											hh++;	
											wcounter++;
										}// end try
										catch (Exception e) {
											Log.d("one",
													"This is a null variable");
										}// end catch
										
										String nearVar;
										Log.d("near", "" +  near1);
										/*if (near1 == true){
											nearVar = "*Near*";
										}//end if 
										else {
											
											nearVar = "*Not Near*";
											
										}*/
										int countertrue = 0;int counterfalse=0;
										for(int ww = 0; ww < 4; ww++){
										Log.d("nearVar", "the nearArray is " + nearArray[ww] + " counter " + ww);

												countertrue++;

											if(nearArray[ww]==false){
												counterfalse++;
											}//end if 
											
										}//end for
										
									
										Log.d("nearVar", "the nearVar is " + countertrue);
										Log.d("nearVar", "the notnearVar is " + counterfalse);
										
										nearVar = "*Far*";
										
										if (counterfalse * 3 < countertrue){
											nearVar = "*Near*";
											
										}//end if 
										
										
										map2.put(KEY_NAME, strMapPut + nearVar);
										near1 = false;
										
										mapstring.add(KEY_NAME + strMapPut);

										// adding HashMap to ArrayList
										placesListItems2.add(map2);
										Log.d("map2121", "This is map of items 2121 "+ map2.toString());

										n++;
										i = i + 1;
										yy++;
									
									}// end i < 10
									else {

									}// do nothing
								}// end map

								// list adapter
								 adapter2 = new SimpleAdapter(
										SecondActivity.this, placesListItems2,
										R.layout.list_item, new String[] {
												KEY_REFERENCE, KEY_LATTITUDE, KEY_LONGITUDE, KEY_NAME,
												KEY_INFORMATION }, new int[] {
												R.id.reference, R.id.lattitude, R.id.longitude, R.id.name });
								

								lv2.setAdapter(adapter2);

							}
						}
					}

					else if (status1.equals("ZERO_RESULTS")) {
						// Zero results found
						alert.showAlertDialog(
								SecondActivity.this,
								"Near Places",
								"Sorry no places found. Try to change the types of places",
								false);
					} else if (status1.equals("UNKNOWN_ERROR")) {
						alert.showAlertDialog(SecondActivity.this,
								"Places Error", "Sorry unknown error occured.",
								false);
					} else if (status1.equals("OVER_QUERY_LIMIT")) {
						alert.showAlertDialog(
								SecondActivity.this,
								"Places Error",
								"Sorry query limit to google places is reached",
								false);
					} else if (status1.equals("REQUEST_DENIED")) {
						alert.showAlertDialog(SecondActivity.this,
								"Places Error",
								"Sorry error occured. Request is denied", false);
					} else if (status1.equals("INVALID_REQUEST")) {
						alert.showAlertDialog(SecondActivity.this,
								"Places Error",
								"Sorry error occured. Invalid Request", false);
					} else if (status2.equals("ZERO_RESULTS")) {
						// Zero results found
						alert.showAlertDialog(
								SecondActivity.this,
								"Near Places",
								"Sorry no places found. Try to change the types of places",
								false);
					} else if (status2.equals("UNKNOWN_ERROR")) {
						alert.showAlertDialog(SecondActivity.this,
								"Places Error", "Sorry unknown error occured.",
								false);
					} else if (status2.equals("OVER_QUERY_LIMIT")) {
						alert.showAlertDialog(
								SecondActivity.this,
								"Places Error",
								"Sorry query limit to google places is reached",
								false);
					} else if (status2.equals("REQUEST_DENIED")) {
						alert.showAlertDialog(SecondActivity.this,
								"Places Error",
								"Sorry error occured. Request is denied", false);
					} else if (status2.equals("INVALID_REQUEST")) {
						alert.showAlertDialog(SecondActivity.this,
								"Places Error",
								"Sorry error occured. Invalid Request", false);
					}

					else {
						alert.showAlertDialog(SecondActivity.this,
								"Places Error", "Sorry error occured.", false);
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

	private static String distanceVar(int n, int i, int uk) {
		Double[] distance_i = new Double[ARRAYLENGTH];
		DecimalFormat[] myFormat_i = new DecimalFormat[ARRAYLENGTH];
		place_i[n] = hm.get(uk);

		int befkey = uk;
		distanceVariable_i[n] = null;

		if (uk > 100) {
			befkey = uk - 100;
			distance_i[n] = DistanceCalculation.distFrom(
					placesArrayLat2[befkey], placesArrayLng2[befkey],
					place_i[n].geometry.location.lat,
					place_i[n].geometry.location.lng);
			Log.d("filter", "  place distance calc " + distance_i[n].toString());
			myFormat_i[n] = new DecimalFormat("0.0");
			distanceVariable_i[n] = myFormat_i[n].format(distance_i[n]);


			// error - does not get second place reference here because loops
			// through all to get to it
			// secondPlaceReference[nameCount] = place_i[n].reference;
			Log.d("second", "1 secondPlaceReference[nameCount]  " + nameCount);
			nameCount++;
			// Log.d("distance", "This is place i over 100  " + place_i[n]);

		} else {
			// this distance variable needs to loop????
			distance_i[n] = DistanceCalculation.distFrom(placesArrayLat[i],
					placesArrayLng[i], place_i[n].geometry.location.lat,
					place_i[n].geometry.location.lng);
			Log.d("filter", "  place distance calc " + distance_i[n].toString());
			myFormat_i[n] = new DecimalFormat("0.0");
			distanceVariable_i[n] = myFormat_i[n].format(distance_i[n]);
			nameArr[1] = place_i[n].name;

			// --------------------------------------------
			// secondPlaceReference[1] = place_i[n].reference;
			Log.d("second", "2 secondPlaceReference[nameCount]  " + nameCount);
			Log.d("distance", "This is place i under 100  " + place_i[n]);
			Log.d("distance", "This is secondPlaceReference[1]  "
					+ secondPlaceReference[1]);
		}
		return distanceVariable_i[n];
	}
	
	public boolean nearInd(String distanceVariable, int nearCounterIn){
		nearcounter = nearCounterIn;
		Double dv = Double.parseDouble(distanceVariable);
		try{
			if(dv < nearMaxDistance){
				near[nearcounter] = true;
				
			}//end inductive if
			else{
				
				if(nearcounter > nearMaxDistance & dv < nearMaxDistance){
					near[nearcounter] = true;
				}
				else{
					near[nearcounter] = false;
				}
				near[nearcounter] = false;
				
			}//end else
			}catch(Exception e){
				Log.d("exception", e.toString());
			}//end catch
		
		Log.d("nearcounter", "near is " + near[nearcounter] + " and method accessed " + nearcounter + " times." );
		return near[nearcounter];
		
	}//end method
	
	
	

}


