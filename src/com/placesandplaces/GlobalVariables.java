package com.placesandplaces;

import android.app.Application;

public class GlobalVariables extends Application {

	public String type1;
	public String type2;
	public String strPlaces1;
	public String strPlaces2;

	private String someVariable, someVariable2;

	public void setStrPlaces1(String someVariable) {
		strPlaces1 = someVariable;
	}

	public String getStrPlaces1() {
		return strPlaces1;
	}

	public void setStrPlaces2(String someVariable2) {
		strPlaces1 = someVariable2;
	}

	public String getStrPlaces2() {
		return strPlaces1;
	}

}
