package com.example.roman.myapplication;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public CalcService calcService;
    private boolean bound = false;

    public final static String GLOBAL_SPACE = "com.example.roman.myapplication.MESSAGE";
    public final static String BROADCAST_ACTION = "com.example.roman.myapplication.BROADCAST";

    private TextView resultFromChildTextView;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            switch (type) {
                case "simple-service-signal":
                    resultFromChildTextView.append("\n" + intent.getStringExtra("duration") + " completed.");
                    break;
                case "intent-service-signal":
                    resultFromChildTextView.append("\n" + intent.getStringExtra("duration") + " completed.");
                    break;
                default:
                    resultFromChildTextView.setText("unknown process");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(this.broadcastReceiver, intentFilter);

        resultFromChildTextView = (TextView)(findViewById(R.id.resultFromChildTextView));
        resultFromChildTextView.setMovementMethod(new ScrollingMovementMethod());

        Button sumBtn = (Button) findViewById(R.id.sumButton);
        sumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText firstNumbElem = (EditText)findViewById(R.id.editText);
                String firstNumb = firstNumbElem.getText().toString();

                EditText secondNumbElem = (EditText)findViewById(R.id.editText2);
                String secondNumb = secondNumbElem.getText().toString();

                try {

                    int result = calcService.sum(Integer.parseInt(firstNumb), Integer.parseInt(secondNumb));

                    Intent intent = new Intent(v.getContext(), ResultActivity.class);
                    intent.putExtra(GLOBAL_SPACE, "{'first':" + firstNumb + ", 'second': " + secondNumb + ", 'result':  " + Integer.toString(result) + " }");
                    startActivityForResult(intent, 1);

                } catch (Exception e) {
                    Log.d(e.getMessage(), e.getMessage());
                }
            }
        });

        // Lab3: demo PendingIntent
        Button pendingBtn = (Button) findViewById(R.id.pendingButton);
        pendingBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText firstNumbElem = (EditText)findViewById(R.id.editText);
                EditText secondNumbElem = (EditText)findViewById(R.id.editText2);

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                PendingIntent pIntent = createPendingResult(2, intent, 0);

                Intent calcServiceIntent = new Intent(v.getContext(), CalcService.class);
                calcServiceIntent.putExtra("first_numb", firstNumbElem.getText().toString());
                calcServiceIntent.putExtra("second_numb", secondNumbElem.getText().toString());
                calcServiceIntent.putExtra("p_intent", pIntent);
                calcServiceIntent.putExtra("intent", intent);

                startService(calcServiceIntent);
            }
        });

        // Lab3: demo BroadcastReceiver - run Simple Service
        Button broadcastBtn = (Button) findViewById(R.id.broadcastButton);
        broadcastBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int rnd = (new Random()).nextInt(5000);

                Intent intent = new Intent(v.getContext(), MyService.class);
                intent.putExtra("duration", rnd);
                startService(intent);

                resultFromChildTextView.append("\n" + Integer.toString(rnd) + " started...");
            }
        });

        // Lab3: demo IntentService
        Button intentServiceBtn = (Button) findViewById(R.id.intentServiceButton);
        intentServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rnd = (new Random()).nextInt(5000);

                Intent intent = new Intent(v.getContext(), MyIntentService.class);
                intent.putExtra("duration", rnd);
                startService(intent);

                resultFromChildTextView.append("\n" + Integer.toString(rnd) + " started...");
            }
        });
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (intent == null) {
            return;
        }

        switch (requestCode) {

            case 2:

                String jsonResult = intent.getStringExtra("json_result");
                resultFromChildTextView.append("\nJSON: '" + jsonResult + "'");
                break;

            default:

                String result = intent.getStringExtra(GLOBAL_SPACE);
                resultFromChildTextView.append("\n***\nResult from child Activity: " + result + "\n***");
        }
    }

    // Retrieving service from binder
    private ServiceConnection sConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CalcService.CalcServiceBinder binder = (CalcService.CalcServiceBinder) service;
            calcService = binder.getService();

            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        // Binding this activity with service ...
        Intent intent = new Intent(this, CalcService.class);
        bindService(intent, sConn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (bound) {
            unbindService(sConn);
            bound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}