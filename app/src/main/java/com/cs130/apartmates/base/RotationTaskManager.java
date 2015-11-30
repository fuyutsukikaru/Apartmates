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
    private String dropTaskUrl = "/task";
    private String activateRotationTaskUrl = "/task/activate?taskId=";
    private String completeTaskUrl = "/task/complete";
    private String rotateTaskUrl = "/task/rotate?taskId=";

    public RotationTaskManager() {
        m_task_list = new ArrayList<RotationTask>();
        m_member_list = new ArrayList<Long>();
    }

    public void populateTask(long tid, int points, String time_limit, String deadline,
                             String title, String description, String state) {
        m_task_list.add(new RotationTask(tid, points, time_limit, deadline, title, description, state));
    }

    public int getNumTasks() {
        return m_task_list.size();
    }

    public RotationTask getTask(int index) {
        return m_task_list.get(index);
    }

    public int getSize() {
        return m_task_list.size();
    }

    public void setState(String state, int position) {
        m_task_list.get(position).setState(state);
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
    public void addTask(long user_id, long gid, int points, String deadline, String title,
                        String description, String state) {
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("userId", Long.toString(user_id));
            params.put("groupId", Long.toString(gid));
            params.put("title", title);
            params.put("description", description);
            params.put("value", Integer.toString(points));
            params.put("type", "rotation");
            params.put("timeLimit", deadline);

            JSONObject resp = ApartmatesHttpClient.sendRequest(createTaskUrl, params,
                    null, "POST");
            System.err.println("Create task RESP: " + resp);
            if (resp.has("task_id")) {
                populateTask(resp.getLong("task_id"), points, null, deadline, title,
                        description, state);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean dropTask(long uid, long id, int position) {
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("userId", Long.toString(uid));
            params.put("taskId", Long.toString(id));
            JSONObject resp = ApartmatesHttpClient.sendRequest(dropTaskUrl,
                    params, null, "DELETE");
           if (!resp.has("error")) {
               m_task_list.remove(position);
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
            if (!resp.has("error")) {
                for (RotationTask rt : m_task_list){
                    if (rt.getId() == id) {
                        rt.activateTask();
                        rt.setState("activated");
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
    public boolean completeTask(int index, long uid) {
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("userId", Long.toString(uid));
            params.put("taskId", Long.toString(m_task_list.get(index).getId()));
            JSONObject resp = ApartmatesHttpClient.sendRequest(completeTaskUrl, params, null, "POST");
            System.err.println("Complete task RESP: " + resp);
            if (resp.has("points")) {
                m_task_list.get(index).setState("pending");
                return true;
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
