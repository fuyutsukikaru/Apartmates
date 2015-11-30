package com.cs130.apartmates.base.tasks;

import java.text.DateFormat;

import com.cs130.apartmates.base.taskstates.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BountyTask implements Task {
    //Getters
    private long m_id;
    private int m_points;
    private long m_creator;
    private String m_deadline; //TODO: might want to change to DateTime
    private long m_time_started;
    private TaskState m_state;
    private ActiveTaskState m_active_state;
    private CompletedTaskState m_completed_state;

    private String m_title;
    private String m_description;

    public BountyTask(long id, int points, long creator, String deadline,
                        String title, String description) {
        m_id = id;
        m_points = points;
        m_title = title;
        m_description = description;
        m_creator = creator;
        m_deadline = deadline;

        m_active_state = new ActiveTaskState(this);
        m_completed_state = new CompletedTaskState(this);
        m_state = m_active_state;
        m_time_started = new Date().getTime();
    }

    //Getters
    public long getId() { return m_id; }
    public int getPoints() { return m_points; }
    public long getCreator() { return m_creator; }
    public String getDeadline() {
        String deadline;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(m_deadline);
            SimpleDateFormat nf = new SimpleDateFormat("MM/dd");
            deadline = nf.format(date);
        } catch(Exception e) {
            deadline = null;
        }
        System.err.println(deadline);
        return deadline;
    }
    public String getTitle() { return m_title; }
    public String getDescription() { return m_description; }

    public TaskState getPendingState() { return new PendingTaskState(this); }
    public TaskState getPenaltyState() { return new PenaltyTaskState(this); }
    public TaskState getActivatedState() { return m_active_state; }
    public TaskState getCompletedState() { return m_completed_state; }
    public void setState(TaskState s) { m_state = s;}



    public long getDuration() {
        if (m_time_started < 0) {
            throw new IllegalStateException("PendingTaskState: Time in current state was never set");
        }
        return (new Date().getTime() - m_time_started);
    }

    public void awardPoints(){
//        User user = getUserbyId(m_assignee);
//        user.earnPoints(this.m_points);
    }
    public void deductPoints(){
//        User user = getUserbyId(m_creator);
//        user.deductPoints(this.m_points);
    }
    //Bounty tasks are active once they are created, so this function is not used.
    public boolean activateTask(){
        return true;
    }
    //implementing to interface.
    public boolean setToBounty(){
        return true;
    }

    /*If returns true,
        Task was completed and points were awarded.
        Task expired and points were deducted.
        Task was not activated and was not completed yet.
        */
    public boolean completeTask(){
        return m_state.completeTask();
    }





}