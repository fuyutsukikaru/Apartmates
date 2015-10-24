package com.cs130.apartmates.base.taskstates;

import java.util.Date;

public class PenaltyTaskState implements TaskState {
    private long m_time;

    public PenaltyTaskState() {
        m_time = -1; //indicating m_time is invalid
    }

    public void setTime(long time) {
        m_time = time;
    }

    public long getDuration() {
        if (m_time < 0) {
            throw new IllegalStateException("PenaltyTaskState: Time in current state was never set");
        }
        return (new Date().getTime() - m_time);
    }

}
