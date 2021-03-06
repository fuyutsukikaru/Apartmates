package com.cs130.apartmates.base.taskstates;

import com.cs130.apartmates.base.tasks.Task;

public class PendingTaskState implements TaskState {
   private Task task;

    public PendingTaskState(Task t) {
        task = t;
   }

    public boolean activateTask() {
        task.setState(task.getActivatedState());
        return true;
    }

    public boolean completeTask() { return false; }
    public boolean setPending() { return false; }
    public boolean setPenalty() { return false; }
}
