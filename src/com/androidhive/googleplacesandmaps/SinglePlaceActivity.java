package com.androidhive.googleplacesandmaps;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.api.client.googleapis.auth.oauth.GoogleOAuthDomainWideDelegation.Url;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_place);

		Intent i = getIntent();

		// Place reference id
		String reference = i.getStringExtra(KEY_REFERENCE);

		// Calling a Async Background thread
		new LoadSinglePlaceDetails().execute(reference);
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
					if(placeDetails != null){
						String status = placeDetails.status;

						// check place details status
						// Check for all possible status
						if(status.equals("OK")){
							if (placeDetails.result != null) {
								String name = placeDetails.result.name;
								address = placeDetails.result.formatted_address;
								phone = placeDetails.result.formatted_phone_number;
								rating = placeDetails.result.rating;
							
								String latitude = Double.toString(placeDetails.result.geometry.location.lat);
								String longitude = Double.toString(placeDetails.result.geometry.location.lng);
								//String rating = placeDetails.result.rating;
								String image = placeDetails.result.icon;

								// Displaying all the details in the view
								// single_place.xml
								TextView lbl_name = (TextView) findViewById(R.id.name);
								TextView lbl_address = (TextView) findViewById(R.id.address);
								//ImageView imgPlace = (ImageView) findViewById(R.id.imgPlace);

								Log.d("Place ", name + address + phone + latitude + longitude);

								// Check for null data from google
								// Sometimes place details might missing
								name = name == null ? "Not present" : name; // if name is null display as "Not present"
								address = address == null ? "Not present" : address;
								phone = phone == null ? "Not present" : phone;
								latitude = latitude == null ? "Not present" : latitude;
								longitude = longitude == null ? "Not present" : longitude;
								image = image == null ? "Not present" : image; 
								rating = rating == null ? 0 : rating;



								lbl_name.setText(name);
								lbl_address.setText(address);
								
								lbl_name.setTextColor(Color.parseColor("#000000"));
								lbl_address.setTextColor(Color.parseColor("#000000"));
								
								
								


								//----- check to see if this is an api call and not an image url
								String imageString2 = 
										"https://maps.googleapis.com/maps/api/place/photo?photoreference=CoQBegAAAFg5U0y-iQEtUVMfqw4KpXYe60QwJC-wl59NZlcaxSQZNgAhGrjmUKD2NkXatfQF1QRap-PQCx3kMfsKQCcxtkZqQ&sensor=true&key=AIzaSyCRLa4LQZWNQBcjCYcIVYA45i9i8zfClqc";
								//imgString.setText("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CoQBegAAAFg5U0y-iQEtUVMfqw4KpXYe60QwJC-wl59NZlcaxSQZNgAhGrjmUKD2NkXatfQF1QRap-PQCx3kMfsKQCcxtkZqQ&sensor=true&key=AIzaSyCRLa4LQZWNQBcjCYcIVYA45i9i8zfClqc");

								String imageString1 = "http://maps.googleapis.com/maps/api/streetview?size=150x150&location="+ Double.toString(placeDetails.result.geometry.location.lat) +","+ Double.toString(placeDetails.result.geometry.location.lng) + "&heading=235&sensor=false";
								
	
								URL url;
								InputStream content;
								try {
									url = new URL(imageString1);

									content = (InputStream)url.getContent();


								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}


								// get nearest places
								try {
									nearPlaces = googlePlaces.search(placeDetails.result.geometry.location.lat,
											placeDetails.result.geometry.location.lng, SecondActivity.types);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								
								ImageButton btnNext = (ImageButton) findViewById(R.id.btnNextPlace);
								/** Button click event for shown on map */
								btnNext.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View arg0) {
										String a = SecondActivity.name;

										new LoadSinglePlaceDetails().execute(SecondActivity.secondPlaceReference);
									
									}
								});								
								



								ImageButton btnMap = (ImageButton) findViewById(R.id.btnMap);
								/** Button click event for shown on map */
								btnMap.setOnClickListener(new View.OnClickListener() {

									
									@Override
									public void onClick(View arg0) {

										String map = "geo:0,0?q=" +address;
										Intent callIntent = new Intent(Intent.ACTION_VIEW);
										callIntent.setData(Uri.parse(map));
										startActivity(callIntent);
									}
									
								});

								ImageButton btnPhone = (ImageButton) findViewById(R.id.btnCall);
								/** Button click event for shown on map */
								btnPhone.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View arg0) {

										phone = "tel:" + phone;
										Intent callIntent = new Intent(Intent.ACTION_CALL);
										callIntent.setData(Uri.parse(phone));
										startActivity(callIntent);
									}
								});



								
								ImageButton btnNavigate = (ImageButton) findViewById(R.id.btnNavigate);
								/** Button click event for shown on map */
								btnNavigate.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View arg0) {
									
														
										Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" +placeDetails.result.geometry.location.lat+","+placeDetails.result.geometry.location.lng));
										intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										startActivity(intent);

										
									}
								});
									

							}
						}
						else if(status.equals("ZERO_RESULTS")){
							alert.showAlertDialog(SinglePlaceActivity.this, "Near Places",
									"Sorry no place found.",
									false);
						}
						else if(status.equals("UNKNOWN_ERROR"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry unknown error occured.",
									false);
						}
						else if(status.equals("OVER_QUERY_LIMIT"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry query limit to google places is reached",
									false);
						}
						else if(status.equals("REQUEST_DENIED"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry error occured. Request is denied",
									false);
						}
						else if(status.equals("INVALID_REQUEST"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry error occured. Invalid Request",
									false);
						}
						else
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry error occured.",
									false);
						}
					}else{
						alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
								"Sorry error occured.",
								false);
					}




				}


				public Drawable drawableFromUrl(String url) throws IOException {
					Bitmap x;

					HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
					connection.connect();
					InputStream input = connection.getInputStream();

					x = BitmapFactory.decodeStream(input);
					return new BitmapDrawable(x);
				}
			});

		}

	}

}
