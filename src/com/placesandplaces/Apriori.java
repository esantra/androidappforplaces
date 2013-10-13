package com.placesandplaces;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import android.os.StrictMode;
import android.util.Log;


public class Apriori {
	
	public ArrayList<String> dataIn = new ArrayList<String>();
	public ArrayList<String> LargeItemSets = new ArrayList<String>();
	public ArrayList<String> placesItems = new ArrayList<String>();
	private int counter1 = 0;
	private int counter2 = 0;
	private int counter3 = 0;
	private int passCount=0; //the number of times the algorithm has iterated
	private double support = .1;
	private double confidence = .6; 
	private int ARRAYLENGTH = 2000;
	
	
	
	public void readFromFile(String filePath) {
		 try{
			 
			//strict mode for reading urls
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

			StrictMode.setThreadPolicy(policy); 
				
			URL oracle = new URL("http://ec2-50-17-51-53.compute-1.amazonaws.com/goods.txt");
	        BufferedReader in = new BufferedReader(
	        new InputStreamReader(oracle.openStream()));

	        String inputLine;
	        while ((inputLine = in.readLine()) != null)
	            Log.d("input", inputLine);
	        	
	        	//add the lines of input to the ArrayList
	            dataIn.add(inputLine);

		        in.close();	
			 }catch(Exception e){
				 Log.d("exception", e.toString());
				 
			 }//end catch
	}//end readfromfile
	
	
	private void iterateTrans(){
		
		String candSet[] = new String[ARRAYLENGTH];  //list of candidate sets
		for(int size=0; size < dataIn.size(); size++){
			
			candSet[size] = dataIn.get(size);
			
		}//end for
		
		
		while(dataIn.size() > 0){
			for (int c=0; c< candSet[c].length(); c++){
				
				//for each  candSet[] in dataIn
				for(String candsetdin : dataIn){
					
					String[] splitcs = new String[ARRAYLENGTH];
					splitcs = candsetdin.split(",");
					
					for(int o =0; o < splitcs.length; o++){
						placesItems.add(splitcs[o]);
					
						//if the transaction contains a placeItems place
						//if placesItems(i) is contained in candset
						String s1 = placesItems.get(o);
						String s2 = dataIn.get(o);
						if(s1.contains(s2)){
							counter3++;
							
						
						}//end if
						
						
						
											
						
					}//end o for
					//increment counter 
					counter2++;
				}//for each candidate set in the dataIn
				
			}//end for
			
			
			
		}//end while
		
	}//end iterateTrans
	
	
			
}
