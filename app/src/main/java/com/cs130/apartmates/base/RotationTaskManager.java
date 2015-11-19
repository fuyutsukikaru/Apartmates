package com.cs130.apartmates.base;

import com.cs130.apartmates.base.tasks.BountyTask;
import com.cs130.apartmates.base.tasks.RotationTask;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class RotationTaskManager {
    private ArrayList<RotationTask> m_task_list;
    private ArrayList<Long> m_member_list;

    //TODO: maybe we can move to a common constants file
    private String createTaskUrl = "/task/create";
    private String dropTaskUrl = "/task?taskId=";
    private String activateRotationTaskUrl = "/task/activate?taskId=";
    private String completeTaskUrl = "/task/complete";
    private String rotateTaskUrl = "/task/rotate?taskId=";

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

    // Returns a task at the virtual index specified given a user's id that specifies their virtual task list
    public RotationTask getTaskByUser(int index, long user_id) {
        int userTaskPos = 0;
        for (int i = 0; i < m_task_list.size(); i++) {
            if (getTask(i).getId() == user_id) {
                if (userTaskPos == index)
                    return getTask(i);
                else
                    userTaskPos++;
            }
        }

        // Return an invalid entry just in case r
        return m_task_list.get(m_task_list.size() + 1);
    }

    // Gets the number of tasks that a user with the user_id currently has
    public int getNumTasksByUser(long user_id) {
        int num = 0;
        for (int i = 0; i < m_task_list.size(); i++) {
            if (getTask(i).getId() == user_id) {
                num++;
            }
        }

        return num;
    }

    public void clear() {
        m_task_list.clear();
    }

    //Assign a user to the task initially and the rotation will be dependent on the apartment list.
    public void addTask(long user_id, long gid, int points, long deadline, String title, String description) {
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("userId", Long.toString(user_id));
            params.put("groupId", Long.toString(gid));
            params.put("title", title);
            params.put("description", description);
            params.put("value", Integer.toString(points));
            params.put("type", "rotation");
            params.put("timeLimit", Long.toString(deadline));

            JSONObject resp = ApartmatesHttpClient.sendRequest(createTaskUrl, params,
                    null, "POST");
            System.err.println("Create task RESP: " + resp);
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
           if (resp.has("success") && resp.get("success") == "true") {
               for (RotationTask rt : m_task_list) {
                   if (rt.getId() == id) {
                       m_task_list.remove(rt);
                       break;
                   }
               }
               return true;
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean activateTask(long id) {
        try {
            JSONObject resp = ApartmatesHttpClient.sendRequest(activateRotationTaskUrl + id, null, null, "POST");
            System.err.println("Activate task RESP: " + resp);
            if (resp.has("success") && resp.get("success") == "true") {
                for (RotationTask rt : m_task_list){
                    if (rt.getId() == id) {
                        rt.activateTask();
                        break;
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //Complete task will increment the points of the current users and set the task to complete state.
    public boolean completeTask(long tid) {
        try {
            JSONObject resp = ApartmatesHttpClient.sendRequest(completeTaskUrl + tid, null, null, "POST");
            System.err.println("Complete task RESP: " + resp);
            if (resp.has("success") && resp.get("success") == "true") {
                return rotate(tid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    //add member and keep list sorted by member id
    public void addMember(long id) {
        m_member_list.add(id);
        Collections.sort(m_member_list);
    }

    /* Remove a member and unassign all tasks associated with that member
     *
     */
    public void removeMember(long id) {
        for (RotationTask rt : m_task_list) {
            if (rt.getAssignee() == id) {
                rotate(rt.getId());
            }
        }

        Iterator<Long> iterator =  m_member_list.iterator();
        while (iterator.hasNext()) {
            Long user_id = iterator.next();
            if (user_id == id) {
                iterator.remove();
            }
        }

    }

    //if we ever have a bug in this func, it's probably because we operated on a copy of a task
    //rather than the actual one
    //rotates the given task so that the next person on the list is assigned to it
    public boolean rotate(long tid) {
        try {
            RotationTask rt = null;
            for (RotationTask t : m_task_list) {
                if (t.getAssignee() == tid) {
                    rt = t;
                    break;
                }
            }
            if (rt == null)
                return false;

            rt.completeTask(); //not sure if we need to factor this into the return
            int currentUser_index = m_member_list.indexOf(rt.getAssignee());
            long nextUser_id;
            if (currentUser_index == m_member_list.size() - 1) {
                nextUser_id = m_member_list.get(0);
            } else {
                nextUser_id = m_member_list.get(currentUser_index + 1);
            }
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("taskId", Long.toString(tid));
            params.put("nextUserId", Long.toString(nextUser_id));
            JSONObject resp = ApartmatesHttpClient.sendRequest(rotateTaskUrl, params, null, "POST");
            if (resp.has("success") && resp.get("success") == "true") {
                rt.setAssignee(nextUser_id);
                rt.setState(rt.getPendingState());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //assign all tasks that currently have no assignees
    //not sure if we want random allocation or even allocation (i.e. give first task to first person,
    //second task to second person, etc.)
    //if we assign an assignee on creation, this function is not needed
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
