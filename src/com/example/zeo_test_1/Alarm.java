package com.example.zeo_test_1;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

import com.myzeo.android.api.data.ZeoDataContract.SleepRecord;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver 
{    
	
	private int updateCounter;
	private int lastState = -1;
	
     @Override
     public void onReceive(Context context, Intent intent) 
     {   
         PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
         PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
         wl.acquire();
         UpdateScreen(context);
         System.out.println("alarm called!");
//         Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example

         wl.release();
     }

 public void SetAlarm(Context context)
 {
     AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
     Intent i = new Intent(context, Alarm.class);
     PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
     am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60, pi); // Millisec * Second * Minute
 }

 public void CancelAlarm(Context context)
 {
     Intent intent = new Intent(context, Alarm.class);
     PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
     AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
     alarmManager.cancel(sender);
 }
 
 public String stageToTemp(String stage){
     Hashtable<String, String> lookup = new Hashtable<String, String>();
     lookup.put("0", "65");
     lookup.put("1", "68");
     lookup.put("2", "69");
     lookup.put("3", "63");
     lookup.put("4", "61");
     
     return (String) lookup.get(stage);  
}

public String stageNumberToString(String stage){
     Hashtable<String, String> lookup = new Hashtable<String, String>();
     lookup.put("0", "Headband_removed");
     lookup.put("1", "Wake");
     lookup.put("2", "Rem");
     lookup.put("3", "Light");
     lookup.put("4", "Deep");
     lookup.put("5", "Waiting for headband");
     
     return (String) lookup.get(stage);  
}

public void UpdateScreen(Context context){
	updateCounter+=1;
//	TextView textView = (TextView) findViewById(R.id.display_state); 
    System.out.println("Updating Screen");
    String data = getZeoData(context);
    System.out.println("Got Zeo Data");
    System.out.println("converting to temperature");
//    String stage = stageNumberToString(data);
//    System.out.println("posting temp setting " + temp);
    if (Integer.parseInt(data)!=lastState){
    	String resp = postSleepData(data,context);
	    if (resp != ""){
	    	lastState = Integer.parseInt(data);
	    }
    }
   
    
    // Set the text view as the activity layout
}

public String postSleepData(String data, Context context){
	ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	String response = "";
	NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	System.out.println("chekcing if wifi is up");
    if (networkInfo != null && networkInfo.isConnected()) {
    	 try {
//              	response = downloadUrl("http://warm-wake.herokuapp.com/sleep?value="+data);
    		 	response = downloadUrl("http://10.41.88.167:3000/change_temp?value="+data);
              	System.out.println(response);
            } catch (IOException e) {
            	e.printStackTrace();	
            	System.out.println("Unable to retrieve web page. URL may be invalid.");
            }
    } else {
    	System.out.println("Faillllll");
    }
    return response;
}


   public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");        
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
    
    private String downloadUrl(String myurl) throws IOException {
        	final String DEBUG_TAG = "dowloadLogging";  
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;
            
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            // Starts the query
            Log.i(DEBUG_TAG, "Starting the query");
            System.out.println("starting query");
            conn.connect();
            System.out.println("finishing query");
            Log.i(DEBUG_TAG, "Finished the query");
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;
            
        // Makes sure that the InputStream is closed after the app is
        // finished using it.
        } finally {
            if (is != null) {
                is.close();
            } 
        }
    }
    
    public String getZeoData(Context context) {
    	String[] mSelectionArgs = {""};
    	Cursor mCursor;
    	String mSelectionClause;
    	
    	String mSortOrder;

    	String[] mProjection =  
    		{
    			SleepRecord.BASE_HYPNOGRAM,
    		};
    	
    	byte[] sleepArray = null;
    	String latestStage = "5";
    	int count = 0;
    	
    	// If the word is the empty string, gets everything
    	
    	mSelectionClause = SleepRecord.END_REASON + " =1";

	    mSelectionArgs = null;    	    // Constructs a selection clause that matches the word that the user entered
	    mSortOrder = null;
    	// Does a query against the table and returns a Cursor object
        System.out.println("query");
    	mCursor = context.getContentResolver().query(
    		SleepRecord.CONTENT_URI,  // The content URI of the words table
    	    mProjection,                       // The columns to return for each row
    	    mSelectionClause,                   // Either null, or the word the user entered
    	    mSelectionArgs,                    // Either empty, or the string the user entered
    	    mSortOrder);
        System.out.println("query done");
//        sleepObserver = new SleepContentObserver( handler );
//        getContentResolver().registerContentObserver(SleepRecord.CONTENT_URI, true, sleepObserver);
//    	int index = mCursor.getColumnIndex(SleepRecord.ZQ_SCORE);
        
    	
    	if (mCursor != null) {
    	    /*
    	     * Moves to the next row in the cursor. Before the first movement in the cursor, the
    	     * "row pointer" is -1, and if you try to retrieve data at that position you will get an
    	     * exception.
    	     */
            System.out.println("getting count");

            System.out.println(mCursor.getCount());

    	    while (mCursor.moveToNext()) {

    	        // Gets the value from the column.
    	        sleepArray = mCursor.getBlob(0);
    	        System.out.println("Got the blob");
    	        for (byte stage : sleepArray) {
    	        	count+=1;
                    latestStage = Byte.toString(stage);
                }
//    	    	newWord = mCursor.getType(0);

    	        // Insert code here to process the retrieved word.
    	       
    	        
    	        // end of while loop
    	    }
    	}
        System.out.println("Ready to return");
        System.out.println(count);
    	return ""+latestStage;
    }

 
 
}