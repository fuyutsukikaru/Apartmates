package com.cs130.apartmates.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cs130.apartmates.R;
import com.cs130.apartmates.base.ApartmatesHttpClient;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by sjeongus on 11/17/15.
 */
public class GroupCreateActivity extends AppCompatActivity {

    private static final String createUrl = "/group/create";
    private static final String joinUrl = "/group/join";

    private ActionBar ab;
    private SharedPreferences pref;
    private long uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        long groupId = pref.getLong("groupId", 0);

        if (groupId > 0) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_create_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        Button create = (Button) findViewById(R.id.create);
        Button join = (Button) findViewById(R.id.join);

        final EditText nameText = (EditText) findViewById(R.id.groupName);
        final EditText passwordText = (EditText) findViewById(R.id.password);
        final EditText passwordCreateText = (EditText) findViewById(R.id.passwordCreate);
        final EditText stakesText = (EditText) findViewById(R.id.monthlyStakes);
        final EditText gidText = (EditText) findViewById(R.id.groupId);

        uId = pref.getLong("userId", 0);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String password = passwordCreateText.getText().toString();
                String stakes = stakesText.getText().toString();

                if (name.isEmpty() || password.isEmpty() || stakes.isEmpty()) {
                    Snackbar.make(findViewById(R.id.group_create), "You didn't fill all fields.", Snackbar.LENGTH_LONG).show();
                } else {
                    createGroup(uId, stakes, name, password);
                }
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gid = gidText.getText().toString();
                String password = passwordText.getText().toString();

                if (gid.isEmpty() || password.isEmpty()) {
                    Snackbar.make(findViewById(R.id.group_create), "You didn't fill all fields.", Snackbar.LENGTH_LONG).show();
                } else {
                    joinGroup(uId, gid, password);
                }
            }
        });
    }

    public void createGroup(long uid, String stakes, String name, String password) {
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("userId", Long.toString(uid));
            params.put("name", name);
            params.put("stakes", stakes);
            params.put("password", password);

            JSONObject resp = ApartmatesHttpClient.sendRequest(createUrl, params,
                    null, "POST");
            System.err.println("RESP: " + resp);
            if (resp.has("group_id")) {
                pref.edit().putLong("groupId", resp.getLong("group_id")).apply();
                pref.edit().putString("groupName", name).apply();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Snackbar.make(findViewById(R.id.group_create), "Something went wrong, please try again.", Snackbar.LENGTH_LONG).show();
    }

    public void joinGroup(long uid, String gid, String password) {
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("userId", Long.toString(uid));
            params.put("groupId", gid);
            params.put("password", password);

            JSONObject resp = ApartmatesHttpClient.sendRequest(joinUrl, params,
                    null, "POST");
            System.err.println("RESP: " + resp);

            if (resp.has("error")) {
                Snackbar.make(findViewById(R.id.group_create), "The password you entered was incorrect.", Snackbar.LENGTH_LONG).show();
                return;
            }

            pref.edit().putLong("groupId", Long.parseLong(gid)).apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Snackbar.make(findViewById(R.id.group_create), "Something went wrong, please try again.", Snackbar.LENGTH_LONG).show();
    }
}
