package com.cs130.apartmates.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cs130.apartmates.R;
import com.cs130.apartmates.adapters.ViewPagerAdapter;
import com.cs130.apartmates.fragments.BaseFragment;
import com.cs130.apartmates.fragments.BountyFragment;
import com.cs130.apartmates.fragments.MyTasksFragment;
import com.cs130.apartmates.fragments.RotationFragment;
import com.cs130.apartmates.services.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.squareup.picasso.Picasso;

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
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private SharedPreferences pref;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    private static final String REGISTRATION_COMPLETE = "registrationComplete";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        mId = pref.getLong("userId", 0);
        long groupId = pref.getLong("groupId", 0);

        if (mId == 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (groupId == 0) {
            Intent intent = new Intent(this, GroupCreateActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

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

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(SENT_TOKEN_TO_SERVER, false);
            }
        };

        if (checkGooglePlayServices()) {
            // Start IntentService to register this application with GCM.
            System.err.println("Starting RegistrationIntentService");
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private boolean checkGooglePlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new MyTasksFragment(), "My Tasks");
        adapter.addFrag(new RotationFragment(), "Rotation Tasks");
        adapter.addFrag(new BountyFragment(), "Bounty Tasks");
        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        LinearLayout header = (LinearLayout) navigationView.inflateHeaderView(R.layout.drawer_header);
        TextView name = (TextView) header.findViewById(R.id.name);
        name.setText(pref.getString("userName", "Username"));
        TextView groupName = (TextView) header.findViewById(R.id.group_name);
        groupName.setText(Long.toString(pref.getLong("groupId", 0)));
        ImageView pic = (ImageView) header.findViewById(R.id.profile_pic);
        Picasso.with(getBaseContext()).load(pref.getString("userPic", null)).into(pic);
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
                            case R.id.group_page:
                                Intent intent = new Intent(MainActivity.this, GroupInfoActivity.class);
                                startActivity(intent);
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

            BaseFragment frag = (BaseFragment) adapter.getItem(mPosition);
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
