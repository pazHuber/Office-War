package com.example.paz.officewar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class ScoreTableActivity extends Activity {
    private Button btnViewUsers;
    private Button btnNewUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_table);
        Firebase myFirebaseRef = new Firebase("https://blazing-heat-7486.firebaseio.com/");
        myFirebaseRef.child("message").setValue("Do you have data? You'll love Firebase.");
        myFirebaseRef.child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
            }
            @Override public void onCancelled(FirebaseError error) { }
        });
        // Buttons
        btnViewUsers = (Button) findViewById(R.id.act_score_table_btnViewUserss);
        btnNewUser = (Button) findViewById(R.id.act_score_table_btnCreateUser);
        // view products click event
        btnViewUsers.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), AllUsersActivity.class);
                startActivity(i);

            }
        });
        // view products click event
        btnNewUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching create new product activity
                Intent i = new Intent(getApplicationContext(), NewUserActivity.class);
                startActivity(i);

            }
        });
    }
}
