package com.leoybkim.watexam;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.leoybkim.watexam.Data.ScheduleContract;


/**
 * Created by leo on 31/12/16.
 */

public class BookmarkActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Arbitrary number
    private static final int SCHEDULE_LOADER = 0;
    private final static String LOG_TAG = BookmarkActivity.class.getSimpleName();
    ScheduleCursorAdapter mCursorAdapter;

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

        mCursorAdapter = new ScheduleCursorAdapter(this, null);
        scheduleListView.setAdapter(mCursorAdapter);


        TextView tv = (TextView) findViewById(R.id.class_code);
        if (tv != null) {

            String stuff = tv.toString();
            Log.d(LOG_TAG, stuff);
        }
        getLoaderManager().initLoader(SCHEDULE_LOADER, null, this);
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
}
