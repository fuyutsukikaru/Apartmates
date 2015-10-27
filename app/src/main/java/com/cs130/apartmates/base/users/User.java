package com.cs130.apartmates.base.users;

/**
 * Created by Eric Du on 10/26/2015.
 */
public class User {
    private long m_id;
    private int m_points;

    public User(long id) {
        m_id = id;
        m_points = 0;
    }

    public User(long id, int points) {
        m_id = id;
        m_points = points;
    }

    public long getId() {
        return m_id;
    }

    public int getPoints() {
        return m_points;
    }

    public void setPoints(int m_points) {
        this.m_points = m_points;
    }
}
