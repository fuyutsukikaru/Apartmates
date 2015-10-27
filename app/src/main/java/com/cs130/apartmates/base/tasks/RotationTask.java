package com.cs130.apartmates.base.tasks;

import com.cs130.apartmates.base.taskstates.*;

import java.util.Date;

public class RotationTask implements Task {
    private long m_id;
    private int m_points;
    private long m_time_limit;
    private long m_assignee;

    private TaskState m_state;
    private PendingTaskState m_pending_state;
    private ActiveTaskState m_active_state;
    private PenaltyTaskState m_penalty_state;

    private String m_title;
    private String m_description;

    public RotationTask(long id, int points, long time_limit, long assignee,
                        String title, String description) {
        m_id = id;
        m_points = points;
        m_time_limit = time_limit;
        m_assignee = assignee;

        m_pending_state = new PendingTaskState();
        m_active_state = new ActiveTaskState();
        m_penalty_state = new PenaltyTaskState();
        m_state = m_pending_state;

        m_title = title;
        m_description = description;
    }

    public long getId() {
        return m_id;
    }

    public int getPoints() {
        return m_points;
    }

    public long getTimeLimit() {
        return m_time_limit;
    }

    public long getAssignee() {
        return m_assignee;
    }

    public void setAssignee(long assignee) {
        m_assignee = assignee;
    }

    public String getTitle() {return m_title;}

    public String getDescription() {return m_description;}

    public TaskState getState() {
        return m_state;
    }

    public void setState(TaskState s) {
        m_state = s;
        s.setTime(new Date().getTime());
    }

    public void setStateActive() {
        setState(m_active_state);
    }

    public void setStatePending() {
        setState(m_pending_state);
    }

    public void setStatePenalty() {
        setState(m_penalty_state);
    }

}
