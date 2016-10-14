package com.example.roman.myapplication;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Binder;
import android.os.IBinder;

public class CalcService extends Service {

    public class CalcServiceBinder extends Binder {

        CalcService getService() {
            return CalcService.this;
        }
    }

    private final IBinder calcServiceBinder = new CalcServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return calcServiceBinder;
    }

    // Custom method
    public int diff(int a, int b) {
        return a - b;
    }

    public int sum(int a, int b) {
        return a + b;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        String firstNumb = intent.getStringExtra("first_numb");
        String secondNumb = intent.getStringExtra("second_numb");

        if (firstNumb != null && secondNumb != null) {

            int result = sum(Integer.parseInt(firstNumb), Integer.parseInt(secondNumb));

            PendingIntent pendingIntent = (PendingIntent) intent.getParcelableExtra("p_intent");
            Intent calcResultIntent = new Intent(Intent.ACTION_SENDTO);

            calcResultIntent.putExtra("json_result", "{'first':" + firstNumb + ", 'second': " + secondNumb + ", 'result':  " + Integer.toString(result) + " }");

            try {

                pendingIntent.send(this, 0, calcResultIntent);

            } catch (PendingIntent.CanceledException e) {

                e.printStackTrace();

            } finally {

                stopSelfResult(startId);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }
}