package com.cs130.apartmates.base;

import android.util.JsonWriter;

import com.cs130.apartmates.base.tasks.BountyTask;
import com.cs130.apartmates.base.tasks.RotationTask;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class BountyTaskManager {
    private ArrayList<BountyTask> m_task_list;

    public BountyTaskManager() {
        m_task_list = new ArrayList<BountyTask>();
    }

    public BountyTask getTask(int index) {
        return m_task_list.get(index);
    }

    public int getNumTasks() {
        return m_task_list.size();
    }

    public void addTask(long uid, long gid, int points, String title, String description) {
        try {
            /* just to test GET requests
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("userId", "1");
            String resp = ApartmatesHttpClient.getInstance().sendRequest("/user/info", params, null, "GET");
            System.err.println(resp);
            */

            /* task create not implemented yet
            JSONObject data = new JSONObject();
            data.put("userId", uid);
            data.put("groupId", gid);
            data.put("title", title);
            data.put("description", description);
            data.put("value", points);
            String id = ApartmatesHttpClient.sendRequest("/task/create",
                    data.toString(), "POST");
            System.err.println(id);
            m_task_list.add(new BountyTask(Long.parseLong(id), points, -1, title, description));
            */

            //backend currently not up yet, so start by local changes
            m_task_list.add(new BountyTask(-1, points, -1, title, description));
        } catch (Exception e) {
            e.printStackTrace();
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
