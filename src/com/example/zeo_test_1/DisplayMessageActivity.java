package com.example.zeo_test_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.myzeo.android.api.data.ZeoDataContract;
import com.myzeo.android.api.data.ZeoDataContract.SleepRecord;

public class DisplayMessageActivity extends Activity {
	private Handler handler = new Handler();
	private SleepContentObserver sleepObserver = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		// Show the Up button in the action bar.
		Intent intent = getIntent();
//		String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		UpdateScreen();
		 // Create the text view
	    
	}
	
	public void UpdateScreen(){
		TextView textView = new TextView(this);
	    textView.setTextSize(40);
	    System.out.println("Updating Screen");
	    textView.setText(getZeoData());
	    System.out.println("Got Zeo Data");
	    // Set the text view as the activity layout
	    setContentView(textView);
	}
	
	class SleepContentObserver extends ContentObserver {
		  public SleepContentObserver( Handler h ) {
			super( h );
		  }

		  public void onChange(boolean selfChange) {
			System.out.println("StringsContentObserver.onChange( "+selfChange+")");
			UpdateScreen();
		  }
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
	    
	    public String getZeoData() {
	    	String[] mSelectionArgs = {""};
	    	Cursor mCursor;
	    	String mSelectionClause;
	    	
	    	String mSortOrder;

	    	String[] mProjection =  
	    		{
	    			SleepRecord.BASE_HYPNOGRAM,
	    		};
	    	
	    	byte[] sleepArray = null;
	    	String latestStage = "122233";
	    	int count = 0;
	    	
	    	// If the word is the empty string, gets everything
	    	
	    	mSelectionClause = SleepRecord.END_REASON + " =1";

		    mSelectionArgs = null;    	    // Constructs a selection clause that matches the word that the user entered
		    mSortOrder = null;
	    	// Does a query against the table and returns a Cursor object
	        System.out.println("query");
	    	mCursor = getContentResolver().query(
	    		SleepRecord.CONTENT_URI,  // The content URI of the words table
	    	    mProjection,                       // The columns to return for each row
	    	    mSelectionClause,                   // Either null, or the word the user entered
	    	    mSelectionArgs,                    // Either empty, or the string the user entered
	    	    mSortOrder);
	        System.out.println("query done");
	        sleepObserver = new SleepContentObserver( handler );
	        getContentResolver().registerContentObserver(SleepRecord.CONTENT_URI, true, sleepObserver);
//	    	int index = mCursor.getColumnIndex(SleepRecord.ZQ_SCORE);
	    	
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
//	    	    	newWord = mCursor.getType(0);

	    	        // Insert code here to process the retrieved word.
	    	       
	    	        
	    	        // end of while loop
	    	    }
	    	}
	        System.out.println("Ready to return");
	        System.out.println(count);
	    	return ""+count;
	    }
	
}
