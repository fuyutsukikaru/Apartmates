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
import com.cs130.apartmates.base.ApartmatesHttpClient;
import com.cs130.apartmates.base.BountyTaskManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Vector;

/**
 * Created by bchalabian on 10/26/15.
 */
public class BountyActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private BTAdapter mAdapter;
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

        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        mId = prefs.getLong("userId", 1);

        mAdapter = new BTAdapter(points, mId);

        //maybe we could just pass in the groupId instead of making another request
        JSONObject resp = ApartmatesHttpClient.sendRequest("/user/info?userId=" + mId, null, null, "GET");
        if (resp.has("group_id")) {
            try {
                long gid = resp.getLong("group_id");
                JSONObject taskresp =
                        ApartmatesHttpClient.sendRequest("/group?groupId=" + resp.get("group_id"), null, null, "GET");
                if (taskresp.has("tasks")) {
                    JSONArray tasklist = taskresp.getJSONArray("tasks");
                    for (int i = 0; i != tasklist.length(); i++) {
                        long taskno = tasklist.getLong(i);
                        JSONObject detailsresp =
                                ApartmatesHttpClient.sendRequest("/task?taskId=" + Long.toString(taskno),
                                        null, null, "GET");
                        if (detailsresp.has("title")) {
                            mAdapter.getManager().populateTask(taskno, mId, detailsresp.getInt("value"),
                                    detailsresp.getString("title"), detailsresp.getString("description"));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mRecyclerView.setAdapter(mAdapter);
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
            mAdapter.getManager().addTask(mId, 2, value, title, details);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tasks, menu);

        mAdapter.setPoints(points);
        return true;
    }

}
