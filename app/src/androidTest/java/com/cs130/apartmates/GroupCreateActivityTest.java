package com.cs130.apartmates;

import android.graphics.Point;
import android.support.v7.widget.Toolbar;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import com.cs130.apartmates.activities.GroupCreateActivity;
import com.robotium.solo.Solo;

/**
 * Created by Eric Du on 11/18/2015.
 */
public class GroupCreateActivityTest extends ActivityInstrumentationTestCase2<GroupCreateActivity> {
    private GroupCreateActivity mActivity;
    private Toolbar mAb;
    private Button mCreate;
    private Button mJoin;

    private EditText mNameText;
    private EditText mPasswordText;
    private EditText mPasswordCreateText;
    private EditText mStakesText;
    private EditText mGidText;
    private Solo solo;

    private int width;
    private int height;

    public GroupCreateActivityTest() {
        super(GroupCreateActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mActivity = getActivity();
        mAb = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mCreate = (Button) mActivity.findViewById(R.id.create);
        mJoin = (Button) mActivity.findViewById(R.id.join);

        mNameText = (EditText) mActivity.findViewById(R.id.groupName);
        mPasswordText = (EditText) mActivity.findViewById(R.id.password);
        mPasswordCreateText = (EditText) mActivity.findViewById(R.id.passwordCreate);
        mStakesText = (EditText) mActivity.findViewById(R.id.monthlyStakes);
        mGidText = (EditText) mActivity.findViewById(R.id.groupId);
        solo = new Solo(getInstrumentation(), getActivity());

        Point size = new Point();
        mActivity.getWindowManager().getDefaultDisplay().getSize(size);
        width = size.x;
        height = size.y;
    }

    public void testAllPreconditions() {
        assertNotNull("mActivity is null", mActivity);
        assertNotNull("mAb is null", mAb);
        assertNotNull("mCreate is null", mCreate);
        assertNotNull("mJoin is null", mJoin);
        assertNotNull("mNameText is null", mNameText);
        assertNotNull("mPasswordText is null", mPasswordText);
        assertNotNull("mPasswordCreateText is null", mPasswordCreateText);
        assertNotNull("mStakesText is null", mStakesText);
        assertNotNull("mGidText is null", mGidText);
    }

    //Precondition: user is current not in a group
    //creates group with name test, password test, and $10 stakes
    public void testCreateGroup() {
        try {
            solo.enterText(mNameText, "test");
            solo.enterText(mPasswordCreateText, "test");
            solo.enterText(mStakesText, "10");

            solo.clickOnButton("Create");
            solo.waitForActivity("MainActivity", 5000);

            solo.clickOnScreen(width / 10, height / 10);
            assertTrue(solo.searchText("My Tasks"));
        } catch (Exception e) {

        }
        solo.finishOpenedActivities();
    }

}
