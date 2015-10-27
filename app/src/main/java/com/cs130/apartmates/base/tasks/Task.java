package com.cs130.apartmates.base.tasks;

import com.cs130.apartmates.base.taskstates.TaskState;

/* This can probably be an abstract class instead of an interface
 * if the two types of tasks are really similar
 */
public interface Task {

    long getId(); //return ID of task
    int getPoints(); //return point value of task
    long getTimeLimit(); //return time limit of task in ms
    long getAssignee(); //return ID of assignee
    void setAssignee(long assignee); //set ID of assignee of task
    public String getTitle(); //get title of task
    public String getDescription(); //get description of task
    TaskState getState(); //return state of task
    void setState(TaskState s); //set the state of a task

}
