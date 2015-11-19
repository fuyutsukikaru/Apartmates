package com.cs130.apartmates;

import android.test.ActivityInstrumentationTestCase2;

import com.cs130.apartmates.activities.AddTaskActivity;


/**
 * Created by sjeongus on 10/29/15.
 */
public class AddTaskActivityTest extends ActivityInstrumentationTestCase2<AddTaskActivity> {

    private AddTaskActivity mActivity;

    public AddTaskActivityTest() {
        super(AddTaskActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mActivity = getActivity();

        testPreconditions();
    }

    public void testPreconditions() {
        assertNotNull("mActivity is null", mActivity);
    }
}
