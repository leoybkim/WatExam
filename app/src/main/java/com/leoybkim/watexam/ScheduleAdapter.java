package com.leoybkim.watexam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by leo on 24/12/16.
 *
 * ScheduleAdapter is a custom Adapter created to allow more than one TextView to be linked the ListView
 */

public class ScheduleAdapter extends ArrayAdapter<Schedule> {

    // Constructor
    public ScheduleAdapter (Context context, List<Schedule> schedules) {
        super(context, 0, schedules);
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

        // Find view from schedule_list_item.xml and display string
        TextView classCodeView = (TextView) listItemView.findViewById(R.id.class_code);
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        TextView locationView = (TextView) listItemView.findViewById(R.id.location);
        TextView startTimeView = (TextView) listItemView.findViewById(R.id.start_time);
        TextView endTimeView = (TextView) listItemView.findViewById(R.id.end_time);

        classCodeView.setText(currentSchedule.getClassCode());
        dateView.setText(currentSchedule.getDate());
        locationView.setText(currentSchedule.getLocation());
        startTimeView.setText(currentSchedule.getStartTime());
        endTimeView.setText(currentSchedule.getEndTime());

        return listItemView;
    }
}
