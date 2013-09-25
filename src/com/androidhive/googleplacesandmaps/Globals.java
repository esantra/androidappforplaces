package com.androidhive.googleplacesandmaps;

import android.util.Log;

public class Globals {
	private static Globals instance;

	// Global variable
	private String location1;
	private String location2;
	private String location3;
	private String locationi;
	public static int counterVar;
	public int counterLocVar = 0;
	public String[] location_i = new String[100];
	private CharSequence textKey1 = null;
	public static int buttonPushCount = 0;
	public static int nameCount = 0;
	public static int pushID = 0;
	public static int push = 0;

	// Restrict the constructor from being instantiated
	public Globals() {
	}

	public void setLocation1(String d) {
		this.location1 = d;
	}

	public String getLocation1() {
		return this.location1;
	}

	public void setLocation2(String d) {
		this.location2 = d;
	}

	public String getLocation2() {
		return this.location2;
	}

	public CharSequence getTextKey1() {
		return this.textKey1;
	}

	public void setTextKey1(CharSequence d) {
		this.textKey1 = d;
	}

	public void setLocationVari() {

		counterLocVar++;
	}

	public int getLocationVari() {

		return counterLocVar;
	}

	public void setLocationi(String d, int i) {

		this.location_i[i] = d;
		counterVar++;
	}

	public String getLocationi(int i) {
		// counter variable
		// counterVar++;

		if (i > -1) {

			try {
				if (this.location_i[i].toString() != null) {
					return this.location_i[i].toString();
				}// end if
				else
					return null;
			} catch (Exception e) {
				Log.d("one",
						"this is a null pointer exception for getLocation global method");
			}

		}// end if
		else
			return null;
		return null;
	}

	public void setCounterVar(int i) {

		this.counterVar = i;
	}

	public int getCounterVar() {

		return counterVar;
	}

	public static synchronized Globals getInstance() {
		if (instance == null) {
			instance = new Globals();
		}
		return instance;
	}
}
