package com.leoybkim.watexam;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.leoybkim.watexam.Data.ScheduleContract;

import java.util.ArrayList;


/**
 * Created by leo on 31/12/16.
 */

public class BookmarkActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Arbitrary number
    private static final int SCHEDULE_LOADER = 0;
    private final static String LOG_TAG = BookmarkActivity.class.getSimpleName();
    private ScheduleCursorAdapter mCursorAdapter;
    private ScheduleAdapter mAdapter;
    private Uri mCurrentScheduleUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        // Setup FAB to open ScheduleActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookmarkActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });

        ListView scheduleListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        scheduleListView.setEmptyView(emptyView);

        mAdapter = new ScheduleAdapter(this, new ArrayList<Schedule>());

        mCursorAdapter = new ScheduleCursorAdapter(this, null);
        scheduleListView.setAdapter(mCursorAdapter);


        TextView tv = (TextView) findViewById(R.id.class_code);
        if (tv != null) {

            String stuff = tv.toString();
            Log.d(LOG_TAG, stuff);
        }

        getLoaderManager().initLoader(SCHEDULE_LOADER, null, this);

        scheduleListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.v(LOG_TAG, "Long clicked!");
                deleteSchedule();
                return false;
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ScheduleContract.ScheduleEntry._ID,
                ScheduleContract.ScheduleEntry.COLUMN_EXAM_CLASS,
                ScheduleContract.ScheduleEntry.COLUMN_EXAM_LOCATION,
                ScheduleContract.ScheduleEntry.COLUMN_EXAM_DATE,
                ScheduleContract.ScheduleEntry.COLUMN_EXAM_START_TIME,
                ScheduleContract.ScheduleEntry.COLUMN_EXAM_END_TIME };

        // This Loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,
                ScheduleContract.ScheduleEntry.CONTENT_URI, // The content
                projection,                       // The column to return for each row
                null,                             // Selection criteria
                null,                             // Selection criteria
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_delete_all_entries:
                deleteAllSchedules();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllSchedules() {
        getContentResolver().delete(ScheduleContract.ScheduleEntry.CONTENT_URI, null, null);
    }

    private void deleteSchedule() {
        Log.d(LOG_TAG, "deleteSchedule()");

        if(mCurrentScheduleUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentScheduleUri, null, null);
            Log.d(LOG_TAG, "deleted row");
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_schedule_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_schedule_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
