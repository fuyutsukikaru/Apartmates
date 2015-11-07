package com.cs130.apartmates.base.taskstates;

import com.cs130.apartmates.base.tasks.Task;

public class CompletedTaskState implements TaskState {
    private Task task;

    public CompletedTaskState(Task t) {
        task = t;
   }

    public boolean activateTask() { return false; }
    public boolean completeTask() { return false; }

    public boolean setPending() {
        task.setState(task.getPendingState());
        return true;
    }

    public boolean setPenalty() { return false; }
}
