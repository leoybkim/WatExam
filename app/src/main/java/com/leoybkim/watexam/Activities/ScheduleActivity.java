package com.leoybkim.watexam.Activities;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.DialogInterface;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.leoybkim.watexam.Adapters.ScheduleAdapter;
import com.leoybkim.watexam.BuildConfig;
import com.leoybkim.watexam.Loaders.ScheduleLoader;
import com.leoybkim.watexam.Models.Schedule;
import com.leoybkim.watexam.Models.Term;
import com.leoybkim.watexam.R;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity implements LoaderCallbacks <List <Schedule>> {

    // Used for logging
    private static final String LOG_TAG = ScheduleActivity.class.getName();

    private static final String EXAM_API_URL_ROOT ="https://api.uwaterloo.ca/v2/terms/";
    private static final String EXAM_API_URL_APPEND ="/examschedule.json?key=" + BuildConfig.WAT_API_KEY;

    // Adpater for list of exam schedules
    private ScheduleAdapter mAdapter;

    // Constant value for schedule and term loader id
    private static final int SCHEDULE_LOADER_ID = 1;
    private static final int TERM_LOADER_ID = 2;

    private List<Schedule> mSchedules;
    private List<Term> mTerms;

    // Arbitrary position when no item has been selected
    private int mSelectedItem = -1;

    private SwipeRefreshLayout swipeContainer;

    private LoaderManager mLoaderManager;

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
        mLoaderManager = getLoaderManager();
        mLoaderManager.initLoader(SCHEDULE_LOADER_ID, null, this);
        mLoaderManager.initLoader(TERM_LOADER_ID, null, this);

        // Pull down the screen to reload schedule
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
                swipeContainer.setRefreshing(false);
            }
        });
        // Refreshing arrow colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

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

    public void reload() {
        getLoaderManager().restartLoader(0, null, this);
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
                if (mSchedules!=null) {
                    mAdapter.filter(searchKeyword, mSchedules);
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public Loader<List<Schedule>> onCreateLoader(int i, Bundle bundle) {
        String termCode = getIntent().getExtras().getString("termCode");
        return new ScheduleLoader(this, EXAM_API_URL_ROOT + termCode + EXAM_API_URL_APPEND);
    }

    @Override
    public void onLoadFinished(Loader<List<Schedule>> loader, List<Schedule> schedules) {
        mAdapter.clear();
        if (schedules != null && !schedules.isEmpty()) {
            mAdapter.addAll(schedules);
        }
        else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder window = new AlertDialog.Builder(ScheduleActivity.this);
                    window.setMessage("The exam schedule is not out yet!");
                    window.setCancelable(true);

                    window.setPositiveButton(
                            "Go Back",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                }
                            });

                    window.create().show();
                }
            });
        }

        // Saves copy for searching later
        mSchedules = schedules;
    }

    @Override
    public void onLoaderReset(Loader<List<Schedule>> loader) {
        mAdapter.clear();
    }
}
