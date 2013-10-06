package com.placesandplaces;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import com.placesandplaces.R;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class MainActivity extends Activity {

	// Button
	Button btnFindIt;
	ImageView imgExit;

	// declares an array of editText for dynamic form creation
	public EditText[] et = new EditText[100];
	public String[] stringArray = new String[100];

	public static String KEY_INFORMATION = "information"; // Place area name
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name
	public static Globals g = Globals.getInstance();
	ArrayList<String> resultList = null;

	public String strPlaces1 = ""; // Place type
	public String strPlaces2 = ""; // Place type
	private Connection connection;
	private Channel channel;
	private String requestQueueName = "rpc_queue";
	// public static final ByteString emptyString = "";
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
	public static AlertDialog.Builder screenDialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// replaced activity_main.xml with startup screen.xml
		setContentView(R.layout.startup_screen);

		/*
		 * NewRelic.withApplicationToken(
		 * "AA36f12720d4267840d700de32f8aed7bdd0e2439d"
		 * ).start(this.getApplication());
		 */

		// button show on map
		btnFindIt = (Button) findViewById(R.id.btn_find);
		firstPlace = (EditText) findViewById(R.id.txtPlaces2);
		secondPlace = (EditText) findViewById(R.id.txtPlaces1);

		firstPlace.setText("shopping_mall");
		secondPlace.setText("bar");
		
		imgExit = (ImageView) findViewById(R.id.Exit);
		imgExit.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	            finish();
	            System.exit(0);
	        }
	    });
		

		ImageView imgAidm = (ImageView) findViewById(R.id.imgAidm);
		/** Button click event for shown on map */
		imgAidm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// The output TextView we'll use to display messages
				mOutput = (TextView) findViewById(R.id.txtPlaces1);
				mOutput2 = (TextView) findViewById(R.id.txtPlaces2);

				// Create the consumer

				try {

					ConnectionFactory factory = new ConnectionFactory();
					factory.setHost("54.225.112.221");
					connection = factory.newConnection();
					channel = connection.createChannel();

					replyQueueName = channel.queueDeclare().getQueue();
					consumer = new QueueingConsumer(channel);
					channel.basicConsume(replyQueueName, true, consumer);

					System.out.println(" [x] Requesting places()");
					String response = call("30");
					System.out.println(" [.] Got '" + response + "'");

					String[] str = response.split(",");
					String place1 = str[0].toString();
					String place2 = str[1].toString();

					place1 = place1.replace("\'", "");
					place2 = place2.replace("\'", "");
					place1 = place1.trim();
					place2 = place2.trim();

					System.out.println(place1);
					System.out.println(place2);

					// variable that you would use to fill text box
					// response example
					// 'movie_rental' (31)'spa' (12), 'church' (36), 'school'
					// (48), '
					mOutput.setText(place1.toString());
					mOutput2.setText(place2.toString());

					connection.close();
					
					/*Apriori ap = new Apriori();
					ap.aprioriMethod();
					mOutput.setText(ap.text1);
					mOutput2.setText(ap.text2);
					*/
					

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}// end on click

		});

		// -- pull global variable where needed
		/** Button click event for shown on map */
		btnFindIt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// change this

				g.setLocation1(firstPlace.getText().toString());
				g.setLocation2(secondPlace.getText().toString());

				try {
					for (int u = 0; u < 4; u++) {

						if (et[u].getText().toString() != null) {
							// changed code from 0 to u
							setTextMethod(et[u].getText().toString(), u);
							Log.d("types", "types u value " + u);
							Log.d("types",
									"types variable to set text strings "
											+ et[u].getText().toString());
						}// end if

					}// end for
				}// end try
				catch (Exception e) {
					Log.d("one", "This is a set global variable null pointer.");
				}// end catch

				SetLocations();

			}
		});

		ImageView imgQ1 = (ImageView) findViewById(R.id.imgQ1);
		/** Button click event for shown on map */
		imgQ1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// this is the android way of starting a new activity
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

				RelativeLayout r1 = (RelativeLayout) findViewById(R.id.RelativeLayout1);
				addTextView(r1, Globals.buttonPushCount);
				Globals.buttonPushCount++;

			}
		});

		ImageView imgReload = (ImageView) findViewById(R.id.imgRR);
		/** Button click event for shown on map */
		imgReload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Globals.buttonPushCount = 0;

				Intent i = getBaseContext().getPackageManager()
						.getLaunchIntentForPackage(
								getBaseContext().getPackageName());
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});

	}

	public String call(String message) throws Exception {
		String response = null;
		String corrId = java.util.UUID.randomUUID().toString();
		BasicProperties props = new BasicProperties.Builder()
				.correlationId(corrId).replyTo(replyQueueName).build();

		channel.basicPublish("", requestQueueName, props, message.getBytes());
		int timeout_ms = 30000;

		while (true) {

			QueueingConsumer.Delivery delivery = consumer
					.nextDelivery(timeout_ms);
			if (delivery == null) {
				if (channel.isOpen() == false) // Seems to always return true
				{

					System.out.println("Channel is not open");
				}
			} else {

				// ... Process message - delivery.getBody()
				if (delivery.getProperties().getCorrelationId().equals(corrId)) {
					response = new String(delivery.getBody());
					break;
				}
			}
		}

		return response;
		// return "response";
	}

	public void close() throws Exception {
		connection.close();
	}

	int icount1 = 0;
	int textViewCount = 10;
	int i = 0;

	public void addTextView(RelativeLayout r1, int buttoncount) {

		int width = 100;
		et[buttoncount] = new EditText(this);
		et[buttoncount].setText("");
		et[buttoncount].setGravity(icount1 * 25);
		et[buttoncount].setWidth(width);
		et[buttoncount].setId(buttoncount);
		g.setLocationVari();

		// must set dynamic names like this so that they can be reached:
		// txtPlaces1
		// and induction can be started

		et[buttoncount].setTag("txtPlaces" + buttoncount);

		LinearLayout l1 = (LinearLayout) findViewById(R.id.LLTexts);
		l1.addView(et[buttoncount]);

		Log.d("types", "types button count " + buttoncount);

		// increment counter
		// i++;

	}// end addTextView method

	public void setTextMethod(String ivalue, int i) {
		g.setLocationi(ivalue, i);
	}// end setTextMethod

	private void OpenScreenDialog() {
		screenDialog = new AlertDialog.Builder(this);
		screenDialog.setTitle("Finding Places");
		TextView TextOut = new TextView(MainActivity.this);
		TextOut.setBackgroundColor(Color.WHITE);

		LayoutParams textOutLayoutParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		TextOut.setLayoutParams(textOutLayoutParams);

		RelativeLayout dialogLayout = new RelativeLayout(this);
		dialogLayout.addView(TextOut);
		screenDialog.setView(dialogLayout);

		screenDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					// do something when the button is clicked
					public void onClick(DialogInterface arg0, int arg1) {

						// this is the android way of starting a new activity
						Intent in = new Intent(getApplicationContext(),
								SecondActivity.class);

						// startActivity(i);
						// in.putExtra(arrayreferences);
						startActivity(in);

					}
				});
		screenDialog.show();
	}

	public void SetLocations() {
		// open a pop up screen
		OpenScreenDialog();

	}// end setActivity

}

//code inspired and built upon: http://www.androidhive.info/2012/08/android-working-with-google-places-and-maps-tutorial/