package com.example.roman.myapplication;

import android.app.IntentService;
import android.content.Intent;

import java.util.concurrent.TimeUnit;

public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int duration = intent.getIntExtra("duration", 0);

            try {

                TimeUnit.MILLISECONDS.sleep(duration);

                Intent broadcastIntent = new Intent(MainActivity.BROADCAST_ACTION);
                broadcastIntent.putExtra("type", "intent-service-signal");
                broadcastIntent.putExtra("duration", Integer.toString(duration));
                sendBroadcast(broadcastIntent);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
