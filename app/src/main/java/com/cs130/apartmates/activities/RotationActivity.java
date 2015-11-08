package com.cs130.apartmates.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.cs130.apartmates.R;
import com.cs130.apartmates.adapters.BTAdapter;
import com.cs130.apartmates.adapters.RotTAdapter;
import com.cs130.apartmates.base.tasks.RotationTask;

import java.util.List;
import java.util.Vector;

/**
 * Created by nhadfieldmenell on 11/3/15.
 */
public class RotationActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RotTAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuItem points;
    private long mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tasks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // I copied this stuff from the bounty activity , I assume it works because I am just
        // trying to grab the user's id.
        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        mId = prefs.getLong("userId", 1);

        mAdapter = new RotTAdapter(points, mId);
    }

    public void addTask(View view) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String title = intent.getStringExtra("task_title");
            int value = intent.getIntExtra("task_value", 0);
            String details = intent.getStringExtra("task_details");

            // THIS IS INCORRECT. I DON'T KNOW IF THIS IS HOW ROTATION TASKS WOULD BE ADDED.
            // THIS DEFINITELY NEEDS TO BE CHANGED
            mAdapter.getManager().addTask(mId, title, value, details);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tasks, menu);

        mAdapter = new RotTAdapter(points, mId);
        return true;
    }

}