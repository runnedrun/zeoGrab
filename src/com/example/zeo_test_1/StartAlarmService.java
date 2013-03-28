package com.example.zeo_test_1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

public class StartAlarmService extends Service
{
	private final IBinder mBinder = new LocalBinder();
    Alarm alarm = new Alarm();
    public void onCreate()
    {
    	System.out.println("service created!");
        super.onCreate();       
    }

    public class LocalBinder extends Binder {
        StartAlarmService getService() {
            // Return this instance of LocalService so clients can call public methods
            return StartAlarmService.this;
        }
    }
    
    public void onStart(Context context,Intent intent, int startId)
    {
    	System.out.println("service started!");
        alarm.SetAlarm(context);
    }
    
    public void onDestroy(Context context) {
    	System.out.println("service destroyed, cancelling alarm!");
    	alarm.CancelAlarm(context);
    }

    @Override
    public IBinder onBind(Intent intent) 
    {
        return mBinder;
    }
}
