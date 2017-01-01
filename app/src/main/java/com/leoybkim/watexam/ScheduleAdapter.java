package com.leoybkim.watexam;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    private List<Schedule> schedulesList = null;
    private ArrayList<Schedule> arrayList;

    // Constructor
    public ScheduleAdapter (Context context, List<Schedule> schedules) {
        super(context, 0, schedules);
        this.schedulesList = schedules;
        arrayList = new ArrayList<>();
        this.arrayList.addAll(schedules);
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
        schedulesList.clear();

        for (Schedule schedule: schedules) {
            if (schedule.getClassCode().toLowerCase(Locale.getDefault()).contains(searchKeyword)) {
                schedulesList.add(schedule);
            }
        }

        notifyDataSetChanged();
    }
}
