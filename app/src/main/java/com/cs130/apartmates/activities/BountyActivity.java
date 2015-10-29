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

        list.add(new BountyTask("Vacuum upstairs and downstairs and all aroundstairs", 10, "Vacuum the living room carpet and empty the filter", Boolean.TRUE));
        list.add(new BountyTask("Clean the Bathroom", 15, "Scrub the shower, clean out hairs from drain, wipe down sink, scrub bathtub.  We will know if you didn't do a thurough job so don't mess it up you bum.", Boolean.FALSE));
        List<BountyTask> list = new Vector<BountyTask>();
        list.add(new BountyTask("Vacuum upstairs and downstairs and all around stairs", 10, "Vacuum the living room carpet and empty the filter", Boolean.TRUE));
        list.add(new BountyTask("Clean the Bathroom", 15, "Scrub the shower, clean out hairs from drain, wipe down sink, scrub bathtub.  We will know if you didn't do a thorough job so don't mess it up you bum.", Boolean.FALSE));

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
        return true;
    }
}
