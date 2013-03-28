package com.example.zeo_test_1;
import static com.myzeo.android.api.data.ZeoDataContract.SleepRecord;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.provider.UserDictionary;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;




public class MainActivity extends Activity {
	
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    
    
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("starting main activity");
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
   
    
    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
    	
//    	Intent myAlarm = new Intent(getApplicationContext(),AlarmReceiver.class);
    	//myAlarm.putExtra("project_id",project_id); //Put Extra if neede
        	
    	
    	Intent intent = new Intent(this, DisplayMessageActivity.class);
//    	EditText editText = (EditText) findViewById(R.id.edit_message);
//    	String message = editText.getText().toString();
//    	intent.putExtra(EXTRA_MESSAGE, message);
    	startActivity(intent);
//    	TextView textView = new TextView(this);
//	    textView.setTextSize(40);
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
    
    public int getZeoData() {
    	String[] mSelectionArgs = {""};
    	Cursor mCursor;
    	String mSelectionClause;
    	
    	String mSortOrder;

    	String[] mProjection =  
    		{
    			SleepRecord.ZQ_SCORE
    		};
    	
    	int newWord = 1211111;

    	
    	// If the word is the empty string, gets everything
    	
    	mSelectionClause = null;

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
    	        newWord = mCursor.getInt(0);

    	        // Insert code here to process the retrieved word.
    	       
    	        
    	        // end of while loop
    	    }
    	}
        System.out.println("Ready to return");
    	return newWord;
    	
    }
}
