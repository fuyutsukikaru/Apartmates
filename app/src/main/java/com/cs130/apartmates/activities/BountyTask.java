package com.cs130.apartmates.activities;

/**
 * Created by bchalabian on 10/26/15.
 */
public class BountyTask {
    public int taskValue;
    public String taskName;
    public String description;
    public String action;
    public Boolean mine;
    //we will want to replace the dummy image with the venmo profile picture of the user who posted the bounty

    public BountyTask(String n, int val, String des, Boolean my) {
        this.taskName = n;
        this.taskValue = val;
        this.description = des;
        this.mine = my;
        if (mine == Boolean.TRUE)
            this.action = "Drop";
        else
            this.action = "Claim";
    }

}
