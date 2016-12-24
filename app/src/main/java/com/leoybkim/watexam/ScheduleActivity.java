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
        ArrayList<Schedule> schedules = new ArrayList<>();
        schedules.add(new Schedule("ECE 318", "2016-04-08", "MC 4059,4060,4061", "12:30 PM", "3:00 PM"));
        schedules.add(new Schedule("ECE 390", "2016-04-13", "RCH 301,302", "12:30 PM", "3:00 PM"));
        schedules.add(new Schedule("ECE 356", "2016-04-22", "DC 1350", "9:00 AM", "11:30 AM"));
        schedules.add(new Schedule("ECE 358", "2016-04-19", "PAC 6", "4:00 PM", "6:30 PM"));
        schedules.add(new Schedule("PHYS 233", "2016-04-23", "PHY 308", "4:00 PM", "6:30 PM"));

        // Find view from activity_schedule.xml
        ListView scheduleListView = (ListView) findViewById(R.id.list);

        // Create and set adapter that takes the schedules list and populate to the ListView
        ScheduleAdapter adapter = new ScheduleAdapter(this, schedules);
        scheduleListView.setAdapter(adapter);
    }
}
