package com.cs130.apartmates.base;

import com.cs130.apartmates.base.tasks.BountyTask;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BountyTaskManager {
    private ArrayList<BountyTask> m_task_list;

    private String createTaskUrl = "/task/create";
    private String dropTaskUrl = "/task?taskId=";

    public BountyTaskManager() {
        m_task_list = new ArrayList<BountyTask>();
    }

    public BountyTask getTask(int index) {
        return m_task_list.get(index);
    }

    public int getNumTasks() {
        return m_task_list.size();
    }

    public void populateTask(long tid, long uid, int points, String title, String description) {
        m_task_list.add(new BountyTask(tid, points, uid, title, description));
    }

    public void addTask(long uid, long gid, int points, String title, String description) {
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("userId", Long.toString(uid));
            params.put("groupId", Long.toString(gid));
            params.put("title", title);
            params.put("description", description);
            params.put("value", Integer.toString(points));
            params.put("type", "bounty");

            JSONObject resp = ApartmatesHttpClient.sendRequest(createTaskUrl, params,
                    null, "POST");
            System.err.println("RESP: " + resp);
            if (resp.has("task_id")) {
                m_task_list.add(new BountyTask(resp.getLong("task_id"), points, uid, title, description));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean dropTask(int index) {
        try {
            JSONObject resp = ApartmatesHttpClient.sendRequest(dropTaskUrl + m_task_list.get(index).getId(),
                    null, null, "DELETE");
            m_task_list.remove(index);
            return (resp.has("success") && resp.get("success") == "true");
        } catch (Exception e) {
            return false;
        }
    }

    public void claimTask(long id) {
        for (BountyTask bt : m_task_list) {
            if (bt.getId() == id) {
                //STUB remove bt and award points
            }
        }
    }
}
