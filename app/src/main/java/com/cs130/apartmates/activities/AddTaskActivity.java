package com.cs130.apartmates.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by sjeongus on 10/27/15.
 */
public class AddTaskActivity extends AppCompatActivity implements
        CalendarDatePickerDialogFragment.OnDateSetListener {

    private Button create;
    private Button cancel;
    private EditText deadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_bounty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText title = (EditText) findViewById(R.id.title);
        final EditText description = (EditText) findViewById(R.id.description);
        final EditText value = (EditText) findViewById(R.id.value);
        Button picker = (Button) findViewById(R.id.picker);
        picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Calendar now = Calendar.getInstance();
                CalendarDatePickerDialogFragment calendarDatePickerDialogFragment = CalendarDatePickerDialogFragment
                        .newInstance(AddTaskActivity.this, now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                                now.get(Calendar.DAY_OF_MONTH));
                calendarDatePickerDialogFragment.show(fm, null);
            }
        });

        deadline = (EditText) findViewById(R.id.deadline);
        create = (Button) findViewById(R.id.create);
        cancel = (Button) findViewById(R.id.cancel);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleText = title.getText().toString();
                String descriptionText = description.getText().toString();
                String valueText = value.getText().toString();
                String deadlineText = deadline.getText().toString();

                if (!titleText.isEmpty() && !descriptionText.isEmpty() && !valueText.isEmpty()) {
                    Snackbar.make(findViewById(R.id.add_task_fragment), "Sending to server...", Snackbar.LENGTH_LONG).show();
                    Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
                    intent.putExtra("task_title", titleText);
                    intent.putExtra("task_deadline", deadlineText);
                    intent.putExtra("task_value", Integer.parseInt(valueText));
                    intent.putExtra("task_details", descriptionText);

                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Snackbar.make(findViewById(R.id.add_task_fragment), "You did not enter all fields.", Snackbar.LENGTH_LONG).show();
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

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        String day;
        if (dayOfMonth < 10) {
            day = "0" + String.valueOf(dayOfMonth);
        } else {
            day = String.valueOf(dayOfMonth);
        }
        String month;
        if (monthOfYear + 1 < 10) {
            month = "0" + String.valueOf(monthOfYear + 1);
        } else {
            month = String.valueOf(monthOfYear + 1);
        }
        String result = String.valueOf(year) + "-" + month + "-" + day;
        deadline.setText(result);
    }
}
