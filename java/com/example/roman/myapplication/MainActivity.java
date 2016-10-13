package com.example.roman.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public final static String GLOBAL_SPACE = "com.example.roman.myapplication.MESSAGE";
    public final static String BROADCAST_ACTION = "com.example.roman.myapplication.BROADCAST";

    private final static String[] actions = {"begin", "middle", "end"};
    private static int counter = 0;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");
            TextView resultFromChildTextView = (TextView)(findViewById(R.id.resultFromChildTextView));

            switch (type) {
                case "begin":
                    resultFromChildTextView.setText("process begin");
                    break;
                case "middle":
                    resultFromChildTextView.setText("process middle");
                    break;
                case "end":
                    resultFromChildTextView.setText("process end");
                    break;
                default:
                    resultFromChildTextView.setText("unknown process");
            }

            try {

                TimeUnit.MILLISECONDS.sleep(1000);
                counter++;

                Intent serviceIntent = new Intent(context, MyService.class);
                serviceIntent.putExtra("type", actions[counter % actions.length]);
                startService(serviceIntent);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button broadcastBtn = (Button) findViewById(R.id.button3);
        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MyService.class);
                intent.putExtra("type", "begin");
                startService(intent);
            }
        };

        broadcastBtn.setOnClickListener(handler);

        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(this.broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
    /**
     *
     * @param view
     */
    public void calcSumResult(View view) {

        EditText firstNumbElem = (EditText)findViewById(R.id.editText);
        String firstNumb = firstNumbElem.getText().toString();

        EditText secondNumbElem = (EditText)findViewById(R.id.editText2);
        String secondNumb = secondNumbElem.getText().toString();

        try {

            int result = Integer.parseInt(firstNumb) + Integer.parseInt(secondNumb);

            // Do something in response to button
            Intent intent = new Intent(this, ResultActivity.class);

            //startService(new Intent(this, MyService.class).putExtra("name", "Roman"));

            intent.putExtra(GLOBAL_SPACE,
                    "{'first':" + firstNumb
                    + ", 'second': " + secondNumb
                    + ", 'result':  " + Integer.toString(result) + " }"
            );

            startActivityForResult(intent, 1);

        } catch (Exception e) {
            Log.d(e.getMessage(), e.getMessage());
        }
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

        String result = intent.getStringExtra(GLOBAL_SPACE);

        TextView textView = (TextView) findViewById(R.id.resultFromChildTextView);

        result = textView.getText().toString() + " " + result;
        textView.setText(result);
    }
}