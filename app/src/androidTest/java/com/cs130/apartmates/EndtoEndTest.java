package com.cs130.apartmates;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.preference.PreferenceActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.test.InstrumentationTestCase;
import android.test.TouchUtils;
import android.text.method.Touch;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.cs130.apartmates.activities.AddTaskActivity;
import com.cs130.apartmates.activities.MainActivity;

public class EndtoEndTest extends InstrumentationTestCase {

    public void testCreateBountyTask() {

        Instrumentation instr = getInstrumentation();
        Instrumentation.ActivityMonitor monitor = instr.addMonitor(MainActivity.class.getName(), null, false);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(instr.getTargetContext(), MainActivity.class.getName());
        instr.startActivitySync(intent);

        Activity curActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5);
        assertNotNull("MainActivity unable to start", curActivity);

        View addTaskButton = curActivity.findViewById(R.id.add_task);
        assertNotNull("Could not find add task button", addTaskButton);
        TouchUtils.clickView(this, addTaskButton); //click to get rid of popup

        final Activity ac = curActivity;
        final InstrumentationTestCase itc = this;
        curActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DrawerLayout drawer = (DrawerLayout) ac.findViewById(R.id.drawer_layout);
                drawer.openDrawer(Gravity.LEFT);
                NavigationView nv = (NavigationView) ac.findViewById(R.id.nav_view);
                nv.setCheckedItem(R.id.nav_bounty);
                drawer.closeDrawers();
            }
        });

        instr.removeMonitor(monitor);
        monitor = instr.addMonitor(AddTaskActivity.class.getName(), null, false);

        TouchUtils.clickView(this, addTaskButton);
        curActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5);
        assertNotNull("AddTaskActivity unable to start", curActivity);

        putTextIntoField(curActivity.findViewById(R.id.title), "Test Task", instr);
        putTextIntoField(curActivity.findViewById(R.id.description), "Test description", instr);
        putTextIntoField(curActivity.findViewById(R.id.value), "5", instr);
        putTextIntoField(curActivity.findViewById(R.id.deadline), "2016-11-25", instr);

        View createButton = curActivity.findViewById(R.id.create);
        assertNotNull("Could not find create button", createButton);

        instr.removeMonitor(monitor);
        monitor = instr.addMonitor(MainActivity.class.getName(), null, false);

        TouchUtils.clickView(this, createButton);
        curActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5);
        assertNotNull("MainActivity unable to start", curActivity);
    }

    private void putTextIntoField(View v, String s, Instrumentation instr) {
        assertNotNull(v);
        TouchUtils.clickView(this, v);
        instr.sendStringSync(s);
    }
}
