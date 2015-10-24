package com.cs130.apartmates.base.taskstates;

public interface TaskState {

    void setTime(long time); //set the time at which the state last changed
    long getDuration(); //get time task has been in state, in ms

}
