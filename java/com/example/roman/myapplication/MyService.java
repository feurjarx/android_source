package com.example.roman.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.concurrent.TimeUnit;

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

        if (intent != null) {
            int duration = intent.getIntExtra("duration", 0);

            try {

                TimeUnit.MILLISECONDS.sleep(duration);

                Intent broadcastIntent = new Intent(MainActivity.BROADCAST_ACTION);
                broadcastIntent.putExtra("type", "simple-service-signal");
                broadcastIntent.putExtra("duration", Integer.toString(duration));
                sendBroadcast(broadcastIntent);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        stopSelfResult(startId);

        return super.onStartCommand(intent, flags, startId);
    }
}
