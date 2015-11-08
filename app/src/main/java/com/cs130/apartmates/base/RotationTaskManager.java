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
    private String activateRotationTaskUrl = "/task/activate?taskId=";
    private String completeTaskUrl = "/task/complete?taskId=";
    private String rotateTaskUrl = "/task/rotate?taskId=";

    //private String setTaskToBounty =

    public RotationTaskManager() {
        m_task_list = new ArrayList<RotationTask>();
        m_member_list = new ArrayList<Long>();
    }

    public void populateTask(long tid, long user_id, int points, long deadline, String title, String description) {
        m_task_list.add(new RotationTask(tid, points, deadline, user_id, title, description));
    }

    public int getNumTasks() {
        return m_task_list.size();
    }

    public RotationTask getTask(int index) {
        return m_task_list.get(index);
    }

    //Assign a user to the task initially and the rotation will be dependent on the apartment list.
    public void addTask(long gid, long user_id, int points, long deadline, String title, String description) {
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("userId", Long.toString(user_id));
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
                populateTask(resp.getLong("task_id"),user_id, points, deadline, title, description);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean dropTask(int id) {
        try {
            JSONObject resp = ApartmatesHttpClient.sendRequest(dropTaskUrl + m_task_list.get(id).getId(),
                    null, null, "DELETE");
           if(resp.has("success") && resp.get("success") == "true") {
               for (RotationTask rt : m_task_list) {
                   if (rt.getId() == id) {
                       m_task_list.remove(rt);
                   }
               }
               return true;
           }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean activateTask(long id) {
        try {
            JSONObject resp = ApartmatesHttpClient.sendRequest(activateRotationTaskUrl + id, null, null, "POST");
            System.err.println("RESP: " + resp);
            if (resp.has("success") && resp.get("success") == "true") {
                for(RotationTask rt : m_task_list){
                    if(rt.getId() == id){
                        rt.activateTask();
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Complete task will increment the points of the current users and set the task to complete state.
    public boolean completeTask(long id) {
        try {
            JSONObject resp = ApartmatesHttpClient.sendRequest(completeTaskUrl + id, null, null, "POST");
            System.err.println("RESP: " + resp);
            if (resp.has("success") && resp.get("success") == "true") {
                RotationTask task = null;
                long currentUser_id = -1;
                for (RotationTask rt : m_task_list) {
                    if (rt.getId() == id) {
                        task = rt;
                    }
                }
                if(task == null)
                    return false;
                task.completeTask();
                int currentUser_index = m_member_list.indexOf(task.getAssignee());
                long nextUser_id;
                if(currentUser_index == m_member_list.size() - 1){
                    nextUser_id = m_member_list.get(0);
                }
                else {
                    nextUser_id = m_member_list.get(currentUser_index + 1);
                }
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("nextUserId", Long.toString(nextUser_id));
                resp = ApartmatesHttpClient.sendRequest(rotateTaskUrl + id, params, null, "POST");
                if(resp.has("success") && resp.get("success") == "true"){
                    task.setAssignee(nextUser_id);
                    task.setState(task.getPendingState());
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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


    //Assigned a user on creation to reduce call to the backend.

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
