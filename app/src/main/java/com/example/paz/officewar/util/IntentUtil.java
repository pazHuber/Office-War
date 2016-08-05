package com.example.paz.officewar.util;

/**
 * Created by paz on 25/04/16.
 */
import android.app.Activity;
import android.content.Intent;

import com.example.paz.officewar.ui.AccessTokenActivity;

/**
 * Created by kirkita on 06.10.15.
 */
public class IntentUtil {

    private Activity activity;

    // constructor
    public IntentUtil(Activity activity) {
        this.activity = activity;
    }

    public void showAccessToken() {
        Intent i = new Intent(activity, AccessTokenActivity.class);
        activity.startActivity(i);
    }
}