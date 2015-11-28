package com.cs130.apartmates;

import android.graphics.Point;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.test.ActivityInstrumentationTestCase2;
import com.cs130.apartmates.activities.MainActivity;
import com.robotium.solo.Solo;

/**
 * Created by Eric Du on 11/18/2015.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mActivity;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ViewPager mViewPager;
    private Solo solo;

    private int width;
    private int height;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mActivity = getActivity();
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) mActivity.findViewById(R.id.nav_view);
        mViewPager = (ViewPager) mActivity.findViewById(R.id.view_pager);
        solo = new Solo(getInstrumentation(), getActivity());

        Point size = new Point();
        mActivity.getWindowManager().getDefaultDisplay().getSize(size);
        width = size.x;
        height = size.y;
    }

    public void testAllPreconditions() {
        solo.sendKey(Solo.ENTER);

        assertNotNull("mActivity is null", mActivity);
        assertNotNull("mToolbar is null", mToolbar);
        assertNotNull("mDrawerLayout is null", mDrawerLayout);
        assertNotNull("mNavigationView is null", mNavigationView);
        assertNotNull("mViewPager is null", mViewPager);

        solo.finishOpenedActivities();
    }

    public void testSwiping() {
        solo.sendKey(Solo.ENTER);

        float startX = width * 9 / 10;
        float endX = width / 10;
        float startY = height / 2;

        //Drag left
        solo.drag(startX, endX, startY, startY, 40);
        solo.drag(startX, endX, startY, startY, 40);

        //Drag right
        solo.drag(endX, startX, startY, startY, 40);
        solo.drag(endX, startX, startY, startY, 40);

        solo.finishOpenedActivities();
    }

    public void testDrawerMyTasks() {
        try {
            solo.sendKey(Solo.ENTER);

            float startX = width * 9 / 10;
            float endX = width / 10;
            float startY = height / 2;
            solo.drag(startX, endX, startY, startY, 40);
            //Opens the drawer by dragging the edge to the right

            solo.drag(0, width, startY, startY, 40);

            solo.clickOnMenuItem("My Tasks");
            solo.clickOnImageButton(1);

            solo.waitForActivity("AddTaskActivity", 5000);
            assertTrue(solo.searchText("Add New Bounty Task")); // Search for add bounty task
            assertTrue(solo.searchText("Create")); //Search for create button text
            assertTrue(solo.searchText("Cancel")); //Search for cancel button text
            Thread.sleep(500);
        } catch (Exception e) {

        }
        solo.finishOpenedActivities();
    }

    public void testDrawerBountyTasks() {
        try {
            solo.sendKey(Solo.ENTER);

            //Faster way to open the drawer
            solo.clickOnImageButton(0);
            solo.clickOnMenuItem("Bounty Tasks");
            solo.clickOnImageButton(1);
            solo.waitForActivity("AddTaskActivity", 5000);
            assertTrue(solo.searchText("Add New Bounty Task")); // Search for add bounty task
            assertTrue(solo.searchText("Create")); //Search for create button text
            assertTrue(solo.searchText("Cancel")); //Search for cancel button text
            Thread.sleep(500);
        } catch (Exception e) {

        }
        solo.finishOpenedActivities();
    }

    public void testDrawerGroupInfo() {
        try {
            solo.sendKey(Solo.ENTER);

            solo.clickOnImageButton(0);
            solo.clickOnMenuItem("Group Info");
            solo.waitForActivity("GroupInfoActivity", 5000);
            assertTrue(solo.searchText("Leave Group")); //Should find a leave group button
            Thread.sleep(500);
        } catch (Exception e) {

        }
        solo.finishOpenedActivities();
    }

    public void testDrawerRotationTasks() {

        try {
            solo.sendKey(Solo.ENTER);

            solo.clickOnImageButton(0);

            //Not sure why this statement takes a while to execute
            solo.clickOnMenuItem("Rotation Tasks");
            assertTrue(solo.searchText("Rotation Tasks"));
            solo.clickOnImageButton(1);
            solo.waitForActivity("AddRoTaskActivity", 5000);
            assertTrue(solo.searchText("Add New Rotation Task")); // Search for add rotation task
            assertTrue(solo.searchText("Create")); //Search for create button text
            assertTrue(solo.searchText("Cancel")); //Search for cancel button text
            Thread.sleep(1000);
        } catch (Exception e) {

        }
        solo.finishOpenedActivities();
    }
}
