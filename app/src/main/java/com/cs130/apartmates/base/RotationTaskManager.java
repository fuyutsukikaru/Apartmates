package com.cs130.apartmates.base;

import com.cs130.apartmates.base.tasks.BountyTask;
import com.cs130.apartmates.base.tasks.RotationTask;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/*
 * List of rotation tasks that also holds a list of participating members
 * - assigns rotation tasks in order
 * - manages state of each task
 */
public class RotationTaskManager {
    private ArrayList<RotationTask> m_task_list;
    private ArrayList<Long> m_member_list;

    private String createTaskUrl = "/task/create";
    private String dropTaskUrl = "/task?taskId=";

    public RotationTaskManager() {
        m_task_list = new ArrayList<RotationTask>();
        m_member_list = new ArrayList<Long>();
    }

    public void populateTask(long tid, int points, long deadline, String title, String description) {
        m_task_list.add(new RotationTask(tid, points, deadline, title, description));
    }

    public void addTask(long gid, int points, long deadline, String title, String description) {
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            //params.put("userId", Long.toString(uid));
            params.put("groupId", Long.toString(gid));
            params.put("title", title);
            params.put("description", description);
            params.put("value", Integer.toString(points));
            params.put("type", "rotation");
            params.put("deadline", Long.toString(deadline));

            JSONObject resp = ApartmatesHttpClient.sendRequest(createTaskUrl, params,
                    null, "POST");
            System.err.println("RESP: " + resp);
            if (resp.has("task_id")) {
                populateTask(resp.getLong("task_id"), points, deadline, title, description);
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

    public void activateTask(long id) {
        for (RotationTask rt : m_task_list) {
            if (rt.getId() == id) {
                rt.activateTask();
            }
        }
    }

    /*
     * adds member to list at a random location (to make task allocation more fair even when
     *  adding new members after tasks have already been assigned
     */
    public void addMember(long id) {
        m_member_list.add(new Random().nextInt(m_member_list.size() + 1), id);
    }

    /* Remove a member and unassign all tasks associated with that member
     *
     */
    public void removeMember(long id) {
        Iterator<Long> iterator =  m_member_list.iterator();
        while(iterator.hasNext()) {
            Long user_id = iterator.next();
            if(user_id == id) {
                iterator.remove();
            }
        }

        for (RotationTask rt : m_task_list) {
            if (rt.getId() == id) {
                rt.completeTask();
                rt.setAssignee(-1);
            }
        }
    }

    //assign all tasks that currently have no assignees
    public void assignAllTasks() {
        if (m_member_list.size() == 0) {
            return; //not sure if we should throw an exception here or not
        }
        for (RotationTask rt : m_task_list) {
            if (rt.getAssignee() < 0) {
                rt.setAssignee(m_member_list.get(new Random().nextInt(m_member_list.size())));
            }
        }
    }

}
