package com.placesandplaces;

import java.io.BufferedReader;
import java.io.FileReader;

import android.util.Log;

public class DataRow {

	//needs a count field
	int Count = 0;

	 public static void main(String[] arg) throws Exception {

	  BufferedReader CSVFile = 
	        new BufferedReader(new FileReader("placesQueries.csv"));

	  String dataRow = CSVFile.readLine(); // Read first line.
	  // The while checks to see if the data is null. If 
	  // it is, we've hit the end of the file. If not, 
	  // process the data.

	  while (dataRow != null){
	   String[] dataArray = dataRow.split(",");
	   for (String item:dataArray) { 
		   Log.d("item", item + "\t"); 
	   }
	   
	   dataRow = CSVFile.readLine(); // Read next line of data.
	  }
	  // Close the file once all data has been read.
	  CSVFile.close();


	 } //main()
	
}
