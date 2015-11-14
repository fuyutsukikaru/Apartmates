package com.cs130.apartmates.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.cs130.apartmates.R;

public class GroupInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_info);

        TextView groupName = (TextView) findViewById(R.id.displayGroupName);
        //TextView groupDescription = (TextView) findViewById(R.id.displayGroupDescription);
        TextView groupID = (TextView) findViewById(R.id.displayGroupID);
        TextView memberNames = (TextView) findViewById(R.id.displayMemberNames);

        groupName.setText("Apt 306");
        //groupDescription.setText("Lovely people of Cedarwood #306!");
        groupID.setText("GroupID: 123456");
        memberNames.setText("Jade\nSarah\nViolet\nJordan"); //can consider adding their Venmo profile pics later
    }
    // need to figure out how to leave group
}
