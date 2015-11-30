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
import com.cs130.apartmates.base.BountyTaskManager;
import com.cs130.apartmates.base.tasks.BountyTask;
import com.cs130.apartmates.fragments.BountyFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by bchalabian on 10/26/15.
 */
public class BTAdapter extends RecyclerView.Adapter<BTAdapter.TaskViewHolder> {
    private static final String TAG = "BTAdapter";
    private BountyTaskManager bountyTaskManager;
    private long mId;
    private BountyFragment mBf;
    private Context context;

    public BTAdapter(BountyFragment bf, Context context, long id) {
        bountyTaskManager = new BountyTaskManager();
        mId = id;
        mBf = bf;
        this.context = context;
    }

    public BountyTaskManager getManager() {
        return bountyTaskManager;
    }

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
        long id = bt.getCreator();
        String url = getProfilePic(id);
        Picasso.with(context).load(url).into(taskViewHolder.pic);
        taskViewHolder.taskName.setText(bt.getTitle());
        Integer val = new Integer(bt.getPoints());
        taskViewHolder.taskValue.setText(val.toString());
        taskViewHolder.taskDescription.setText(bt.getDescription());
        taskViewHolder.taskDeadline.setText(bt.getDeadline());

        final int fPos = position;
        if (mId == id) {
            taskViewHolder.action.setText("Drop");
            taskViewHolder.action.setBackgroundResource(R.color.colorButtonNegate);
            taskViewHolder.action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bountyTaskManager.dropTask(mId, fPos);
                    notifyItemRemoved(fPos);
                    notifyItemRangeChanged(fPos, bountyTaskManager.getSize());
                }
            });
        } else {
            taskViewHolder.action.setText("Claim");
            taskViewHolder.action.setBackgroundResource(R.color.colorButton);
            taskViewHolder.action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //final int val = bountyTaskManager.getTask(fPos).getPoints();
                    //mBf.addPoints(val);
                    bountyTaskManager.claimTask(mId, fPos);
                    notifyItemRemoved(fPos);
                    notifyItemRangeChanged(fPos, bountyTaskManager.getSize());
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
        TextView taskDeadline;
        Button action;
        ImageView pic;

        TaskViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            taskName = (TextView) itemView.findViewById(R.id.task_name);
            taskValue = (TextView) itemView.findViewById(R.id.task_value);
            taskDescription = (TextView) itemView.findViewById(R.id.task_description);
            action = (Button) itemView.findViewById(R.id.button);
            pic = (ImageView) itemView.findViewById(R.id.profile_pic);
            taskDeadline = (TextView ) itemView.findViewById(R.id.task_duration);
        }
    }
}
