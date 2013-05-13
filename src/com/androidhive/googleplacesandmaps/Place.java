package com.androidhive.googleplacesandmaps;

import java.io.Serializable;

import org.json.JSONArray;

import com.google.api.client.util.Key;

/** Implement this class from "Serializable"
* So that you can pass this class Object to another using Intents
* Otherwise you can't pass to another actitivy
* */
public class Place implements Serializable {

	@Key
	public String id;
	
	@Key
	public String name;
	
	@Key
	public String reference;
	
	@Key
	public String icon;
	
	@Key
	public String vicinity;
	
	@Key
	public Geometry geometry;
	
	@Key
	public String formatted_address;
	
	@Key
	public String formatted_phone_number;
	
	@Key
	public JSONArray[] reviews;
	
	@Key
	public String website;
	
	@Key
	public Double rating;
	
	
	/*@Key
	public Aspects aspects;
	*/
	
	@Override
	public String toString() {
		return name + " - " + id + " - " + reference;
	}
	
	public static class Geometry implements Serializable
	{
		@Key
		public Location location;
	}
	
	public static class Location implements Serializable
	{
		@Key
		public double lat;
		
		@Key
		public double lng;
	}
	/*public static class Reviews implements Serializable
	{
		@Key
		public Aspects aspects;

	}
	public static class Aspects implements Serializable
	{
		@Key
		public int rating;
		
		@Key
		public String author_name;
		
		@Key
		public String text;
	}*/
	
}
