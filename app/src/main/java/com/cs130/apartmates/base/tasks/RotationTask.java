package com.cs130.apartmates.base.tasks;

import com.cs130.apartmates.base.taskstates.ActiveTaskState;
import com.cs130.apartmates.base.taskstates.CompletedTaskState;
import com.cs130.apartmates.base.taskstates.PenaltyTaskState;
import com.cs130.apartmates.base.taskstates.PendingTaskState;
import com.cs130.apartmates.base.taskstates.TaskState;
import com.cs130.apartmates.base.users.User;

import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RotationTask implements Task{
    //Getters
    private long m_id;
    private int m_points;
    private long m_assignee;
    private long m_time_started;
    private String m_time_limit;
    private String m_deadline;

    private TaskState m_state;
    private PendingTaskState m_pending_state;
    private ActiveTaskState m_active_state;
    private PenaltyTaskState m_penalty_state;
    private CompletedTaskState m_completed_state;

    private String m_title;
    private String m_description;
    private String m_current_state;

    public RotationTask(long id, int points, String time_limit, String deadline, long assignee,
                String title, String description, String state) {
        m_id = id;
        m_points = points;
        m_title = title;
        m_description = description;
        m_assignee = assignee;
        m_time_limit = time_limit;
        m_deadline = deadline;

        m_pending_state = new PendingTaskState(this);
        m_active_state = new ActiveTaskState(this);
        m_penalty_state = new PenaltyTaskState(this);
        m_completed_state = new CompletedTaskState(this);
        m_state = m_pending_state;

        m_current_state = state;
    }

    //Getters
    public long getId() { return m_id;}
    public int getPoints() { return m_points;}
    public long getAssignee() {return m_assignee;}
    public String getTitle() {return m_title;}
    public String getDescription() {return m_description;}
    public TaskState getCurrentState() { return m_state;}
    public String getState() { return m_current_state; }

    public TaskState getPendingState(){ return m_pending_state;}
    public TaskState getActivatedState(){ return m_active_state;}
    public TaskState getCompletedState(){ return m_completed_state;}
    public TaskState getPenaltyState() {return m_penalty_state;}
    public void setState(TaskState s){m_state = s;}

    //change to return the number of hours remaining before the task expires
    public long getDuration() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        try {
            Date deadline = format.parse(m_deadline);
            Long time_limit = Long.parseLong(m_time_limit);
            m_time_started = deadline.getTime() - time_limit * 24 * 60 * 60 * 1000;
        } catch(Exception e) {
            e.printStackTrace();
            m_time_started = 0;
        }

        if (m_time_started < 0) {
            throw new IllegalStateException("PendingTaskState: Time in current state was never set");
        }
        return (Calendar.getInstance().getTimeInMillis() - m_time_started) / (1000 * 60 * 60);
    }
    public void setAssignee(long assignee) {
        m_assignee = assignee;
    }

    //Activates the state and records the time started.
    public boolean activateTask() {
        m_time_started = new Date().getTime();
        return true;
    }

    public void setState(String state) {
        m_current_state = state;
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
        if (getDuration() < Long.parseLong(m_time_limit)) {
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
