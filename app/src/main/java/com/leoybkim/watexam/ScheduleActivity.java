package com.leoybkim.watexam;

import android.content.Loader;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity implements LoaderCallbacks <List <Schedule>> {

    // Used for logging
    private static final String LOG_TAG = ScheduleActivity.class.getName();

    private static final String UWATERLOO_API_URL="https://api.uwaterloo.ca/v2/terms/1161/examschedule.json?key=dda487cc76cfe50f8c339eb03866ad91";

    // Adpater for list of exam schedules
    private ScheduleAdapter mAdapter;

    // Constant value for schedule loader id
    private static final int SCHEDULE_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // Find view from activity_schedule.xml
        ListView scheduleListView = (ListView) findViewById(R.id.list);

        // Create and set adapter that takes the schedules list and populate to the ListView
        mAdapter = new ScheduleAdapter(this, new ArrayList<Schedule>());
        scheduleListView.setAdapter(mAdapter);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(SCHEDULE_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Schedule>> onCreateLoader(int i, Bundle bundle) {
        return new ScheduleLoader(this, UWATERLOO_API_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Schedule>> loader, List<Schedule> schedules) {
        mAdapter.clear();
        if (schedules != null && !schedules.isEmpty()) {
            mAdapter.addAll(schedules);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Schedule>> loader) {
        mAdapter.clear();
    }
}
