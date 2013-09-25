package com.androidhive.googleplacesandmaps;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.LauncherActivity.ListItem;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class KeywordActivity extends Activity {

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

	// Button
	Button btnShowOnMap;

	// Progress dialog
	ProgressDialog pDialog;

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
	public Double[] placesArrayLat = new Double[1000];
	public Double[] placesArrayLng = new Double[1000];
	public String[] placesName = new String[1000];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.keywords);

		// bring up the keywords
		ListView keywords = (ListView) findViewById(R.id.keywords);

		ArrayList<String> your_array_list = new ArrayList<String>();
		your_array_list.add("accounting");
		your_array_list.add("airport");
		your_array_list.add("amusement_park");
		your_array_list.add("aquarium");
		your_array_list.add("art_gallery");
		your_array_list.add("atm");
		your_array_list.add("bakery");
		your_array_list.add("bank");
		your_array_list.add("bar");
		your_array_list.add("beauty_salon");
		your_array_list.add("bicycle_store");
		your_array_list.add("book_store");
		your_array_list.add("bowling_alley");
		your_array_list.add("bus_station");
		your_array_list.add("cafe");
		your_array_list.add("campground");
		your_array_list.add("car_dealer");
		your_array_list.add("car_rental");
		your_array_list.add("car_repair");
		your_array_list.add("car_wash");
		your_array_list.add("casino");
		your_array_list.add("cemetary");
		your_array_list.add("church");
		your_array_list.add("city_hall");
		your_array_list.add("clothing_store");
		your_array_list.add("convenience_store");
		your_array_list.add("courthouse");
		your_array_list.add("dentist");
		your_array_list.add("department_store");
		your_array_list.add("doctor");
		your_array_list.add("electrician");
		your_array_list.add("electronics_store");
		your_array_list.add("embassy");
		your_array_list.add("establishment");
		your_array_list.add("finance");
		your_array_list.add("fire_station");
		your_array_list.add("florist");
		your_array_list.add("food");
		your_array_list.add("funeral_home");
		your_array_list.add("furniture_store");
		your_array_list.add("gas_station");
		your_array_list.add("general_contractor");
		your_array_list.add("grocery_or_supermarket");
		your_array_list.add("gym");
		your_array_list.add("hair_care");
		your_array_list.add("hardware_store");
		your_array_list.add("health");
		your_array_list.add("hindu_temple");
		your_array_list.add("home_goods_store");
		your_array_list.add("hospital");
		your_array_list.add("insurance_agency");
		your_array_list.add("jewelry_store");
		your_array_list.add("laundry");
		your_array_list.add("lawyer");
		your_array_list.add("library");
		your_array_list.add("liquor_store");
		your_array_list.add("local_government_office");
		your_array_list.add("locksmith");
		your_array_list.add("lodging");
		your_array_list.add("meal_delivery");
		your_array_list.add("meal_takeaway");
		your_array_list.add("mosque");
		your_array_list.add("movie_rental");
		your_array_list.add("movie_theater");
		your_array_list.add("moving_company");
		your_array_list.add("museum");
		your_array_list.add("night_club");
		your_array_list.add("painter");
		your_array_list.add("park");
		your_array_list.add("parking");
		your_array_list.add("pet_store");
		your_array_list.add("pharmacy");
		your_array_list.add("physiotherapist");
		your_array_list.add("place_of_worship");
		your_array_list.add("plumber");
		your_array_list.add("police");
		your_array_list.add("post_office");
		your_array_list.add("real_estate_agency");
		your_array_list.add("restaurant");
		your_array_list.add("roofing_contractor");
		your_array_list.add("rv_park");
		your_array_list.add("school");
		your_array_list.add("shoe_store");
		your_array_list.add("shopping_mall");
		your_array_list.add("spa");
		your_array_list.add("stadium");
		your_array_list.add("storage");
		your_array_list.add("store");
		your_array_list.add("subway_station");
		your_array_list.add("synagogue");
		your_array_list.add("taxi_stand");
		your_array_list.add("train_station");
		your_array_list.add("travel_agency");
		your_array_list.add("university");
		your_array_list.add("veterinary_care");
		your_array_list.add("zoo");

		// This is the array adapter, it takes the context of the activity as a
		// first // parameter, the type of list view as a second parameter and
		// your array as a third parameter
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				KeywordActivity.this, android.R.layout.simple_list_item_1,
				your_array_list);
		keywords.setAdapter(arrayAdapter);

		keywords.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// ---------- want to pass the value of the list item to the
				// textview on the main screen -------------
				// String[] o = new String[1000];
				// o[position] = arrayAdapter.getItem(position);

				// this is another
				String something;
				Globals globs = new Globals();
				globs.setTextKey1("something");

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						MainActivity.class);

				// String easyPuzzle = "test";
				// in.putExtra("epuzzle", easyPuzzle);

				startActivity(in);
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
