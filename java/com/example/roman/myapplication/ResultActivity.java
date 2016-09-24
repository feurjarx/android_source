package com.example.roman.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultActivity extends Activity {

    public final static String GLOBAL_SPACE = "com.example.roman.myapplication.MESSAGE";

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
            txtView.setText("Sum " + jsonData.getString("first")
                    + " and "
                    + jsonData.getString("second")
                    + " equal is "
                    + jsonData.getString("result")
            );

            Button returnedBtn = (Button) findViewById(R.id.button2);
            View.OnClickListener handler = new View.OnClickListener() {
                public void onClick(View v) {

                    EditText firstNumbElem = (EditText)findViewById(R.id.editText3);
                    String firstNumb = firstNumbElem.getText().toString();

                    int result = sum - Integer.parseInt(firstNumb);

                    intent.putExtra(GLOBAL_SPACE, Integer.toString(result));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            };

            returnedBtn.setOnClickListener(handler);

        } catch (JSONException exp) {}
    }
}
