package com.cs130.apartmates.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cs130.apartmates.R;
import com.cs130.apartmates.adapters.RotTAdapter;
import com.cs130.apartmates.base.ApartmatesHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

public class RotationFragment extends Fragment implements BaseFragment {
    private RecyclerView mRecyclerView;
    private LinearLayout mLinearLayout;
    private RotTAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mRefreshLayout;
    private long mId;
    private long gId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLinearLayout = (LinearLayout) inflater.inflate(R.layout.content_rotation, container, false);
        mRefreshLayout = (SwipeRefreshLayout) mLinearLayout.findViewById(R.id.swipe_to_refresh);
        mRecyclerView = (RecyclerView) mLinearLayout.findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        mId = prefs.getLong("userId", 0);
        gId = prefs.getLong("groupId", 0);

        mAdapter = new RotTAdapter(getContext(), mId);
        mRecyclerView.setAdapter(mAdapter);
        refresh();

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        return mLinearLayout;
    }

    public void refresh() {
        mAdapter.getManager().clear();
        JSONObject resp = ApartmatesHttpClient.sendRequest("/user?userId=" + mId, null, null, "GET");
        if (resp != null && resp.has("group_id")) {
            try {
                JSONObject taskresp =
                        ApartmatesHttpClient.sendRequest("/task/viewbygroup?groupId=" + resp.get("group_id"), null, null, "GET");
                if (taskresp.has("rotation_tasks")) {
                    JSONArray tasklist = taskresp.getJSONArray("rotation_tasks");
                    for (int i = 0; i != tasklist.length(); i++) {
                        JSONObject task = tasklist.getJSONObject(i);

                        mAdapter.getManager().populateTask(task.getLong("task_id"), mId, task.getInt("value"),
                                task.getString("time_limit"), task.getString("deadline"), task.getString("title"), task.getString("description"),
                                task.getString("state"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void addTask(String deadline, String title, int value, String details, String state) {
        mAdapter.getManager().addTask(mId, gId, value, deadline, title, details, state);
        mAdapter.notifyDataSetChanged();
    }
}
