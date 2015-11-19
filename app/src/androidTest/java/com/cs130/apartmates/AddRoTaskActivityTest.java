package com.cs130.apartmates;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;

import com.cs130.apartmates.activities.AddRoTaskActivity;

/**
 * Created by Eric Du on 11/18/2015.
 */
public class AddRoTaskActivityTest extends ActivityInstrumentationTestCase2<AddRoTaskActivity> {

    private AddRoTaskActivity mActivity;
    private EditText mTitle;
    private EditText mDescription;
    private EditText mValue;
    private Button mCreateButton;
    private Button mCancelButton;

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
        mCreateButton = (Button) mActivity.findViewById(R.id.create);
        mCancelButton = (Button) mActivity.findViewById(R.id.cancel);

        testPreconditions();
    }

    public void testPreconditions() {
        assertNotNull("mActivity is null", mActivity);
        assertNotNull("mTitle is null", mTitle);
        assertNotNull("mDescription is null", mDescription);
        assertNotNull("mValue is null", mValue);
        assertNotNull("mCreateButton is null", mCreateButton);
        assertNotNull("mCancelButton is null", mCancelButton);
    }
}
