package com.cs130.apartmates.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
public class BountyFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private LinearLayout mLinearLayout;
    private BTAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuItem points;
    private long mId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLinearLayout = (LinearLayout) inflater.inflate(R.layout.content_bounty, container, false);
        mRecyclerView = (RecyclerView) mLinearLayout.findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        mId = prefs.getLong("userId", 1);

        mAdapter = new BTAdapter(points, mId);

        //maybe we could just pass in the groupId instead of making another request
        JSONObject resp = ApartmatesHttpClient.sendRequest("/user?userId=" + mId, null, null, "GET");
        if (resp != null && resp.has("group_id")) {
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

        return mLinearLayout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);

        mAdapter.setPoints(points);
    }

    public void doStuff(String title, int value, String details) {
        mAdapter.getManager().addTask(mId, 2, value, title, details);
        mAdapter.notifyDataSetChanged();
    }
}
