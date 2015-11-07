package com.cs130.apartmates.activities;

/**
 * Created by nhadfieldmenell on 11/3/15.
 */
public class RotationTask {
    public int taskID;
    public int taskValue;
    public int state;       //0 for pending, 1 for active
    public int[] rotation;  //holds the order of people who are assigned to it
    public String taskName;
    public String description;
    public String action;
    public Boolean mine;
    //we will want to replace the dummy image with the venmo profile picture of the user who posted the bounty

    public RotationTask(String n, int val, String des, int sta, Boolean my) {
        this.taskName = n;
        this.taskValue = val;
        this.description = des;
        this.state = sta;
        this.mine = my;
        //We will want to take out this boolean (it is there as a placeholder) and decide on Drop or Claim
        //by comparing bounty owner id and current user id
        if (state == 0)
            this.action = "Ready";
        else {
            if (mine){
                this.action = "Claim";
            }
            //else
                //hide the button
        }
    }
}
