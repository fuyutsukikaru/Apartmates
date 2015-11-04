package com.cs130.apartmates.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cs130.apartmates.R;
import com.cs130.apartmates.activities.RotationTask;

import java.util.List;

/**
 * Created by bchalabian on 10/26/15.
 */
public class RotTAdapter extends RecyclerView.Adapter<RotTAdapter.TaskViewHolder> {
    private static final String TAG = "RotTAdapter";
    private List<RotationTask> rotationTaskList;
    private MenuItem points;

    public RotTAdapter(MenuItem points, List<RotationTask> rottl) {
        rotationTaskList = rottl;
        this.points = points;
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
    public void onBindViewHolder(RotTAdapter.TaskViewHolder taskViewHolder, int position) {
        taskViewHolder.taskName.setText(rotationTaskList.get(position).taskName);
        final Integer val = new Integer(rotationTaskList.get(position).taskValue);
        taskViewHolder.taskValue.setText(val.toString());
        taskViewHolder.taskDescription.setText(rotationTaskList.get(position).description);
        taskViewHolder.action.setText(rotationTaskList.get(position).action);

        if (rotationTaskList.get(position).action == "Claim")
            taskViewHolder.action.setBackgroundResource(R.color.colorButton);
        else
            taskViewHolder.action.setBackgroundResource(R.color.colorButtonNegate);

        final int pos = position;

        taskViewHolder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(points.getTitle().toString());
                points.setTitle(Integer.toString(count + val));
                rotationTaskList.remove(pos);
                notifyItemRemoved(pos);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {
        return rotationTaskList.size();
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
