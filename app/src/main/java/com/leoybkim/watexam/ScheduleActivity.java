package com.leoybkim.watexam;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

    private List<Schedule> mSchedules;

    // Arbitrary position when no item has been selected
    private int mSelectedItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // Find view from activity_schedule.xml
        ListView scheduleListView = (ListView) findViewById(R.id.list);

        // Create and set adapter that takes the schedules list and populate to the ListView
        mAdapter = new ScheduleAdapter(this, new ArrayList<Schedule>());
        scheduleListView.setAdapter(mAdapter);

        // Use loader manager to put data processing on background thread
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(SCHEDULE_LOADER_ID, null, this);

        // Highlight item on touch
        scheduleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.setSelection(position, false);
            }
        });

        scheduleListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.v(LOG_TAG, "Long clicked!");
                mAdapter.setSelection(position, true);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Create search bar on ActionBar
        getMenuInflater().inflate(R.menu.menu_schedule_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.search_hint));

        // Filter items with search keyword
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Reset SearchView
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                // Set activity title to search query
                ScheduleActivity.this.setTitle(query);
                mAdapter.filter(query, mSchedules);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String searchKeyword) {
                mAdapter.filter(searchKeyword, mSchedules);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
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

        // Saves copy for searching later
        mSchedules = schedules;
    }

    @Override
    public void onLoaderReset(Loader<List<Schedule>> loader) {
        mAdapter.clear();
    }
}
