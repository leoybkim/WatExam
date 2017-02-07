package com.leoybkim.watexam;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.leoybkim.watexam.Data.ScheduleContract;

/**
 * Created by leo on 06/02/17.
 */

public class ScheduleCursorAdapter extends CursorAdapter{

    public ScheduleCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.schedule_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find view that needs to be modified
        Log.d("CursorAdapter", "Called!!1");
        TextView classCodeTextView = (TextView) view.findViewById(R.id.class_code);
        TextView dateTextView = (TextView) view.findViewById(R.id.date);
        TextView locationTextView = (TextView) view.findViewById(R.id.location);
        TextView timeTextView = (TextView) view.findViewById(R.id.time);

        // Find columns of exam attributes of interest
        int classCodeColumnIndex = cursor.getColumnIndex(ScheduleContract.ScheduleEntry.COLUMN_EXAM_CLASS);
        int locationColumnIndex = cursor.getColumnIndex(ScheduleContract.ScheduleEntry.COLUMN_EXAM_LOCATION);
        int dateColumnIndex = cursor.getColumnIndex(ScheduleContract.ScheduleEntry.COLUMN_EXAM_DATE);
        int startTimeColumnIndex = cursor.getColumnIndex(ScheduleContract.ScheduleEntry.COLUMN_EXAM_START_TIME);
        int endTimeColumnIndex = cursor.getColumnIndex(ScheduleContract.ScheduleEntry.COLUMN_EXAM_END_TIME);

        // Read the exam attribute from the cursor of the current schedule
        String scheduleClassCode = cursor.getString(classCodeColumnIndex);
        String scheduleDate = cursor.getString(dateColumnIndex);
        String scheduleLocation = cursor.getString(locationColumnIndex);
        String scheduleStartTime = cursor.getString(startTimeColumnIndex);
        String scheduleEndTime = cursor.getString(endTimeColumnIndex);
        String scheduleTime = scheduleStartTime + " - " + scheduleEndTime;

        if (TextUtils.isEmpty(scheduleDate)) {
            scheduleDate = context.getString(R.string.unknown);
        } else if (TextUtils.isEmpty(scheduleLocation)) {
            scheduleLocation = context.getString(R.string.unknown);
        } else if (TextUtils.isEmpty(scheduleTime)) {
            scheduleTime = context.getString(R.string.unknown);
        }

        // Set TextView
        classCodeTextView.setText(scheduleClassCode);
        dateTextView.setText(scheduleDate);
        locationTextView.setText(scheduleLocation);
        timeTextView.setText(scheduleTime);
    }
}
