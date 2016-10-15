package com.example.roman.myapplication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultActivity extends Activity {

    public final static String GLOBAL_SPACE = "com.example.roman.myapplication.MESSAGE";

    /**
     * Binding service
     */
    CalcService calcService;
    /**
     * Unbinding flag
     */
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        final Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.GLOBAL_SPACE);
        try {

            JSONObject jsonData = new JSONObject(message);

            final int sum = Integer.parseInt(jsonData.getString("result"));

            TextView txtView = (TextView) findViewById(R.id.textView1);
            txtView.setText("Sum " + jsonData.getString("first") + " and " + jsonData.getString("second") + " equal is " + jsonData.getString("result"));

            // Lab3: demo Service Binding
            Button returnedBtn = (Button) findViewById(R.id.button2);
            View.OnClickListener handler = new View.OnClickListener() {
                public void onClick(View v) {

                    EditText firstNumbElem = (EditText)findViewById(R.id.editText3);
                    final String firstNumb = firstNumbElem.getText().toString();

                    // Call binding service
                    int result = calcService.diff(sum, Integer.parseInt(firstNumb));

                    intent.putExtra(GLOBAL_SPACE, Integer.toString(result));
                    setResult(RESULT_OK, intent);
                    finish(); // close child activity
                }
            };

            returnedBtn.setOnClickListener(handler);

        } catch (JSONException e) {

            e.printStackTrace();

        }
    }

    /**
     * Callbacks bindService()
     */
    private ServiceConnection sConn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // Get binding service
            CalcService.CalcServiceBinder binder = (CalcService.CalcServiceBinder) service;
            calcService = binder.getService();

            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        // Bind service processing
        Intent intent = new Intent(this, CalcService.class);
        bindService(intent, sConn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(sConn);
            mBound = false;
        }
    }
}
