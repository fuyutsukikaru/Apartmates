package com.cs130.apartmates.activities;

import android.content.Context;
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
import com.cs130.apartmates.adapters.RotTAdapter;

import java.util.List;
import java.util.Vector;

/**
 * Created by nhadfieldmenell on 11/3/15.
 */
public class RotationActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RotTAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<RotationTask> list = new Vector<RotationTask>();
    private MenuItem points;

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

        list.add(new RotationTask("Vacuum", 20, "Vacuum the living room and dining room.  Make sure to get under the furniture.", 0, Boolean.FALSE));
        list.add(new RotationTask("Unstack Dishwasher", 5, "Please keep lids and bottoms of containers together.  Wipe the bottom of cups.", 1, Boolean.TRUE));
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
            list.add(new RotationTask(title, value, details, 0, true));
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tasks, menu);
        this.points = menu.findItem(R.id.point_count);

        mAdapter = new RotTAdapter(points, list);
        mRecyclerView.setAdapter(mAdapter);
        return true;
    }

}