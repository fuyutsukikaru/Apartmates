package com.cs130.apartmates.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cs130.apartmates.R;
import com.cs130.apartmates.activities.MainActivity;
import com.cs130.apartmates.adapters.BTAdapter;
import com.cs130.apartmates.base.ApartmatesHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

public class BountyFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private LinearLayout mLinearLayout;
    private BTAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuItem points;
    private long mId;
    private long gId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        mLinearLayout = (LinearLayout) inflater.inflate(R.layout.content_bounty, container, false);
        mRecyclerView = (RecyclerView) mLinearLayout.findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        mId = prefs.getLong("userId", 1);

        mAdapter = new BTAdapter(this, mId);

        return mLinearLayout;
    }

    public int getPoints() {
        return Integer.parseInt(points.getTitle().toString());
    }

    public void addPoints(int pts) {
        int nPoints = Integer.parseInt(points.getTitle().toString()) + pts;
        points.setTitle(Integer.toString(nPoints));
    }

    private void refresh() {
        System.err.println("REFRESHING");
        mAdapter.getManager().clear();
        JSONObject resp = ApartmatesHttpClient.sendRequest("/user?userId=" + mId, null, null, "GET");
        if (resp != null && resp.has("group_id")) {
            try {
                gId = resp.getLong("group_id");
                JSONObject taskresp =
                        ApartmatesHttpClient.sendRequest("/task/viewbygroup?groupId=" + resp.get("group_id"), null, null, "GET");
                if (taskresp.has("bounty_tasks")) {
                    JSONArray tasklist = taskresp.getJSONArray("bounty_tasks");
                    for (int i = 0; i != tasklist.length(); i++) {
                        JSONObject task = tasklist.getJSONObject(i);

                        mAdapter.getManager().populateTask(task.getLong("id"), task.getLong("client_id"), task.getInt("value"),
                                task.getString("deadline"), task.getString("title"), task.getString("description"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);

        points = menu.findItem(R.id.point_count);

        mRecyclerView.setAdapter(mAdapter);
        refresh();
    }

    public void addTask(String deadline, String title, int value, String details) {
        mAdapter.getManager().addTask(mId, gId, value, deadline, title, details);
        mAdapter.notifyDataSetChanged();
    }
}
