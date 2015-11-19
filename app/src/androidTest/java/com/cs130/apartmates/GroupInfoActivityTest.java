package com.cs130.apartmates;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.cs130.apartmates.activities.GroupInfoActivity;

/**
 * Created by Eric Du on 11/18/2015.
 */
public class GroupInfoActivityTest extends ActivityInstrumentationTestCase2<GroupInfoActivity> {
    private GroupInfoActivity mActivity;
    private TextView mGroupName;
    private TextView mGroupID;
    private TextView mMemberNames;

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
        testPreconditions();
    }

    public void testPreconditions() {
        assertNotNull("mActivity is null", mActivity);
        assertNotNull("mGroupName is null", mGroupName);
        assertNotNull("mGroupID is null", mGroupID);
        assertNotNull("mMemberNames is null", mMemberNames);
    }

}
