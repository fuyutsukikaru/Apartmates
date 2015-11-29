package com.cs130.apartmates;

import android.graphics.Point;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.TextView;

import com.cs130.apartmates.activities.GroupInfoActivity;
import com.robotium.solo.Solo;

/**
 * Created by Eric Du on 11/18/2015.
 */
public class GroupInfoActivityTest extends ActivityInstrumentationTestCase2<GroupInfoActivity> {
    private GroupInfoActivity mActivity;
    private TextView mGroupName;
    private TextView mGroupID;
    private TextView mMemberNames;
    private Solo solo;

    private int width;
    private int height;

    public GroupInfoActivityTest() {
        super(GroupInfoActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mActivity = getActivity();
        mGroupName = (TextView) mActivity.findViewById(R.id.displayGroupName);
        mGroupID = (TextView) mActivity.findViewById(R.id.displayGroupID);
        mMemberNames = (TextView) mActivity.findViewById(R.id.displayMemberNames);
        solo = new Solo(getInstrumentation(), getActivity());

        Point size = new Point();
        mActivity.getWindowManager().getDefaultDisplay().getSize(size);
        width = size.x;
        height = size.y;
    }

    public void testAllPreconditions() {
        solo.sendKey(Solo.ENTER);

        assertNotNull("mActivity is null", mActivity);
        assertNotNull("mGroupName is null", mGroupName);
        assertNotNull("mGroupID is null", mGroupID);
        assertNotNull("mMemberNames is null", mMemberNames);

        solo.finishOpenedActivities();
    }

    //Precondition: user is currently in a group
    //leaves current group and then joins group with id 11 and password password
    public void testLeaveAndJoin() {
        try {
            solo.clickOnButton("Leave Group");
            solo.waitForActivity("GroupCreateActivity", 5000);

            assertTrue(solo.searchButton("Join Group"));
            assertTrue(solo.searchButton("Create Group"));

            solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.groupId), "11");
            solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.password), "password");
            solo.clickOnButton("Join Group");
            solo.waitForActivity("MainActivity", 5000);

            solo.clickOnScreen(width / 10, height / 10);
            assertTrue(solo.searchText("My Tasks"));
        } catch (Exception e) {

        }
        solo.finishOpenedActivities();
    }
}
