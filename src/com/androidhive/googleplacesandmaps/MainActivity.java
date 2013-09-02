package com.androidhive.googleplacesandmaps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import com.androidhive.googleplacesandmaps.MessageConsumer.OnReceiveMessageHandler;
import com.newrelic.agent.android.NewRelic;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;


public class MainActivity extends Activity {


	// Button
	Button btnFindIt;
	
	// declares an array of editText for dynamic form creation
	public EditText[] et = new EditText[100];
	public String[] stringArray = new String[100];
	
	public static String KEY_INFORMATION = "information"; // Place area name
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";
	public static Globals g = Globals.getInstance();
	ArrayList<String> resultList = null;

	public String strPlaces1 = ""; // Place type
	public String strPlaces2 = ""; // Place type
	private Connection connection;
	private Channel channel;
	private String requestQueueName = "rpc_queue";
	//public static final ByteString emptyString = "";
	private String replyQueueName;
	private QueueingConsumer consumer;
	public static MainActivity ma = new MainActivity();

    public TextView mOutput;
    public TextView mOutput2;
	
	
	 Bitmap bmScreen;
	 View screen;
	 EditText EditTextIn;
	 public static EditText firstPlace;
	 public static EditText secondPlace;
	 private static final String RPC_QUEUE_NAME = "rpc_queue";
	 
	 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RelativeLayout r1 = (RelativeLayout) findViewById (R.id.RelativeLayout1);
		LinearLayout l1 = (LinearLayout) findViewById (R.id.LLTexts);
		
		//replaced activity_main.xml with startup screen.xml
		setContentView(R.layout.startup_screen);
		
		NewRelic.withApplicationToken(
				"AA36f12720d4267840d700de32f8aed7bdd0e2439d"
				).start(this.getApplication());

		// button show on map
		btnFindIt = (Button) findViewById(R.id.btn_find);
		firstPlace = (EditText) findViewById(R.id.txtPlaces1);
	    secondPlace = (EditText) findViewById(R.id.txtPlaces2);
		
		
		ImageView imgAidm = (ImageView) findViewById(R.id.imgAidm);
		/** Button click event for shown on map */
		imgAidm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				RelativeLayout r1 = (RelativeLayout) findViewById (R.id.RelativeLayout1);
				//MessageConsumer mConsumer2;

		        //The output TextView we'll use to display messages
		        mOutput =  (TextView) findViewById(R.id.txtPlaces1);
		        mOutput2 =  (TextView) findViewById(R.id.txtPlaces2);
		        
		        
		        //Create the consumer

		        try {
		        	
		        	 	ConnectionFactory factory = new ConnectionFactory();
		        	    factory.setHost("54.225.112.221");
		        	    connection = factory.newConnection();
		        	    channel = connection.createChannel();

		        	    replyQueueName = channel.queueDeclare().getQueue(); 
		        	    consumer = new QueueingConsumer(channel);
		        	    channel.basicConsume(replyQueueName, true, consumer);
		        	    
		        	    System.out.println(" [x] Requesting fib()");    
		        	    String response = call("30");
		        	    System.out.println(" [.] Got '" + response + "'"); 
		        	    
		        	    
		        
		        	    String[] str = response.split(",");
		        	    String str1 = str[0].toString();
		        	    String[] str2 = str1.split("\\s+");

		        	    String place1 = str2[0].toString();
		        	    String place2 = str2[1].toString();
		        	    
		        	    place1 = place1.replace("\'", "");
		        	    place2 = place2.replace("\'", "");
		        	    
		        	    String arr[] = new String [100];
		        	    
		        	    System.out.println(place1);
		        	    System.out.println(place2);

		        	    //variable that you would use to fill text box
		        	    // response example 
		        	    // 'movie_rental'  (31)'spa'  (12),	'church'  (36),	'school'  (48),	'
		        	    mOutput.setText(place1.toString());
		        	    mOutput2.setText(place2.toString());

		        	    connection.close();
 
						 
				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		        
			}//end on click
		      
		});
		
		

		//-- pull global variable where needed
		/** Button click event for shown on map */
		btnFindIt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

			    //change this
				//firstPlace.setText("shopping_mall");
				//secondPlace.setText("bar");
				
				String loc1 = firstPlace.getText().toString();
				String loc2 = secondPlace.getText().toString();

				g.setLocation1(firstPlace.getText().toString());
				g.setLocation2(secondPlace.getText().toString());
				
				
				try{
				for(int u = 0; u < i; u++){
				
						if (et[u].getText().toString() != null){		
							setTextMethod(et[u].getText().toString(), 0);
						}//end if
					
				}//end for
				}//end try
				catch(Exception e){
					Log.d("one", "This is a set global variable null pointer.");
				}//end catch 
				
				SetLocations();
				
				//create a database entry with the database manager
				DatabaseManager dm = new DatabaseManager(getApplicationContext());
				
				 SQLiteDatabase db = dm.db;
			        try {
			            ContentValues values = new ContentValues();
			            dm.addRow(loc1, loc2);
			 
			        } finally {
			            if (db != null)
			                db.close();
			        }
				

			}
		});
		
		ImageView imgQ1 = (ImageView) findViewById(R.id.imgQ1);
		/** Button click event for shown on map */
		imgQ1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//this is the android way of starting a new activity
	        	Intent i = new Intent(getApplicationContext(),
	        			KeywordActivity.class);
				
				startActivity(i);

			}
		});
		
	
		
		ImageView imgPlus = (ImageView) findViewById(R.id.imgPlus);
		/** Button click event for shown on map */
		imgPlus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				RelativeLayout r1 = (RelativeLayout) findViewById (R.id.RelativeLayout1);
				addTextView(r1);
				g.buttonPushCount++;

			}
		});
		
		ImageView imgReload = (ImageView) findViewById(R.id.imgRR);
		/** Button click event for shown on map */
		imgReload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				g.buttonPushCount = 0;
				
				Intent i = getBaseContext().getPackageManager()
			             .getLaunchIntentForPackage( getBaseContext().getPackageName() );
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				}
		});
	
	}
	
	public String call(String message) throws Exception {     
	    String response = null;
	    String corrId = java.util.UUID.randomUUID().toString();
	    BasicProperties props = new BasicProperties
	                                .Builder()
	                                .correlationId(corrId)
	                                .replyTo(replyQueueName)
	                                .build();

	    channel.basicPublish("", requestQueueName, props, message.getBytes());
	    int timeout_ms = 30000;
	      
	    while (true)
	    {
	         
	       QueueingConsumer.Delivery delivery = consumer.nextDelivery(timeout_ms);
	       if (delivery == null)
	       {
	          if (channel.isOpen() == false)             // Seems to always return true
	          {   
	        	   
	        	  System.out.println("Channel is not open");
	          }
	       }
	       else
	       {
	    	     
	            //... Process message - delivery.getBody()
		        if (delivery.getProperties().getCorrelationId().equals(corrId)) {
		            response = new String(delivery.getBody());
		            break;
		        }
	       }
	    }
	    
	    return response; 
	    //return "response";
	}

	public void close() throws Exception {
	    connection.close();
	}

	int icount1 = 0;
	int textViewCount = 10;
	int i = 0;

	
	public void addTextView(RelativeLayout r1){
		
		
		int width = 100;
		et[i] = new EditText(this);
		et[i].setText("");
		et[i].setGravity(icount1 * 25);
		et[i].setWidth(width);
		et[i].setId(i);
		g.setLocationVari();
		

		// must set dynamic names like this so that they can be reached: txtPlaces1
		// and induction can be started
		
		et[i].setTag("txtPlaces" + i);
		
		LinearLayout l1 = (LinearLayout) findViewById (R.id.LLTexts);
		l1.addView(et[i]);
		
		//increment counter
		i++;
		
		}//end addTextView method

	
	public void setTextMethod(String ivalue, int i ){
		g.setLocationi(ivalue, i);
	}//end setTextMethod

	private void OpenScreenDialog(){
		
	    AlertDialog.Builder screenDialog = new AlertDialog.Builder(this);
	    screenDialog.setTitle("Captured Screen");
	    TextView TextOut = new TextView(MainActivity.this);
	    TextOut.setBackgroundColor(Color.WHITE);
	    TextOut.setText(g.getLocation1() + " and " + g.getLocation2() + " " + g.getLocationi(0));
	   
	    LayoutParams textOutLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    TextOut.setLayoutParams(textOutLayoutParams);
	    
	    RelativeLayout dialogLayout = new RelativeLayout(this);
	    dialogLayout.addView(TextOut);
	    screenDialog.setView(dialogLayout);
	    
	    screenDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	        // do something when the button is clicked
	        public void onClick(DialogInterface arg0, int arg1) {
	        	
	        	//this is the android way of starting a new activity
	        	Intent i = new Intent(getApplicationContext(),
						SecondActivity.class);
				
				startActivity(i);
	        	
	         }
	        });
	    screenDialog.show();
	   }
	
	
	public void SetLocations(){
		//open a pop up screen
		OpenScreenDialog();
	
	}//end setActivity
	
	
	
	
	private ArrayList<String> autocomplete(String input) {
	    ArrayList<String> resultList = null;
	    
	    HttpURLConnection conn = null;
	    StringBuilder jsonResults = new StringBuilder();
	    try {
	        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
	        sb.append("?sensor=false&key=" + GooglePlaces.API_KEY );
	        sb.append("&components=country:us");
	        sb.append("&input=" + URLEncoder.encode(input, "utf8"));
	        
	        URL url = new URL(sb.toString());
	        conn = (HttpURLConnection) url.openConnection();
	        InputStreamReader in = new InputStreamReader(conn.getInputStream());
	        
	        // Load the results into a StringBuilder
	        int read;
	        char[] buff = new char[1024];
	        while ((read = in.read(buff)) != -1) {
	            jsonResults.append(buff, 0, read);
	        }
	    } catch (MalformedURLException e) {
	        Log.e("LOG_TAG", "Error processing Places API URL", e);
	        return resultList;
	    } catch (IOException e) {
	        Log.e("LOG_TAG", "Error connecting to Places API", e);
	        return resultList;
	    } finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }

	    try {
	        // Create a JSON object hierarchy from the results
	        JSONObject jsonObj = new JSONObject(jsonResults.toString());
	        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
	        
	        // Extract the Place descriptions from the results
	        resultList = new ArrayList<String>(predsJsonArray.length());
	        for (int i = 0; i < predsJsonArray.length(); i++) {
	            resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
	        }
	    } catch (JSONException e) {
	        Log.e("LOG_TAG", "Cannot process JSON results", e);
	    }
	    
	    return resultList;
	}
	
	
	

}
