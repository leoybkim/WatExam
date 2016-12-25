package com.leoybkim.watexam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    // Used for logging
    private static final String LOG_TAG = ScheduleActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // Temporary data
        ArrayList<Schedule> schedules = QueryUtils.extractSchedule();

        // Find view from activity_schedule.xml
        ListView scheduleListView = (ListView) findViewById(R.id.list);

        // Create and set adapter that takes the schedules list and populate to the ListView
        ScheduleAdapter adapter = new ScheduleAdapter(this, schedules);
        scheduleListView.setAdapter(adapter);
    }
}
