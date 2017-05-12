package com.leoybkim.watexam.Activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.leoybkim.watexam.Adapters.ScheduleAdapter;
import com.leoybkim.watexam.Adapters.ScheduleCursorAdapter;
import com.leoybkim.watexam.Data.ScheduleContract;
import com.leoybkim.watexam.Fragments.EditTermFragment;
import com.leoybkim.watexam.Models.Schedule;
import com.leoybkim.watexam.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by leo on 31/12/16.
 */

public class BookmarkActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Arbitrary number
    private static final int SCHEDULE_LOADER = 0;
    private static final String LOG_TAG = BookmarkActivity.class.getSimpleName();
    private ScheduleCursorAdapter mCursorAdapter;
    private ScheduleAdapter mAdapter;
    private Uri mCurrentScheduleUri;
    private static final String TERM_API_URL="https://api.uwaterloo.ca/v2/terms/list.json?key=dda487cc76cfe50f8c339eb03866ad91";

    // Term organization
    private HashMap<String, String> mTerms = new HashMap<String, String>();
    private String currentTermSelection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        final ListView scheduleListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        scheduleListView.setEmptyView(emptyView);

        mAdapter = new ScheduleAdapter(this, new ArrayList<Schedule>());

        mCursorAdapter = new ScheduleCursorAdapter(this, null);
        scheduleListView.setAdapter(mCursorAdapter);

        getTermList();

        getLoaderManager().initLoader(SCHEDULE_LOADER, null, this);

        scheduleListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                // listView.getChildAt(i) works where 0 is the first visible row and (n-1) is the last visible row
                int relativePosition = position - adapterView.getFirstVisiblePosition();
                TextView tv = (TextView) adapterView.getChildAt(relativePosition).findViewById(R.id.class_code);
                String classCode = tv.getText().toString();
                deleteSchedule(classCode);
                return false;
            }
        });

        // Setup FAB to open ScheduleActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookmarkActivity.this, ScheduleActivity.class);
                if (currentTermSelection == null) {
                    currentTermSelection = mTerms.get("current_term");
                }
                intent.putExtra("termCode", currentTermSelection);
                startActivity(intent);
            }
        });
    }

    private void getTermList() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(TERM_API_URL).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(BookmarkActivity.this, "OkHttpClient error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    // Clear pre-existing array list to avoid duplication
                    if (mTerms != null) { mTerms.clear(); }

                    // Parse json results
                    JSONObject responseJson = new JSONObject(response.body().string());

                    // Save on hashmap
                    mTerms.put("previous_term", responseJson.getJSONObject("data").getString("previous_term"));
                    mTerms.put("current_term", responseJson.getJSONObject("data").getString("current_term"));
                    mTerms.put("next_term", responseJson.getJSONObject("data").getString("next_term"));

                    // Save to shared preference
                    // TODO: need to refactor this later
//                    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPref.edit();
//                    editor.putString("previous_term", responseJson.getJSONObject("data").getString("previous_term"));
//                    editor.putString("current_term", responseJson.getJSONObject("data").getString("current_term"));
//                    editor.putString("next_term", responseJson.getJSONObject("data").getString("next_term"));
//                    editor.commit();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Adapter on UI Tread to update the data
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update adapter
                    }
                });
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
            case R.id.action_previous_term:
                currentTermSelection = mTerms.get("previous_term");
                return true;
            case R.id.action_current_term:
                currentTermSelection = mTerms.get("current_term");
                return true;
            case R.id.action_next_term:
                currentTermSelection = mTerms.get("next_term");
                return true;
        }
        Log.d(LOG_TAG, currentTermSelection);
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EditTermFragment alertDialog = EditTermFragment.newInstance("Edit term");
        alertDialog.show(fm, "fragment_alert");
    }

    private void deleteAllSchedules() {
        getContentResolver().delete(ScheduleContract.ScheduleEntry.CONTENT_URI, null, null);
    }

    private void deleteSchedule(String classCode) {
        if(mCurrentScheduleUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentScheduleUri, null, null);

        } else {
            int rowsDeleted = getContentResolver().delete(ScheduleContract.ScheduleEntry.CONTENT_URI, "class="+"'"+classCode+"'", null);

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
