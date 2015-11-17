package com.cs130.apartmates.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.cs130.apartmates.R;
import com.cs130.apartmates.adapters.ViewPagerAdapter;
import com.cs130.apartmates.base.ApartmatesHttpClient;
import com.cs130.apartmates.fragments.BountyFragment;
import com.cs130.apartmates.fragments.MyTasksFragment;
import com.cs130.apartmates.fragments.RotationFragment;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by sjeongus on 11/13/15.
 */
public class MainActivity extends AppCompatActivity {

    private ViewPagerAdapter adapter;
    private DrawerLayout mDrawerLayout;
    private ActionBar ab;
    private ViewPager mViewPager;
    private NavigationView navigationView;
    private long mId;
    private int mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        mId = prefs.getLong("userId", 1);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int id = 0;
                switch (position) {
                    case 0:
                        id = R.id.nav_my_tasks;
                        ab.setTitle(adapter.getPageTitle(0));
                        mPosition = 0;
                        break;
                    case 1:
                        id = R.id.nav_rotation;
                        ab.setTitle(adapter.getPageTitle(1));
                        mPosition = 1;
                        break;
                    case 2:
                        id = R.id.nav_bounty;
                        ab.setTitle(adapter.getPageTitle(2));
                        mPosition = 2;
                        break;
                    default:
                        break;
                }
                navigationView.setCheckedItem(id);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new MyTasksFragment(), "My Tasks");
        adapter.addFrag(new RotationFragment(), "Rotation Tasks");
        adapter.addFrag(new BountyFragment(), "Bounty Tasks");
        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {
                            case R.id.nav_my_tasks:
                                mViewPager.setCurrentItem(0, true);
                                ab.setTitle(adapter.getPageTitle(0));
                                mPosition = 0;
                                break;
                            case R.id.nav_rotation:
                                mViewPager.setCurrentItem(1, true);
                                ab.setTitle(adapter.getPageTitle(1));
                                mPosition = 1;
                                break;
                            case R.id.nav_bounty:
                                mViewPager.setCurrentItem(2, true);
                                ab.setTitle(adapter.getPageTitle(2));
                                mPosition = 2;
                                break;
                            default:
                                break;
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String title = intent.getStringExtra("task_title");
            int value = intent.getIntExtra("task_value", 0);
            String deadline = intent.getStringExtra("task_deadline");
            String details = intent.getStringExtra("task_details");

            BountyFragment frag = (BountyFragment) adapter.getItem(0);
            frag.addTask(deadline, title, value, details);
        }
    }

    public void addTask(View view) {
        Intent intent;
        switch(mPosition) {
            case 0:
                intent = new Intent(this, AddTaskActivity.class);
                break;
            case 1:
                intent = new Intent(this, AddRoTaskActivity.class);
                break;
            case 2:
                intent = new Intent(this, AddTaskActivity.class);
                break;
            default:
                intent = new Intent(this, AddTaskActivity.class);
                break;
        }
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tasks, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
