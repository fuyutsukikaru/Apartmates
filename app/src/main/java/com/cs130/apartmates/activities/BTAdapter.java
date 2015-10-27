package com.cs130.apartmates.activities;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cs130.apartmates.R;

import java.util.List;

/**
 * Created by bchalabian on 10/26/15.
 */
public class BTAdapter extends RecyclerView.Adapter<BTAdapter.TaskViewHolder> {
    private static final String TAG = "BTAdapter";
    private List<BountyTask> bountyTaskList;

    BTAdapter(List<BountyTask> btl) {
        bountyTaskList = btl;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.a_bountycard_layout, viewGroup, false);
        CardView cv = (CardView) v.findViewById(R.id.cv);
        final LinearLayout details = (LinearLayout) v.findViewById(R.id.details);
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (details.getVisibility() == View.GONE) {
                    details.setVisibility(View.VISIBLE);
                } else {
                    details.setVisibility(View.GONE);
                }
            }
        });
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BTAdapter.TaskViewHolder taskViewHolder, int position) {
        taskViewHolder.taskName.setText(bountyTaskList.get(position).taskName);
        Integer val = new Integer(bountyTaskList.get(position).taskValue);
        taskViewHolder.taskValue.setText(val.toString());
        taskViewHolder.taskDescription.setText(bountyTaskList.get(position).description);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {
        return bountyTaskList.size();
    }

    public final static class TaskViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView taskName;
        TextView taskValue;
        TextView taskDescription;


        TaskViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            taskName = (TextView) itemView.findViewById(R.id.task_name);
            taskValue = (TextView) itemView.findViewById(R.id.task_value);
            taskDescription = (TextView) itemView.findViewById(R.id.task_description);
        }
    }
}


