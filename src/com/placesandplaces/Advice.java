package com.placesandplaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;



public class Advice extends Activity {


	  private static HashMap<Integer, ArrayList<String>> places = new HashMap<Integer, ArrayList<String>>();
      private static ArrayList<String> placeId = new ArrayList<String>();
      private static ArrayList<String> placesList = new ArrayList<String>();
      private static String placeValue = "GETPLACEVALUE";
      private static int n = 11; //counter variable for number of places entered
      private static int sp = 10; //counter variable for number of sets of places entered
      private static int frq = 1;
      private static HashMap<String, Integer> count = new HashMap<String, Integer>();
      private static HashMap<String, Integer> multicount = new HashMap<String, Integer>();
      private static ArrayList<String> remove = new ArrayList<String>();
      public static TextView  balloon;
	  
	  
  	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	

		// replaced activity_main.xml with startup screen.xml
		setContentView(R.layout.advices);
		
		 balloon = (TextView) findViewById(R.id.advice);
		 advice();

	    }
		
public void advice(){
		  //entered list = {restaurant, bar, cafe, gym .. 10 items}
		  
		  //Step #1: Determine how many times the item appears in sets
		  
		  String[] placeEntered = {"restaurant", "church", "restaurant", "school", "gym",
				  "bar", "cafe", "laundromat", "vet", "doctor", "lawyer", "university" };
		  
		  for (int x = 0; x < n; x++){
			  //generate a random number for places
			  int r = randInt(0,11);
			  placeId.add(placeEntered[r]);
			  
			 
		  }
		  
		  
		  //the hashmap should look like this now:
		  //{0, {restaurant, bar, cafe, gym .. 10 items}}
		  //{1, {restaurant, bar, cafe, gym .. 10 items}}
		  //{2, {restaurant, bar, cafe, gym .. 10 items}}
		  
		  for (int y = 0; y < sp; y++){
			  places.put(y, placeId);	  
		  }
		  
		  //test the hashmap output
		
		  
		  
		  //get frequent items
		  //count the number of transactions in which each item occurs
		  //if restaurant happens in the place id, then restaurant++
		  //loop through places to get results
		  
		  //loop through each transaction counting the number of restaurants and adding them to each other
		  Iterator it = places.entrySet().iterator(); 
		  while (it.hasNext()) { 
			  Map.Entry entry = (Map.Entry) it.next(); 
			  Integer key = (Integer)entry.getKey(); 
			  ArrayList<String> val = (ArrayList<String>)entry.getValue(); 
			  
			  //add an array list here for all vals
			  placesList.add(val.toString());
			  
			  //asking if a.equals(b).
			  Iterator it2 = val.iterator();
			  while (it2.hasNext()){
				  
				  int aa = 0;
				  Object w = it2.next();
				  ArrayList<String> wList = new ArrayList<String>();
				
				 
				  String plString = placesList.toString();
				  
				  String regex = "\\[|\\]";
				  plString = plString.replaceAll(regex, "");
				  plString = plString.replace(",", "");
				  
				  count = placesCount(plString);
				  
			  }//end while
		  }
		  
		  //step #2 : eliminate items that appear in less than 3 sets
		  //if value < frq, the take value from the hashmap - or find frequent itemsets
		  Iterator ifq = count.entrySet().iterator(); 
		  while (ifq.hasNext()) { 
			  Map.Entry entry = (Map.Entry) ifq.next(); 
			  String key = (String)entry.getKey(); 
			  Integer fqval = (Integer) entry.getValue(); 
			  
			 
			  if(fqval > frq){
				 
			  }//end if 
			  else{
				  //make a record of the keys that need to be removed
				  
				  remove.add(key);
				  
			  }//end else
			  
			  
		  }//end ifq while
		  
		 int counter = 0;
		  //from count remove all that were set to remove because not enough frequency
		  Iterator rm = count.entrySet().iterator(); 
		  while (rm.hasNext()) { 
			  Map.Entry entry = (Map.Entry) rm.next(); 
			  String key = (String)entry.getKey(); 
			  Integer fqval = (Integer)entry.getValue();
		      if (fqval < frq){
		    	  rm.remove();
		      }//end if 
			  
			  counter++;
		  }//end while
		 
		  
		  //remove all non frequent values from the places hashmap
		  
		  
		  //remove infrequent items
		  placesRemove(places, remove);
		  
		  //generate the next itemset
		  
		  //need a method to append the next items to count as double itemsets
		  //this returns the new list appended (item, item, etc.)
		  ArrayList<String> addedList = addonitems();
		  
		  //compare addonitems with places to get frequences 
		  checkitemmatch(addedList, 0);
		  
		  
		  
	  }//end main method
	  
	  
	  
	  private static HashMap placesCount(String input) {
		  
		  	int numcount = 0;
		  	HashMap placeCount = new HashMap();

		    //Puts input string into an array, and split it with " ".
		  	//this one splits on space -- need some other type of marker between sets
		    String[] wordlist = input.split(" ");
		 
		    // Text out (Header)
		    
		    // declare Arraylist for words
		    ArrayList<String> wordEncounter = new ArrayList<String>();
		    ArrayList<Integer> numberEncounter = new ArrayList<Integer>();

		    // Checks number of encounters of words
		    for (int i = 0; i < wordlist.length; i++) {
		        String word = wordlist[i];


		        if (wordEncounter.contains(word)) {
		            // Checks word encounter - return index of word
		            int position = wordEncounter.indexOf(word);
		            Integer number = numberEncounter.get(position);
		            int number_int = number.intValue();
		            number_int++;
		            number = new Integer(number_int);
		            numberEncounter.set(position, number);

		            // Number of encounters - add 1;
		        } else {
		            wordEncounter.add(word);
		            numberEncounter.add(new Integer(1));
		        }

		    }

		    // Text out (the list of words)
		    for (int i = 0; i < wordEncounter.size(); i++) {
		        numcount = numberEncounter.get(i);
		        placeCount.put(wordEncounter.get(i), numcount);
		    }
		    
		    return placeCount;

		    
		  }//end placescount method/function
	  
	  
	  
	  private static HashMap<String, Integer> placesCountAdded(ArrayList<String> addedList,HashMap<Integer, ArrayList<String>> places) {
		  
		  
		  Iterator it = places.entrySet().iterator(); 
		  while (it.hasNext()) { 
			  Map.Entry entry = (Map.Entry) it.next(); 
			  Integer key = (Integer)entry.getKey(); 
			  ArrayList<String> val = (ArrayList<String>)entry.getValue(); 
			  
		 
		  
		  	int numcount = 0;
		  	HashMap placeCount = new HashMap();
		  	String[] vallist = null;
		  	String[] subwordlist =null;
		  	String[] words = new String[100];
		  	String[] plist = null;
		  	int[] matches = new int[100];
		  	
		  	//items will be like place1, place2, place3
		  	ArrayList<String> addedListNP = new ArrayList<String>();
		  	//divide addedlistnp into sublist of items like
		  	//place1
		  	//place2
		  	//place3
		  	ArrayList<String> addedListSP = new ArrayList<String>();
		  	
		  	//addedlist comes in this form (word1, word2), (word1, word3)
		  	//and should incorporate infinites such as (word, word2, word3), (word, word4, word5)
		  	//added list should be split into things between commas
		  	
		  	for(int yy = 0; yy < addedList.size(); yy++){
		  		addedListNP.add(addedList.get(yy).replaceAll("[()]",""));
		  		
		  	}//end for    
		  	
		  	for (String s : addedListNP){
		  		subwordlist = s.split(",");
		  		for(int i=0; i< subwordlist.length; i++){
		  			//NOT SURE THIS IS THE RIGHT VALUE - CHECK IT OUT
		  		
		  		String value = val.toString().replaceAll("\\[", "").replaceAll("\\]","");
		  			
		  		
		  		//add all strings in value to an array of strings
		  		vallist = value.toString().split(",");
		  		
		  		int counter = 0;
		  		
		  		for(int un=0; un< vallist.length; un++){
		  			
		  			if(subwordlist[i].equals(vallist[un])){
		  				
		  				//add one to the match array
		  				matches[un]=1;
		  				counter++;
		  				
		  			}else{
		  				
		  				//add 0 to the match array	
		  				matches[un]=0;
		  				
		  			}//end else
		  		}
		  		
		  		int count =0;
		  		
		  		for(int mat = 0; mat< matches.length; mat++){
		  			count = count + matches[mat];
		  		}//end for
		  		
		  		if(counter > 1){
		  			}
		  		
		  		
		  		String values =addedListNP.toString();
		  		multicount.put("(" + s + ")", counter);
		  		
		  		
		  		
		  		//if subwordlist item is in key 1 of the places hashmap report match
		  		//if(subwordlist[i]){
		  			
		  		//}//end if
		  	
		  		}//endsubwordlist for
		  	}//end for each
		  //need to return a value = look into it //modify count to be new count
		  	
		  }//end hash iterator while
		  return multicount;
		    
		  }//end placescount method/function
	  
	  private static HashMap placesRemove(HashMap<Integer, ArrayList<String>> places, ArrayList<String> remove) {

		  Iterator place = count.entrySet().iterator(); 
		  while (place.hasNext()) { 
			  Map.Entry entry = (Map.Entry) place.next(); 
			  String key = (String)entry.getKey(); 
			  Integer fqval = (Integer)entry.getValue();
			  
			  //loop through all elements in remove in order to match with elements in places
			  for (String s : remove){
				  if(key.equals(s)){
					  place.remove();
				  }//end if 
				  
			  }//end remove for each
			 
		  }//end while
		  Log.d("advices", "Pruned itemset is " + count.toString());
		  
		  //giving advice section
		  
		  //a lot of people that go here, also go there
/*put advice about the places that correlate. The user can enter one place and find other places
 * that he or she may be interested in going because they are related
 * for instance (gym, restaurant) is a frequent pair
 * user types in advice.. go to gym and restaurant today!
 * or user types in gym and is advised to go the restaurant
 * or even better, user types in restaurant and is advised to go the gym
 * if user type in restaurant button, advice button will turn red or green
 * user can press on it and it tells user other areas to visit like gym or laundromat
 * may list near locations
 */
	   giveadvice(count.toString());
		  
		  return null;
	  }//places remove end
	  
	  public static int randInt(int min, int max) {

		    // Usually this can be a field rather than a method variable
		    Random rand = new Random();

		    // nextInt is normally exclusive of the top value,
		    // so add 1 to make it inclusive
		    int randomNum = rand.nextInt((max - min) + 1) + min;

		    return randomNum;
		}
	  
	  //may need to add an integer counter here to determine how many times
	  //this method needs to make pairs/etc
	  public static ArrayList<String> addonitems(){
		  //this needs to recombine to make sets of two 
		  //then sets of three etc.
		  
		  //for every entry in count, iterate through the list adding the next item
		  ArrayList<String> newlist = new ArrayList<String>();
		//for every entry in count, iterate through the list adding the next item
		  ArrayList<String> newlistappend = new ArrayList<String>();
		 
		  //create a string list from the count hashmap
		  Iterator place = count.entrySet().iterator(); 
		  while (place.hasNext()) { 
			  Map.Entry entry = (Map.Entry) place.next(); 
			  String key = (String)entry.getKey(); 
			  Integer fqval = (Integer)entry.getValue();
			  newlist.add(key);
		  }//end while
		  
		  //loop through all elements in remove in order to match with elements in places
		  for (String s : newlist){
			  
			  for (String ss : newlist){
				  
				if(ss.equals(s)){
				   //do nothing
					
				}else
				{
					ss = "(" + s +  ", " + ss + ")";
					newlistappend.add(ss);
				}//end else
				  
			  }//end remove for each
			  
		  }//end remove for each
		  
		  //add every other item on the list to each item in the newlist
		  
		  return newlistappend;
		 	 
	}//end 
	  
	  
	 private static void checkitemmatch(ArrayList<String> addedList, int n){
		 ArrayList<String> remove2 = new  ArrayList<String>();

		 
		 //loop through each transaction counting the number of restaurants and adding them to each other
		  Iterator it = places.entrySet().iterator(); 
		 
		  
		  
		  while (it.hasNext()) { 
			  Map.Entry entry = (Map.Entry) it.next(); 
			  Integer key = (Integer)entry.getKey(); 
			  ArrayList<String> val = (ArrayList<String>)entry.getValue(); 
			  
			  //add an array list here for all vals
			  placesList.add(val.toString());
			  
			  //asking if a.equals(b).
			  Iterator it2 = val.iterator();
			  while (it2.hasNext()){
				  int aa = 0;
				  Object w = it2.next();
				  ArrayList<String> wList = new ArrayList<String>();
				
				  
				  String plString = placesList.toString();
				  
				  String regex = "\\[|\\]";
				  plString = plString.replaceAll(regex, "");
				  plString = plString.replace(",", "");
				  
				  
				  count = placesCountAdded(addedList, places);
				  
				  
				  
				  //REMOVE THE ONES THAT DID NOT MAKE COUNT
				  remove2 = new ArrayList<String>();
				   
				   
				  Iterator ifq = count.entrySet().iterator(); 
				  while (ifq.hasNext()) { 
					  Map.Entry entry1 = (Map.Entry) ifq.next(); 
					  String key1 = (String)entry1.getKey(); 
					  Integer fqval = (Integer) entry1.getValue(); 
					  
					 
					  if(fqval > frq){
						  
					  }//end if 
					  else{
						  //make a record of the keys that need to be removed
						  
						  remove2.add(key1);
						  
					  }//end else
				 
				  
				  //remove all non frequent values from the places hashmap
				  }//end while
				  
				  
				  
			  }//end while
			  
			   placesRemove(places, remove2);
		  }
	 }//end checkitemmatch method
	 
	 public static String[] trimword(String word){
		
		 //fix to be dynamic lengths
		 String[] input = new String[200];
		 String[] pl = new String[10];
		 
		 
		 try{
		 //split on comma
		 input[0] = word;
		 
		//take input remove parenthesis
		 input[0] = input[0].replace(")", "");
		 input[0] = input[0].replace("(", "");
		 
		 pl = input[0].split(",");
		 
		 for(int z = 0; z < pl.length; z++){

		 }//end for
		 }catch(Exception e){
			 Log.d("Exception", e.toString());
			 
		 }
		 
		 //return list of words to check for matches
		 return pl;
		 
	 }//end trimword
	 
	 private static void giveadvice(String list){
		 Log.d("advices","Entered advices function" );
		 
		 String pls[] = null;
		 String pllist = list;
		 // pllist = list.replace("(", "");
		 //pllist = pllist.replace(")", "");
		 pllist = pllist.replace("{", "");
		 pllist = pllist.replace("}", "");
		 pllist = pllist.replace("=", " popularity points: ");
		 pllist = pllist.concat("---");
		 
		 pls = pllist.split("---");
		 
		 
		 //user input
		 String input = "restaurant";
		 StringBuilder text = new StringBuilder();
		 text.append("Other popular areas include: ") ;
		 if( contains( list, input ) ) {
			 // Log.d("advices",  "Found " + input + " within " + list + "." );
			  
			  for(int u = 0; u < pls.length; u++){
				  
			
			  
			  
			  text.append( pls[u].toString()+ " \n");
			  text.append(System.getProperty("line.separator"));
			  
			  }//end for 
			  
			  balloon.setText(text);
		 	}
		 
		 
		 
	 }//end giveadvice method
	 
	 private static boolean contains( String haystack, String needle ) {
		  haystack = haystack == null ? "" : haystack;
		  needle = needle == null ? "" : needle;

		  // Works, but is not the best.
		  //return haystack.toLowerCase().indexOf( needle.toLowerCase() ) > -1

		  return haystack.toLowerCase().contains( needle.toLowerCase() );
		}
		 	 

}//end Apriori_Alg class