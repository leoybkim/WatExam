package com.leoybkim.watexam;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by leo on 24/12/16.
 *
 * ScheduleAdapter is a custom Adapter created to allow more than one TextView to be linked the ListView
 */

public class ScheduleAdapter extends ArrayAdapter<Schedule> {

    private static final String LOG_TAG = ScheduleAdapter.class.getName();
    private List<Schedule> mSchedules = null;
    private ArrayList<Schedule> mArrayList;

    private static final int NOT_SELECTED = -1;
    private int selectedPosition = NOT_SELECTED;
    private boolean empty = true;

    // Constructor
    public ScheduleAdapter (Context context, List<Schedule> schedules) {
        super(context, 0, schedules);
        this.mSchedules = schedules;
        this.mArrayList = new ArrayList<>();
        this.mArrayList.addAll(schedules);
    }

    // Toggle if position is selected again
    public void setSelection(int position, boolean longClick) {

        if (selectedPosition == position) {
            //selectedPosition = NOT_SELECTED;
        } else {
            selectedPosition = position;
            if (longClick) {
                Log.d(LOG_TAG, String.valueOf(longClick));
                empty = !empty; // toggle star
                // TODO: the star fills up slowly from user's side because it creates empty star first
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if there is an exisiting list item view (convertView) that can be reused
        // Otherwise inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.schedule_list_item, parent, false);
        }

        // Find schedule at the given position in the list of schedules
        Schedule currentSchedule = getItem(position);

        // Find view from schedule_list_item.xml
        TextView classCodeView = (TextView) listItemView.findViewById(R.id.class_code);
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        TextView locationView = (TextView) listItemView.findViewById(R.id.location);
        TextView startTimeView = (TextView) listItemView.findViewById(R.id.time);

        // Format string and display to TextView
        classCodeView.setText(currentSchedule.getClassCode());
        dateView.setText(formatDate(currentSchedule.getDate()));
        locationView.setText(currentSchedule.getLocation());
        startTimeView.setText(formatTime(currentSchedule.getStartTime(), currentSchedule.getEndTime()));

        ImageView star = (ImageView) listItemView.findViewById(R.id.star);



        //View view = super.getView(position, convertView, parent);
        if (position == selectedPosition) {
            // Selected item
            //listItemView.setBackgroundColor(Color.YELLOW);
            if (star.getVisibility() == View.INVISIBLE) {
                Log.v(LOG_TAG, "if inivisible");
                star.setVisibility(View.VISIBLE);
                empty = true;
            } else if (star.getVisibility() == View.VISIBLE && empty) {
                Log.v(LOG_TAG, "if visible and empty");
                empty = false;

                star.setImageResource(R.drawable.ic_star_black_24dp);
            } else if (star.getVisibility() == View.VISIBLE && !empty){
                Log.v(LOG_TAG, "if visible and full");
                star.setImageResource(R.drawable.ic_star_border_black_24dp);
                star.setVisibility(View.INVISIBLE);
            }

        } else {
            // Unselected item
            //istItemView.setBackgroundColor(Color.WHITE);
            star.setVisibility(View.INVISIBLE);
        }

        return listItemView;
    }

    // Date formatter
    // Return String "Unknown" if date is null or before epoch time (1970-01-01)
    public String formatDate(String dateString) {
        Date parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString, new ParsePosition(0));
        if (parsedDate != null && !dateString.equals("1969-12-31")) {
            return new SimpleDateFormat("LLL dd, yyyy").format(parsedDate);
        } else {
            return "Unknown";
        }
    }

    public String formatTime(String startTime, String endTime) {
        if (startTime != null && !startTime.isEmpty() && endTime != null && !endTime.isEmpty()) {
            return startTime + " - " + endTime;
        } else {
            return "";
        }
    }

    public void filter(String searchKeyword, List<Schedule> schedules) {
        searchKeyword = searchKeyword.toLowerCase(Locale.getDefault());
        mSchedules.clear();

        for (Schedule schedule: schedules) {
            if (schedule.getClassCode().toLowerCase(Locale.getDefault()).contains(searchKeyword)) {
                mSchedules.add(schedule);
            }
        }

        notifyDataSetChanged();
    }
}
