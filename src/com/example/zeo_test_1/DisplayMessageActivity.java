package com.example.zeo_test_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zeo_test_1.StartAlarmService.LocalBinder;
import com.myzeo.android.api.data.ZeoDataContract.SleepRecord;

public class DisplayMessageActivity extends Activity {
	private Handler handler = new Handler();
	private Timer autoUpdate;

	boolean mBound = false;

	StartAlarmService mService;
    Alarm alarm = new Alarm();

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//    	wakelock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
//    	wakelock.acquire();
//		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
//    	setContentView(R.layout.activity_display_message);
//    	autoUpdate = new Timer();
//    	  autoUpdate.schedule(new TimerTask() {
//    	   @Override
//    	   public void run() {
//    	    runOnUiThread(new Runnable() {
//    	     public void run() {
		System.out.println("trying to start service");
//		Intent intent = new Intent(this, StartAlarmService.class);
//        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		alarm.SetAlarm(this);
		System.out.println("tried to start service");

//    	     }
//    	    });
//    	   }
//    	  }, 0, 40000); 
		
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		System.out.println("onDestroy called");
		alarm.CancelAlarm(this);
	}
	
	 /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            System.out.println("service bound");

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
		
}
