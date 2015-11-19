package com.cs130.apartmates;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import com.cs130.apartmates.activities.AddTaskActivity;

/**
 * Created by sjeongus on 10/29/15.
 */
public class AddTaskActivityTest extends ActivityInstrumentationTestCase2<AddTaskActivity> {

    private AddTaskActivity mActivity;
    private EditText mTitle;
    private EditText mDescription;
    private EditText mValue;
    private EditText mDuration;
    private Button mCreateButton;
    private Button mCancelButton;

    public AddTaskActivityTest() {
        super(AddTaskActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mActivity = getActivity();
        mTitle = (EditText) mActivity.findViewById(R.id.title);
        mDescription = (EditText) mActivity.findViewById(R.id.description);
        mValue = (EditText)mActivity.findViewById(R.id.value);
        mDuration = (EditText) mActivity.findViewById(R.id.duration);
        mCreateButton = (Button) mActivity.findViewById(R.id.create);
        mCancelButton = (Button) mActivity.findViewById(R.id.cancel);

        testPreconditions();
    }

    public void testPreconditions() {
        assertNotNull("mActivity is null", mActivity);
        assertNotNull("mTitle is null", mTitle);
        assertNotNull("mDescription is null", mDescription);
        assertNotNull("mValue is null", mValue);
        assertNotNull("mDuration is null", mDuration);
        assertNotNull("mCreateButton is null", mCreateButton);
        assertNotNull("mCancelButton is null", mCancelButton);
    }
}
