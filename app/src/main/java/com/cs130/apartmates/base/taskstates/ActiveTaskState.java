package com.cs130.apartmates.base.taskstates;

import com.cs130.apartmates.base.tasks.Task;

public class ActiveTaskState implements TaskState {
    private Task task;

    public ActiveTaskState(Task t) {
        task = t;
    }

    public boolean activateTask(){return false;}
    public boolean setPending(){return false;}
    public boolean completeTask(){
        task.awardPoints();
        task.setState(task.getCompletedState());
        return true;
    }
    public boolean setPenalty(){
        task.deductPoints();
        task.setState(task.getPenaltyState());
        return true;
    }

}
