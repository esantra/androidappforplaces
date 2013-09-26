package com.placesandplaces;

import java.io.Serializable;
import java.util.List;

import com.google.api.client.util.Key;

/** Implement this class from "Serializable"
* in this way the object can be passed using INTENTS
* */
public class PlacesList implements Serializable {

	@Key
	public String status;

	@Key
	public List<Place> results;

}