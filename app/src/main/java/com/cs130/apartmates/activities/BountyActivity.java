package com.cs130.apartmates.activities;

import android.content.Intent;
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

import java.util.List;
import java.util.Vector;

/**
 * Created by bchalabian on 10/26/15.
 */
public class BountyActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private BTAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<BountyTask> list = new Vector<BountyTask>();
    private MenuItem points;
    private static int myPoints;

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

        list.add(new BountyTask("Move my car", 15, "My car is on Roebling and Strathmore. Move it for me before 10am on Thursday so I don't get towed.", Boolean.TRUE));
        list.add(new BountyTask("Pick up bananas", 5, "My smoothies are lacking. I'll appreciate a bunch. BA DUM TSS", Boolean.FALSE));

        mAdapter = new BTAdapter(list);
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
            list.add(new BountyTask(title, value, details, true));
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tasks, menu);
        //points = menu.getItem(R.id.point_count);
        //myPoints = Integer.parseInt(points.getTitle().toString());

        return true;
    }

    public static void putPoints(int value) {
        myPoints += value;
    }

    public void updatePoints(int value) {
        points.setTitle(myPoints);
    }
}