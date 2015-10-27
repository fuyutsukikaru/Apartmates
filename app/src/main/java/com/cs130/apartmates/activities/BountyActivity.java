package com.cs130.apartmates.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.cs130.apartmates.R;

import java.util.List;
import java.util.Vector;

/**
 * Created by bchalabian on 10/26/15.
 */
public class BountyActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private BTAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;




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

        List<BountyTask> list = new Vector<BountyTask>();
        list.add(new BountyTask("Vacuum", 10, "Vacuum the living room carpet and empty the filter"));
        list.add(new BountyTask("Clean the Bathroom", 15, "Scrub the shower, clean out hairs from drain, wipe down sink, scrub bathtub.  We will know if you didn't do a thurough job so don't mess it up you bum."));
        list.add(new BountyTask("Vacuum", 10, "Vacuum the living room carpet and empty the filter"));
        list.add(new BountyTask("Clean the Bathroom", 15, "Scrub the shower, clean out hairs from drain, wipe down sink"));
        list.add(new BountyTask("Vacuum", 10, "Vacuum the living room carpet and empty the filter"));
        list.add(new BountyTask("Clean the Bathroom", 15, "Scrub the shower, clean out hairs from drain, wipe down sink"));
        list.add(new BountyTask("Vacuum", 10, "Vacuum the living room carpet and empty the filter"));
        list.add(new BountyTask("Clean the Bathroom", 15, "Scrub the shower, clean out hairs from drain, wipe down sink"));
        list.add(new BountyTask("Vacuum", 10, "Vacuum the living room carpet and empty the filter"));
        list.add(new BountyTask("Clean the Bathroom", 15, "Scrub the shower, clean out hairs from drain, wipe down sink"));
        list.add(new BountyTask("Vacuum", 10, "Vacuum the living room carpet and empty the filter"));
        list.add(new BountyTask("Clean the Bathroom", 15, "Scrub the shower, clean out hairs from drain, wipe down sink"));

        mAdapter = new BTAdapter(list);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


}
