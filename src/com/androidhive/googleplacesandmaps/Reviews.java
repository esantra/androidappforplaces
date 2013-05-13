package com.androidhive.googleplacesandmaps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class Reviews extends Activity{
	
	//-------------------------------this class does not work - you will have to modify it -------------------
	
	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name
	public static String KEY_INFORMATION = "information"; // Place area name
	private String mPhoneNumber = "";
	private String mWebsite = "";
	private ArrayList<PlaceReview> mReviews = new ArrayList<PlaceReview>();


}//end on class

