package com.cs130.apartmates.base;

import com.cs130.apartmates.base.tasks.RotationTask;

import java.util.ArrayList;
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
    private long m_id_counter; //contains the next task id to be given, increment each time

    public RotationTaskManager() {
        m_task_list = new ArrayList<RotationTask>();
        m_member_list = new ArrayList<Long>();
        m_id_counter = 1;
    }

    public void addTask(int points, long time_limit, String title, String description) {
        m_task_list.add(new RotationTask(m_id_counter, points, time_limit, -1, title, description));
        m_id_counter++;
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