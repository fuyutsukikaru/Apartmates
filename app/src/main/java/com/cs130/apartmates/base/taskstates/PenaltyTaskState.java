package com.cs130.apartmates.base.taskstates;

import com.cs130.apartmates.base.tasks.Task;

public class PenaltyTaskState implements TaskState {
    private Task task;
    public PenaltyTaskState(Task t) {
       task = t;
    }

    public boolean activateTask(){return false;}
    public boolean completeTask(){return false;}
    public boolean setPending(){
        task.setState(task.getPendingState());
        return true;}
    public boolean setPenalty(){return false;}

}
