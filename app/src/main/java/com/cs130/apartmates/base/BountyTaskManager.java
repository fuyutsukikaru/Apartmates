package com.cs130.apartmates.base;

import com.cs130.apartmates.base.tasks.BountyTask;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BountyTaskManager {
    private ArrayList<BountyTask> m_task_list;

    private String createTaskUrl = "/task/create";
    private String dropTaskUrl = "/task";
    private String completeTaskUrl = "/task/complete";

    public BountyTaskManager() {
        m_task_list = new ArrayList<BountyTask>();
    }

    public BountyTask getTask(int index) {
        return m_task_list.get(index);
    }

    public int getNumTasks() {
        return m_task_list.size();
    }

    public int getSize() {
        return m_task_list.size();
    }

    public void populateTask(long tid, long uid, int points, String deadline, String title, String description) {
        m_task_list.add(new BountyTask(tid, points, uid, deadline, title, description));
    }

    public void addTask(long uid, long gid, int points, String deadline, String title, String description) {
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("userId", Long.toString(uid));
            params.put("groupId", Long.toString(gid));
            params.put("deadline", deadline);
            params.put("title", title);
            params.put("description", description);
            params.put("value", Integer.toString(points));
            params.put("type", "bounty");

            JSONObject resp = ApartmatesHttpClient.sendRequest(createTaskUrl, params,
                    null, "POST");
            System.err.println("RESP: " + resp);
            if (resp.has("task_id")) {
                m_task_list.add(new BountyTask(resp.getLong("task_id"), points, uid, deadline, title, description));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean dropTask(long uid, int index) {
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("userId", Long.toString(uid));
            params.put("taskId", Long.toString(m_task_list.get(index).getId()));
            JSONObject resp = ApartmatesHttpClient.sendRequest(dropTaskUrl,
                    params, null, "DELETE");
            if (!resp.has("error")) {
                m_task_list.remove(index);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean claimTask(long uid, int index) {
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("userId", Long.toString(uid));
            params.put("taskId", Long.toString(m_task_list.get(index).getId()));
            JSONObject resp = ApartmatesHttpClient.sendRequest(completeTaskUrl, params, null, "POST");
            if (resp.has("points")) {
                m_task_list.remove(index);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void clear() {
        m_task_list = new ArrayList<BountyTask>();
    }
}
