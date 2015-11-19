package com.cs130.apartmates;

import android.test.ActivityInstrumentationTestCase2;
import com.cs130.apartmates.activities.LoginActivity;

/**
 * Created by Eric Du on 11/18/2015.
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private LoginActivity mActivity;

    public LoginActivityTest() {
        super(LoginActivity.class);
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
