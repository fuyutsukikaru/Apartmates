package com.cs130.apartmates.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.cs130.apartmates.R;
import com.cs130.apartmates.base.ApartmatesHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GroupInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_info);

        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        long gid = prefs.getLong("groupId", 0);

        TextView groupName = (TextView) findViewById(R.id.displayGroupName);
        //TextView groupDescription = (TextView) findViewById(R.id.displayGroupDescription);
        TextView groupID = (TextView) findViewById(R.id.displayGroupID);
        TextView memberNames = (TextView) findViewById(R.id.displayMemberNames);
        TextView stakes = (TextView) findViewById(R.id.displayStakes);

        JSONObject resp = ApartmatesHttpClient.sendRequest("/group?groupId=" + gid, null, null, "GET");
        if (resp != null && resp.has("name")) {
            try {
                groupName.setText(resp.getString("name"));
                //groupDescription.setText("Lovely people of Cedarwood #306!");
                groupID.setText("GroupID: " + gid);
                stakes.setText(resp.getString("stakes"));
                JSONArray jNames = resp.getJSONArray("users");
                System.err.println("GroupInfoActivity: " + jNames.toString());
                String names = "";
                for (int i = 0; i < jNames.length(); i++) {
                    String uid = jNames.get(i).toString();
                    JSONObject users = ApartmatesHttpClient.sendRequest("/user?userId=" + uid, null, null, "GET");
                    if (users != null && users.has("first_name")) {
                        try {
                            String name = users.getString("first_name") + " " + users.getString("last_name") + "\n";
                            names += name;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                memberNames.setText(names); //can consider adding their Venmo profile pics later
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    // need to figure out how to leave group
}
