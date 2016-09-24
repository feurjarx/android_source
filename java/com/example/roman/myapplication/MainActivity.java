package com.example.roman.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public final static String GLOBAL_SPACE = "com.example.roman.myapplication.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            intent.putExtra(GLOBAL_SPACE,
                    "{'first':" + firstNumb
                    + ", 'second': " + secondNumb
                    + ", 'result':  " + Integer.toString(result) + " }"
            );

            startActivityForResult(intent, 1);

        } catch (Exception e) {}
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        String result = data.getStringExtra(GLOBAL_SPACE);

        TextView textView = (TextView) findViewById(R.id.resultFromChildTextView);

        result = textView.getText().toString() + " " + result;
        textView.setText(result);
    }
}
