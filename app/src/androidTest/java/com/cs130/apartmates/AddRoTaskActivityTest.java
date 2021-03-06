package com.cs130.apartmates;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import com.cs130.apartmates.activities.AddRoTaskActivity;
import com.robotium.solo.Solo;

/**
 * Created by Eric Du on 11/18/2015.
 */
public class AddRoTaskActivityTest extends ActivityInstrumentationTestCase2<AddRoTaskActivity> {

    private AddRoTaskActivity mActivity;
    private EditText mTitle;
    private EditText mDescription;
    private EditText mValue;
    private EditText mDeadline;
    private Button mCreateButton;
    private Button mCancelButton;
    private Solo solo;

    public AddRoTaskActivityTest() {
        super(AddRoTaskActivity.class);

    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mActivity = getActivity();
        mTitle = (EditText) mActivity.findViewById(R.id.title);
        mDescription = (EditText) mActivity.findViewById(R.id.description);
        mValue = (EditText)mActivity.findViewById(R.id.value);
        mDeadline = (EditText) mActivity.findViewById(R.id.deadline);
        mCreateButton = (Button) mActivity.findViewById(R.id.create);
        mCancelButton = (Button) mActivity.findViewById(R.id.cancel);
        solo = new Solo(getInstrumentation(), getActivity());

        testPreconditions();
    }

    public void testPreconditions() {
        assertNotNull("mActivity is null", mActivity);
        assertNotNull("mTitle is null", mTitle);
        assertNotNull("mDescription is null", mDescription);
        assertNotNull("mValue is null", mValue);
        assertNotNull("mDeadline is null", mDeadline);
        assertNotNull("mCreateButton is null", mCreateButton);
        assertNotNull("mCancelButton is null", mCancelButton);
    }

    public void testCreateTask() {
        solo.enterText(mTitle, "Test task");
        solo.enterText(mDescription, "Test description");
        solo.enterText(mValue, "5");
        solo.enterText(mDeadline, "2016-11-25");
        solo.clickOnText("Create");
    }
}
