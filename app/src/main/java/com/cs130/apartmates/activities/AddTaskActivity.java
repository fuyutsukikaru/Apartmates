package com.cs130.apartmates.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cs130.apartmates.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sjeongus on 10/27/15.
 */
public class AddTaskActivity extends AppCompatActivity {

    private Button create;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_bounty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText title = (EditText) findViewById(R.id.title);
        final EditText description = (EditText) findViewById(R.id.description);
        final EditText value = (EditText) findViewById(R.id.value);
        create = (Button) findViewById(R.id.create);
        cancel = (Button) findViewById(R.id.cancel);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleText = title.getText().toString();
                String descriptionText = description.getText().toString();
                String valueText = value.getText().toString();

                if (!titleText.isEmpty() && !descriptionText.isEmpty() && !valueText.isEmpty()) {
                    //new CreateTask().execute(titleText, descriptionText, valueText);

                    Snackbar.make(findViewById(R.id.add_task_fragment), "Sending to server...", Snackbar.LENGTH_LONG);
                    Intent intent = new Intent(AddTaskActivity.this, BountyActivity.class);
                    intent.putExtra("task_title", titleText);
                    intent.putExtra("task_value", Integer.getInteger(valueText));
                    intent.putExtra("task_details", descriptionText);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Snackbar.make(findViewById(R.id.add_task_fragment), "You did not enter all fields.", Snackbar.LENGTH_LONG);
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTaskActivity.this.finish();
            }
        });

    }

    private class CreateTask extends AsyncTask<String, String, Void> {
        private HttpURLConnection conn;
        private URL url;
        private OutputStream out;
        private InputStream in;
        private String userId;

        @Override
        protected void onPreExecute() {
            create.setClickable(false);
            cancel.setClickable(false);
            SharedPreferences prefs = AddTaskActivity.this.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            userId = prefs.getString("user_id", null);
        }

        @Override
        protected Void doInBackground(String... args) {
            try {
                url = new URL("http://apartmates-backend.rhcloud.com/task/create");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", args[1]);

                out = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                System.err.println(jsonObject.toString());
                writer.write(jsonObject.toString());
                writer.flush();
                writer.close();
                out.close();

                conn.connect();

                int status = conn.getResponseCode();

                if (status >= 400) {
                    in = conn.getErrorStream();
                } else {
                    in = conn.getInputStream();
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    result.append(line);
                }
                in.close();
                System.err.println(result.toString());
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
            }
            return null;
        }
    }
}
