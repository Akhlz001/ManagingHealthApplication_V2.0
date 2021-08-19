/**
 * @author	Copyright (c) 2013 Jessica Yuen <jyuen@ualberta.ca>
 *
 * Permission to use, copy, modify, and distribute this software
 * is granted, provided that the above copyright notice appear
 * in all copies.
 */

/**
 * First activity upon application load - displays the summaries of
 * the calories log.
 */
package com.example.managinghealthapplicationv1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CalorieCounter extends Activity {

    private CalorieSummary calSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caloriecounter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveCalorieLog();
        displayCalorieSummary();
    }

    private void retrieveCalorieLog() {
        LogManager.loadCalorieLog(this);
    }

    private void displayCalorieSummary() {
        calSum = new CalorieSummary(LogManager.getLogEntries());
        calSum.display(this);
    }

    /** Listener for open calorie log button **/
    public void openCalorieLog(View view) {
        Intent intent = new Intent(this, DisplayLogActivity.class);
        startActivity(intent);
    }

    public void quit(View view) {
        finish();
    }
}

