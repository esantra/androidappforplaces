package com.placesandplaces;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.placesandplaces.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class SinglePlaceActivity extends Activity {
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	// Google Places
	GooglePlaces googlePlaces;

	// Place Details
	public static PlaceDetails placeDetails;

	// Progress dialog
	ProgressDialog pDialog;

	// Places List
	PlacesList nearPlaces;

	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place

	public String phone;

	public String address;

	public Double rating;
	
	private String latitude;
	
	private String longitude;
	
	private static final String TAG_PHOTOS = "photos";
	private static final String TAG_REFRENCE = "photo_reference";
	
	private String photo1;
	
	// GPS Location
	GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_place);

		Intent i = getIntent();

		// Place reference id
		String reference2 = i.getStringExtra(KEY_REFERENCE);

		// Calling a Async Background thread
		new LoadSinglePlaceDetails().execute(reference2);
	}

	/**
	 * Background Async Task to Load Google places
	 * */
	class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SinglePlaceActivity.this);
			pDialog.setMessage("Loading profile ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Profile JSON
		 * */
		protected String doInBackground(String... args) {
			String reference = args[0];

			// creating Places class object
			googlePlaces = new GooglePlaces();

			// Check if used is connected to Internet
			try {
				placeDetails = googlePlaces.getPlaceDetails(reference);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					if (placeDetails != null) {
						String status = placeDetails.status;

						// check place details status
						// Check for all possible status
						if (status.equals("OK")) {
							if (placeDetails.result != null) {
								String name = placeDetails.result.name;
								address = placeDetails.result.formatted_address;
								phone = placeDetails.result.formatted_phone_number;
								rating = placeDetails.result.rating;

								 latitude = Double
										.toString(placeDetails.result.geometry.location.lat);
								 longitude = Double
										.toString(placeDetails.result.geometry.location.lng);
								// String rating = placeDetails.result.rating;
								 
								 
								
								//Log.d("photos", "Length is " +
								//		Place.toStringphoto());
								
								
								/*if(photo[0]!=null){
								//JSONObject pc;
								photo1 = photo[0].toString();
								ImageView place1 = (ImageView) findViewById(R.id.imgPlace1);
								place1.setImageURI(Uri.parse(photo1));
								}
								else{
									//do nothing
								}*/
								
								// Displaying all the details in the view
								// single_place.xml
								TextView lbl_name = (TextView) findViewById(R.id.name);
								TextView lbl_address = (TextView) findViewById(R.id.address);
								

								// Check for null data from google
								// Sometimes place details might missing
								name = name == null ? "Not present" : name; // if
																			// name
																			// is
																			// null
																			// display
																			// as
																			// "Not present"
								address = address == null ? "Not present"
										: address;
								phone = phone == null ? "Not present" : phone;
								latitude = latitude == null ? "Not present"
										: latitude;
								longitude = longitude == null ? "Not present"
										: longitude;
								//image = image == null ? "Not present" : image;
								//photo = photo[0] == null ? "Not present" : photo;
								rating = rating == null ? 0 : rating;

								lbl_name.setText(name);
								lbl_address.setText(address);

								lbl_name.setTextColor(Color
										.parseColor("#000000"));
								lbl_address.setTextColor(Color
										.parseColor("#000000"));
								
								
								
							

								// get nearest places
								try {
									nearPlaces = googlePlaces
											.search(placeDetails.result.geometry.location.lat,
													placeDetails.result.geometry.location.lng,
													SecondActivity.types);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								final ImageButton btnNext = (ImageButton) findViewById(R.id.btnNextPlace);
								/** Button click event for shown on map */
								btnNext.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View arg0) {

										String a;

										// the first instances are already
										// covered
										try {
											if (Globals.nameCount > 0) {

												if (Globals.nameCount == 0) {
													push2();
												}

												Log.d("LV ",
														"ID is  "
																+ Globals.pushID
																+ " Globals.nameCount is "
																+ Globals.nameCount
																+ " plus "
																+ (Globals.nameCount + Globals.pushID));

												// surround with try catch for
												// when user presses button too
												// many times

												a = SecondActivity.arrayreferences
														.get(Globals.nameCount
																+ Globals.pushID
																- 1).get(0);

												Log.d("LV ",
														"LV "
																+ " name array "
																+ SecondActivity.arrayreferences);

												Log.d("LV ",
														"LV "
																+ " name array "
																+ SecondActivity.arraynames);
												new LoadSinglePlaceDetails()
														.execute(a);
												Globals.nameCount = Globals.nameCount += 100;

											}// end if
											else {

												push2();

											}
										} catch (Exception e) {
											btnNext.setBackgroundColor(Color.RED);
											Log.d("load places error",
													"load places error" + e);
										}

									}
								});

								/*ImageButton btnMap = (ImageButton) findViewById(R.id.btnMap);
								
								btnMap.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View arg0) {
										
										
										mapping();
										
										
										
									}

								});

								ImageButton btnPhone = (ImageButton) findViewById(R.id.btnCall);
							
								btnPhone.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View arg0) {

										phone = "tel:" + phone;
										Intent callIntent = new Intent(
												Intent.ACTION_CALL);
										callIntent.setData(Uri.parse(phone));
										startActivity(callIntent);
									}
								});

								ImageButton btnNavigate = (ImageButton) findViewById(R.id.btnNavigate);
								
								btnNavigate
										.setOnClickListener(new View.OnClickListener() {

											@Override
											public void onClick(View arg0) {

												Intent intent = new Intent(
														Intent.ACTION_VIEW,
														Uri.parse("google.navigation:q="
																+ placeDetails.result.geometry.location.lat
																+ ","
																+ placeDetails.result.geometry.location.lng));
												intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
												startActivity(intent);

											}
										});*/

							}
						} else if (status.equals("ZERO_RESULTS")) {
							alert.showAlertDialog(SinglePlaceActivity.this,
									"Near Places", "Sorry no place found.",
									false);
						} else if (status.equals("UNKNOWN_ERROR")) {
							alert.showAlertDialog(SinglePlaceActivity.this,
									"Places Error",
									"Sorry unknown error occured.", false);
						} else if (status.equals("OVER_QUERY_LIMIT")) {
							alert.showAlertDialog(
									SinglePlaceActivity.this,
									"Places Error",
									"Sorry query limit to google places is reached",
									false);
						} else if (status.equals("REQUEST_DENIED")) {
							alert.showAlertDialog(SinglePlaceActivity.this,
									"Places Error",
									"Sorry error occured. Request is denied",
									false);
						} else if (status.equals("INVALID_REQUEST")) {
							alert.showAlertDialog(SinglePlaceActivity.this,
									"Places Error",
									"Sorry error occured. Invalid Request",
									false);
						} else {
							alert.showAlertDialog(SinglePlaceActivity.this,
									"Places Error", "Sorry error occured.",
									false);
						}
					} else {
						alert.showAlertDialog(SinglePlaceActivity.this,
								"Places Error", "Sorry error occured.", false);
					}

				}
			});

		}

	}

	public boolean onCreateOptionsMenu(Menu menu){
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.spmenu, menu);
		return true;
		
	}
	
	 //NEW
	  @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.call:
	      Toast.makeText(this, "Calling...", Toast.LENGTH_SHORT)
	          .show();
	      	phone = "tel:" + phone;
			Intent callIntent = new Intent(
					Intent.ACTION_CALL);
			callIntent.setData(Uri.parse(phone));
			startActivity(callIntent);
	      break;
	    case R.id.map:
	      Toast.makeText(this, "Mapping...", Toast.LENGTH_SHORT)
	          .show();
	      	mapping();
	      break;
	    case R.id.navig:
	    	Intent intent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("google.navigation:q="
							+ placeDetails.result.geometry.location.lat
							+ ","
							+ placeDetails.result.geometry.location.lng));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
        break;


	    default:
	      break;
	    }

	    return true;
	  }
	
	public void push2() {
		Globals.pushID = SecondActivity.lvid;
		new LoadSinglePlaceDetails()
				.execute(SecondActivity.secondPlaceReference[Globals.nameCount
						+ Globals.pushID]);

		int addint = Globals.nameCount + Globals.pushID;

		Log.d("LV", "one two namecount is " + addint);

		Globals.nameCount++;
	}

	public void mapping(){
		/*private GoogleMap mMap;
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.addMarker(new MarkerOptions()
		        .position(new LatLng(0, 0))
		        .title("Hello world"));*/
		
		String zoomLevel = "10";
		
		 Intent intent = new Intent();
		 intent.setAction(Intent.ACTION_VIEW);
		 String data = String.format("geo:%s,%s", latitude, longitude);
		 if (zoomLevel != null) {
		     data = String.format("%s?z=%s", data, zoomLevel);
		 }
		 intent.setData(Uri.parse(data));
		 startActivity(intent);

		String map = "geo:0,0?q=" + address;
		Intent callIntent = new Intent(
				Intent.ACTION_VIEW);
		callIntent.setData(Uri.parse(map));
		startActivity(callIntent);
		
		
	}
	
	public void getImage(){
		
		 // Array of references of the photos
        PlacePhoto[] photos = PlaceParcel.mPhotos;


		
		 // Creating an array of ImageDownloadTask to download photos
        ImageDownloadTask[] imageDownloadTask = new ImageDownloadTask[photos.length];

        int width = 90;
        int height = 90;

        String url = "https://maps.googleapis.com/maps/api/place/photo?";
        String key = "key=AIzaSyAYlKYeI7SYOWMYmuYSu534-ESCeHldmd8";
        String sensor = "sensor=true";
        String maxWidth="maxwidth=" + width;
        String maxHeight = "maxheight=" + height;
        url = url + "&" + key + "&" + sensor + "&" + maxWidth + "&" + maxHeight;

        // Traversing through all the photoreferences
        for(int i=0;i<photos.length;i++){
            // Creating a task to download i-th photo
            imageDownloadTask[i] = new ImageDownloadTask();

            String photoReference = "photoreference="+photos[i].mPhotoReference;

            // URL for downloading the photo from Google Services
            url = url + "&" + photoReference;

            // Downloading i-th photo from the above url
            imageDownloadTask[i].execute(url);
        }
		
	}//end getimage

	 
	 
}

class ImageDownloadTask extends AsyncTask<String, Integer, Bitmap>{
    Bitmap bitmap = null;
    @Override
    protected Bitmap doInBackground(String... url) {
        try{
            // Starting image download
            bitmap = downloadImage(url[0]);
        }catch(Exception e){
            Log.d("Background Task",e.toString());
        }
        return bitmap;
    }


    
    public static Bitmap downloadImage(String strUrl) throws IOException{
        Bitmap bitmap=null;
        InputStream iStream = null;
        try{
            URL url = new URL(strUrl);
 
            /** Creating an http connection to communcate with url */
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
 
            /** Connecting to url */
            urlConnection.connect();
 
            /** Reading data from url */
            iStream = urlConnection.getInputStream();
 
            /** Creating a bitmap from the stream returned from the url */
            bitmap = BitmapFactory.decodeStream(iStream);
 
        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
        }
        return bitmap;
    }
}

