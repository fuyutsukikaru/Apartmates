package com.cs130.apartmates.activities;

/**
 * Created by bchalabian on 10/26/15.
 */
public class BountyTask {
    public int taskValue;
    public String taskName;
    public String description;

    public BountyTask(String n, int val, String des) {
        this.taskName = n;
        this.taskValue = val;
        this.description = des;
    }

}
