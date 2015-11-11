package com.cs130.apartmates.base.tasks;

import com.cs130.apartmates.base.taskstates.ActiveTaskState;
import com.cs130.apartmates.base.taskstates.CompletedTaskState;
import com.cs130.apartmates.base.taskstates.PenaltyTaskState;
import com.cs130.apartmates.base.taskstates.PendingTaskState;
import com.cs130.apartmates.base.taskstates.TaskState;
import com.cs130.apartmates.base.users.User;

import java.util.Date;

public class RotationTask implements Task{
    //Getters
    private long m_id;
    private int m_points;
    private long m_assignee;
    private long m_time_started;
    private long m_time_limit;

    private TaskState m_state;
    private PendingTaskState m_pending_state;
    private ActiveTaskState m_active_state;
    private PenaltyTaskState m_penalty_state;
    private CompletedTaskState m_completed_state;

    private String m_title;
    private String m_description;

    public RotationTask(long id, int points, long time_limit, long assignee,
                String title, String description) {
        m_id = id;
        m_points = points;
        m_title = title;
        m_description = description;
        m_assignee = assignee;
        m_time_limit = time_limit;

        m_pending_state = new PendingTaskState(this);
        m_active_state = new ActiveTaskState(this);
        m_penalty_state = new PenaltyTaskState(this);
        m_completed_state = new CompletedTaskState(this);
        m_state = m_pending_state;
    }

    //Getters
    public long getId() { return m_id;}
    public int getPoints() { return m_points;}
    public long getAssignee() {return m_assignee;}
    public String getTitle() {return m_title;}
    public String getDescription() {return m_description;}
    public TaskState getCurrentState() { return m_state;}

    public TaskState getPendingState(){ return m_pending_state;}
    public TaskState getActivatedState(){ return m_active_state;}
    public TaskState getCompletedState(){ return m_completed_state;}
    public TaskState getPenaltyState() {return m_penalty_state;}
    public void setState(TaskState s){m_state = s;}

    public long getDuration() {
        if (m_time_started < 0) {
            throw new IllegalStateException("PendingTaskState: Time in current state was never set");
        }
        return (new Date().getTime() - m_time_started);
    }
    public void setAssignee(long assignee) {
        m_assignee = assignee;
    }

    //Activates the state and records the time started.
    public boolean activateTask(){
        m_time_started = new Date().getTime();
        return m_state.activateTask();
    }

    public boolean setToBounty(){
        m_state.setPenalty();
        return true;
    }

    /* If returns true,
     * Task was completed and points were awarded.
     * Task expired and points were deducted.
     * Task was not activated and was not completed yet.
     */
    public boolean completeTask(){
        boolean endTurn;
        if (getDuration() < m_time_limit) {
            endTurn = m_state.completeTask();
        } else {
            endTurn = m_state.setPenalty();
        }
        if (endTurn) {
            return m_state.setPending();
        }
        return false;
    }

    public void awardPoints() {
        //TODO: stub
    }

    public void deductPoints() {
        //TODO: stub
    }

}
