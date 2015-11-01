package com.cs130.apartmates.base.taskstates;

public interface TaskState {

    //Sets a pending task to an activated state and starts the countdown of the time limit.
    public boolean activateTask();

    //Sets the task as completed, awards points to the Assignee
    public boolean completeTask();

    //Sets the task to pending state;
    public boolean setPending();

    public boolean setPenalty();

}
