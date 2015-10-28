package com.cs130.apartmates.base.tasks;
import com.cs130.apartmates.base.taskstates.TaskState;

/* This can probably be an abstract class instead of an interface
 * if the two types of tasks are really similar
 */


public interface Task  {
    public long getId();
    public int getPoints();  //May be used to display points on screen
    public long getAssignee();
    public String getTitle();
    public String getDescription();

    public TaskState getPendingState();
    public TaskState getActivatedState();
    public TaskState getCompletedState();
    public TaskState getPenaltyState();
    public void setState(TaskState s);

    public void setAssignee(long assignee);
    public void awardPoints();
    public void deductPoints();
    public boolean activateTask();
    public boolean completeTask();
    public boolean setToBounty();

}
