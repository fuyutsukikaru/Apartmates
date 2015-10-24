package com.cs130.apartmates.base.taskstates;

import java.util.Date;

public class PendingTaskState implements TaskState {
    private long m_time;

    public PendingTaskState() {
        m_time = -1; //indicating m_time is invalid
    }

    public void setTime(long time) {
        m_time = time;
    }

    public long getDuration() {
        if (m_time < 0) {
            throw new IllegalStateException("PendingTaskState: Time in current state was never set");
        }

        return (new Date().getTime() - m_time);
    }

}
