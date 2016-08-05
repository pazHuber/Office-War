package com.example.paz.officewar;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView robotAnimation;
    private  String uid;
    private String uName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uid = getIntent().getExtras().getString("USER_ID");
        uName = getIntent().getExtras().getString("USER_NAME");
        Button playBtn = (Button) findViewById(R.id.act_main_play_btn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                i.putExtra("USER_ID", uid);
                i.putExtra("USER_NAME", uName);
                startActivity(i);
            }
        });
        Button highScoreTable = (Button) findViewById(R.id.act_main_high_score_table_btn);
        highScoreTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ScoreTableActivity.class);
                startActivity(i);
            }
        });
        robotAnimation = (ImageView) findViewById(R.id.act_main_animation_robot);
        robotAnimation.post(new Runnable() {
            @Override
            public void run() {
                ((AnimationDrawable) robotAnimation.getBackground()).start();
            }
        });
    }
}
