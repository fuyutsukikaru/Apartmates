package com.cs130.apartmates.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cs130.apartmates.R;
import com.cs130.apartmates.base.ApartmatesHttpClient;
import com.cs130.apartmates.base.RotationTaskManager;
import com.cs130.apartmates.base.tasks.RotationTask;
import com.cs130.apartmates.base.taskstates.ActiveTaskState;
import com.cs130.apartmates.base.taskstates.PenaltyTaskState;
import com.cs130.apartmates.base.taskstates.PendingTaskState;
import com.cs130.apartmates.base.taskstates.TaskState;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

/**
 * Created by bchalabian on 10/26/15.
 */
public class RotTAdapter extends RecyclerView.Adapter<RotTAdapter.TaskViewHolder> {
    private static final String TAG = "RotTAdapter";
    private long mId;
    private RotationTaskManager rotationTaskManager;
    private Context context;

    public RotTAdapter(Context context, long id) {
        rotationTaskManager = new RotationTaskManager();
        mId = id;
        this.context = context;
    }

    public RotationTaskManager getManager() { return rotationTaskManager; }

    public String getProfilePic(long id) {
        JSONObject resp = ApartmatesHttpClient.sendRequest("/user?userId=" + id, null, null, "GET");
        if (resp != null && resp.has("picture_url")) {
            try {
                return resp.getString("picture_url");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rotationcard_layout, viewGroup, false);
        CardView cv = (CardView) v.findViewById(R.id.rcv);
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


    // The rotation task class needs to be edited so the state can be gotten and the color of
    // the task can be shown. (The task color depends on if it is activated and whether or not
    // it is the user's task.
    @Override
    public void onBindViewHolder(RotTAdapter.TaskViewHolder taskViewHolder, int position) {
        final int pos = position;
        RotationTask rt = rotationTaskManager.getTask(position);
        taskViewHolder.taskName.setText(rt.getTitle());
        final Integer val = new Integer(rt.getPoints());
        long id = rt.getAssignee();
        String url = getProfilePic(id);
        Picasso.with(context).load(url).into(taskViewHolder.pic);
        Button button = taskViewHolder.action;
        taskViewHolder.taskValue.setText(val.toString());
        taskViewHolder.taskDescription.setText(rt.getDescription());
        taskViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotationTaskManager.dropTask(mId, rotationTaskManager.getTask(pos).getId(), pos);
                notifyItemRemoved(pos);
            }
        });

        TaskState curState = rt.getCurrentState();
        final long tid = rt.getId();

        if (rt.getAssignee() == mId) { //user is assigned to this task
            if (curState instanceof PendingTaskState) { //task is waiting for someone to say it needs to be done
                taskViewHolder.action.setText("Activate");
                taskViewHolder.action.setBackgroundResource(R.color.colorButtonClicked);
                //TODO: some kind of color scheme?
            } else if (curState instanceof ActiveTaskState) { //task is active; timer is ticking
                taskViewHolder.action.setText("Done");
                taskViewHolder.action.setBackgroundResource(R.color.colorButton);
                //TODO: color? add some kind of timer interface too?
            } else if (curState instanceof PenaltyTaskState) { //task is in penalty mode
                taskViewHolder.action.setText("Claim");
                taskViewHolder.action.setBackgroundResource(R.color.colorButton);
                //TODO: color?
            }

        } else { //user is NOT assigned to this task
            if (curState instanceof PendingTaskState) { //task is waiting for someone to say it needs to be done
                taskViewHolder.action.setText("Activate");
                taskViewHolder.action.setBackgroundResource(R.color.colorButtonClicked);
                //TODO: color?
            } else if (curState instanceof ActiveTaskState) { //task is active; timer is ticking
                taskViewHolder.action.setText("Active");
                button.setEnabled(false);
                taskViewHolder.action.setBackgroundResource(R.color.colorButtonGrey);
                //TODO: color?
            } else if (curState instanceof PenaltyTaskState) { //task is in penalty mode
                taskViewHolder.action.setText("Claim");
                taskViewHolder.action.setBackgroundResource(R.color.colorButton);

                taskViewHolder.action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //did we already deduct from offending user? if not, we may need to do it here
                        //let's talk about this

                        rotationTaskManager.completeTask(tid);
                    }
                });
            }
        }

        if (curState instanceof ActiveTaskState) {
            long hrsRemaining = rt.getDuration();
            if (hrsRemaining >= 24) {
                long daysRemaining =  hrsRemaining/24;
                String dur = Long.toString(daysRemaining);
                dur = dur.concat("d");
                taskViewHolder.taskDuration.setText(dur);
            }
            else {
                String dur = Long.toString(hrsRemaining);
                dur = dur.concat("h");
                taskViewHolder.taskDuration.setText(dur);
            }
        }

        else if (curState instanceof  PenaltyTaskState) {
            taskViewHolder.taskDuration.setText("Exp");
        }

        else {
            taskViewHolder.taskDuration.setText("");
        }



    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return rotationTaskManager.getNumTasks();
    }

    public final static class TaskViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView taskName;
        TextView taskValue;
        TextView taskDescription;
        TextView taskDuration;
        Button action;
        ImageView pic;
        Button delete;

        TaskViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            taskName = (TextView) itemView.findViewById(R.id.task_name);
            taskValue = (TextView) itemView.findViewById(R.id.task_value);
            taskDescription = (TextView) itemView.findViewById(R.id.task_description);
            taskDuration = (TextView) itemView.findViewById(R.id.task_duration);
            action = (Button) itemView.findViewById(R.id.button);
            pic = (ImageView) itemView.findViewById(R.id.profile_pic);
            delete = (Button) itemView.findViewById(R.id.delete);
        }
    }
}
