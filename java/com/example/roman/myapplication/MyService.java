package com.example.roman.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {
    public MyService() {
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw null;
    }

    public int onStartCommand(Intent intent, int flags, int startId){

        Intent broadcastIntent = new Intent(MainActivity.BROADCAST_ACTION);
        broadcastIntent.putExtra("type", intent.getStringExtra("type"));
        sendBroadcast(broadcastIntent);

        stopSelfResult(startId);

        return super.onStartCommand(intent, flags, startId);
    }
}
