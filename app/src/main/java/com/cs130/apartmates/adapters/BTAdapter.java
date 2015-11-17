package com.cs130.apartmates.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cs130.apartmates.R;
import com.cs130.apartmates.base.BountyTaskManager;
import com.cs130.apartmates.base.tasks.BountyTask;
import com.cs130.apartmates.fragments.BountyFragment;

/**
 * Created by bchalabian on 10/26/15.
 */
public class BTAdapter extends RecyclerView.Adapter<BTAdapter.TaskViewHolder> {
    private static final String TAG = "BTAdapter";
    private BountyTaskManager bountyTaskManager;
    private long mId;
    private BountyFragment mBf;

    public BTAdapter(BountyFragment bf, long id) {
        bountyTaskManager = new BountyTaskManager();
        mId = id;
        mBf = bf;
    }

    public BountyTaskManager getManager() {
        return bountyTaskManager;
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
    public void onBindViewHolder(final BTAdapter.TaskViewHolder taskViewHolder, int position) {
        BountyTask bt = bountyTaskManager.getTask(position);
        taskViewHolder.taskName.setText(bt.getTitle());
        Integer val = new Integer(bt.getPoints());
        taskViewHolder.taskValue.setText(val.toString());
        taskViewHolder.taskDescription.setText(bt.getDescription());

        final int fPos = position;
        long creatorId = bt.getCreator();
        if (mId == creatorId) {
            taskViewHolder.action.setText("Drop");
            taskViewHolder.action.setBackgroundResource(R.color.colorButtonNegate);
            taskViewHolder.action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bountyTaskManager.dropTask(fPos);
                    notifyDataSetChanged();
                }
            });
        } else {
            taskViewHolder.action.setText("Claim");
            taskViewHolder.action.setBackgroundResource(R.color.colorButton);
            taskViewHolder.action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int val = bountyTaskManager.getTask(fPos).getPoints();
                    mBf.addPoints(val);
                    bountyTaskManager.claimTask(fPos);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return bountyTaskManager.getNumTasks();
    }

    public final static class TaskViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView taskName;
        TextView taskValue;
        TextView taskDescription;
        Button action;

        TaskViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            taskName = (TextView) itemView.findViewById(R.id.task_name);
            taskValue = (TextView) itemView.findViewById(R.id.task_value);
            taskDescription = (TextView) itemView.findViewById(R.id.task_description);
            action = (Button) itemView.findViewById(R.id.button);
        }
    }
}
