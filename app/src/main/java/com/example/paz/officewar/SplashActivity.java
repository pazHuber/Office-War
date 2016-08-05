package com.example.paz.officewar;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.Firebase;

import org.opencv.core.Core;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends ActionBarActivity {

    private int count;
    private int i;
    public SplashActivity() {
        super();
        count = 0;
        i=0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_splash);
        final TextView text = (TextView)findViewById(R.id.act_splash_presentage_text);
        final ProgressBar pb = (ProgressBar)findViewById(R.id.act_splash_progress_bar);
        final TextView msgToUser = (TextView)findViewById(R.id.act_splash_msg_to_user_text);
        final List<String> textLst = new ArrayList<String>(5);
        textLst.add("Here");
        textLst.add("Here we");
        textLst.add("Here we go");
        textLst.add("Here we go.");
        textLst.add("Here we go..");
        textLst.add("Here we go...");
        setActionBar();
        new Thread(new Runnable() {
            public void run() {
                try {
                    while (count <= 100) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb.setProgress(count);
                                text.setText(Integer.toString(count) + "%");
                                if (count % 17 == 0) {
                                    msgToUser.setText(textLst.get(i));
                                    i++;
                                }
                            }
                        });
                        Thread.sleep(50);
                        count++;
                    }
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                } catch (Exception e) {
                }
                ;

            }

        }).start();

    }
    private void setActionBar() {
        ActionBar mAction = getSupportActionBar();
        mAction.hide();
    }
}
