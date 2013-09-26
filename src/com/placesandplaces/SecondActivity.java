package com.placesandplaces;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.placesandplaces.R;

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

	// name of place 2
	public static String name;

	// name of second place - well reference to it
	public static String[] secondPlaceReference = new String[10000];

	// Places ListView
	ListView lv;
	public static ListView lv2;

	// ListItems data
	ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String, String>>();
	// ListItems data to hold secondary list
	ArrayList<HashMap<String, String>> placesListItems2 = new ArrayList<HashMap<String, String>>();

	public static String types;

	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name
	public static String KEY_INFORMATION = "information"; // Place area name
	// declare a variable array to hold places
	public static Double[] placesArrayLat = new Double[1000];
	public static Double[] placesArrayLng = new Double[1000];
	public static Double[] placesArrayLat2 = new Double[1000];
	public static Double[] placesArrayLng2 = new Double[1000];
	public Double[] p_i_lat = new Double[1000];
	public Double[] p_i_lng = new Double[1000];
	public static String[] placesName = new String[1000];
	public static String[] placesName2 = new String[1000];
	public static String[] refArray = new String[1000];
	public String name_i;
	public static String nameArr[] = new String[100];
	public static String[] distanceVariable_i = new String[100];
	public Globals g = Globals.getInstance();
	public static Place place_i[] = new Place[100];
	public Place place_ii[] = new Place[100];
	public static int rr;
	public static int wcounter = 0;
	
	
	public static Double[] distance_i = new Double[100];
	public static DecimalFormat[] myFormat_i = new DecimalFormat[100];
	
	
	//this array holds the location of variable and can be static for induction
	public static String[] types_i = new String[100];
	
	//list arrays to hold names and references of searched places
	public static HashMap<Integer, ArrayList<String>> arraynames = new HashMap<Integer, ArrayList<String>>();
	public static HashMap<Integer, ArrayList<String>> arrayreferences = new HashMap<Integer, ArrayList<String>>();
	
	
	public HashMap<Integer, ArrayList<Place>> arrayplaces = new HashMap<Integer, ArrayList<Place>>();
	public static HashMap<Integer, Place> hm = new HashMap<Integer, Place>();
	public static int nameCount = 2;

	
	//placeholder string list variable
	public static List<String> mapstring = new ArrayList<String>();
	public static int lvid;
	int counter = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		cd = new ConnectionDetector(getApplicationContext());

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
				i.putExtra("user_longitude",
						Double.toString(gps.getLongitude()));

				// passing near places to map activity
				i.putExtra("near_places", nearPlaces);
				// staring activity
				startActivity(i);
			}
		});

		/**
		 * ListItem click event On selecting a listitem SinglePlaceActivity is
		 * launched
		 * */
		lv.setOnItemClickListener(new OnItemClickListener() {

			// this part of code is probably inapplicable to the project version
			// as lv is removed from the class project version

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

				refArray[0] = "";

				// getting values from selected ListItem
				String reference = ((TextView) view
						.findViewById(R.id.reference)).getText().toString();

				Log.d("position", "The position is:" + counter + " key ref is "
						+ KEY_REFERENCE.toString() + " Reference is "
						+ reference);
				counter++;

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

				try {
					for (int r = 0; r < g.getLocationVari(); r++) {

						// this needs to be made into an array for INDUCTION
						types_i[r] = g.getLocationi(r);

						// this needs to be made into an array for INDUCTION
						nearPlaces_i[r] = googlePlaces.search(
								gps.getLatitude(), gps.getLongitude(),
								types_i[r]);

					}// end for
				}// end try
				catch (Exception e) {
					Log.d("one", "this is a null pointer exception");

				}// end catch

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
								map.put(KEY_NAME, p.name + " "
										+ distanceVariable);
								mapstring.add(KEY_REFERENCE);
								mapstring.add(p.reference);
								mapstring.add(distanceVariable.toString());

								// adding HashMap to ArrayList
								placesListItems.add(map);
								Log.d("map",
										"This is map  of items "
												+ map.toString());

								// increment counter
								y = y + 1;
							}
							// list adapter
							ListAdapter adapter = new SimpleAdapter(
									SecondActivity.this, placesListItems,
									R.layout.list_item, new String[] {
											KEY_REFERENCE, KEY_NAME },
									new int[] { R.id.reference, R.id.name });

							// Adding data into listview
							// lv.setAdapter(adapter);
							lv.setVisibility(View.INVISIBLE);

							int i = 0; // places counter
							int m = 0; // counter
							Place place = new Place();

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

									// ----added code
									// --- THIS NEEDS TO RUN ON A LOOP FOR
									// INDUCTION PURPOSES
									if (nearPlaces_i[aa + 1].results != null) {
										// loop through each place
										for (Place p_ii : nearPlaces_i[aa + 1].results) {

											// set Place p_i to a variable every
											// time
											place_ii[rr] = p_ii;
											rr++;

										}// end for
									}// end if

								}// end for

							}// end try
							catch (Exception e) {
								Log.d("one", "this is an induction error");
							}// end catch

							int n = 0;
							int yy = 0;

							// Successfully got places details
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

											myFormat_i[n] = new DecimalFormat(
													"0.0");
											distanceVariable_i[n] = myFormat_i[n]
													.format(distance_i[n]);

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

										// --- THIS NEEDS TO RUN ON A LOOP FOR
										// INDUCTION PURPOSES
										String strMapPut = (p.name + " "
												+ distanceVariable2 + "\n"
												+ name + " " + distanceFr + "\n");

										mapstring.add(strMapPut);

										int uk = wcounter + 100;
										int multiply = 100;
										try {
											// this is different places with the
											// same distance variable
											// --------------------------------------------added

											if (arraynames.get(wcounter) != null) {
												distanceVariable_i[wcounter] = distanceVar(
														n, i, wcounter);
												if (distanceVariable_i[wcounter] != null) {
													strMapPut = strMapPut
															+ arraynames.get(
																	wcounter)
																	.toString()
															+ " "
															+ distanceVariable_i[wcounter]
															+ "\n";
												}// end if
												else {
													strMapPut = strMapPut
															+ arraynames
																	.get(wcounter)
															+ " " + "\n";
												}// end else

												Log.d("key",
														"this is the second unique key "
																+ wcounter);
											}// end if

											for (int loop = 1; loop < g.buttonPushCount + 1; loop++) {
												uk = uk * loop;
												if (arraynames.get(uk) != null) {
													distanceVariable_i[wcounter] = distanceVar(
															n, i, uk);
													if (distanceVariable_i[wcounter] != null) {
														strMapPut = strMapPut
																+ arraynames
																		.get(uk)
																+ " "
																+ distanceVariable_i[wcounter]
																+ "\n";
													}// end if
													else {

														strMapPut = strMapPut
																+ arraynames
																		.get(uk)
																+ " " + "\n";
													}// end else

													Log.d("key",
															"this is the third unique key "
																	+ uk);
												}// end if

											}// end for

											wcounter++;
										}// end try
										catch (Exception e) {
											Log.d("one",
													"This is a null variable");
										}// end catch

										map2.put(KEY_NAME, strMapPut);
										mapstring.add(KEY_NAME);
										mapstring.add(strMapPut);

										// adding HashMap to ArrayList
										placesListItems2.add(map2);
										Log.d("map", "This is map of items 2 "
												+ map2.toString());

										n++;
										i = i + 1;
										yy++;
									}// end for
									else {

									}// do nothing
								}// end map

								// list adapter
								ListAdapter adapter2 = new SimpleAdapter(
										SecondActivity.this, placesListItems2,
										R.layout.list_item, new String[] {
												KEY_REFERENCE, KEY_NAME,
												KEY_INFORMATION }, new int[] {
												R.id.reference, R.id.name });

								// Adding data into listview

								// ---------------------------------------------------------------
								// ------------may be able to manipulate adapter
								// 2 somehow

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

	private static Place[] createDynamicStringArray(Place... items) {
		return items;
	}

	private static String distanceVar(int n, int i, int uk) {
		Double[] distance_i = new Double[100];
		DecimalFormat[] myFormat_i = new DecimalFormat[100];
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
			nameArr[nameCount] = place_i[n].name;

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

}
