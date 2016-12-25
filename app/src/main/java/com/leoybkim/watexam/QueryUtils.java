package com.leoybkim.watexam;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by leo on 24/12/16.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    // Hardcoded JSON response for now
    private static final String SAMPLE_JSON_RESPONSE = "{\"meta\":{\"requests\":15,\"timestamp\":1482552703,\"status\":200,\"message\":\"Request successful\",\"method_id\":1187,\"method\":{\"disclaimer\":\"Review the 'No Warranty' section of the University of Waterloo Open Data License before using this data. If building services upon this data, please inform your users of the inherent risks (as a best practice)\",\"license\":\"https:\\/\\/uwaterloo.ca\\/open-data\\/university-waterloo-open-data-license-agreement-v1\"}},\"data\":[{\"course\":\"PHYS 233\",\"sections\":[{\"section\":\"001\",\"day\":\"Saturday\",\"date\":\"2016-04-23\",\"start_time\":\"4:00 PM\",\"end_time\":\"6:30 PM\",\"location\":\"PHY 308\",\"notes\":\"\"}]},{\"course\":\"ECE 318\",\"sections\":[{\"section\":\"001\",\"day\":\"Friday\",\"date\":\"2016-04-08\",\"start_time\":\"12:30 PM\",\"end_time\":\"3:00 PM\",\"location\":\"MC 4059,4060,4061\",\"notes\":\"\"},{\"section\":\"002\",\"day\":\"Friday\",\"date\":\"2016-04-08\",\"start_time\":\"12:30 PM\",\"end_time\":\"3:00 PM\",\"location\":\"MC 4059,4060,4061\",\"notes\":\"\"}]},{\"course\":\"ECE 390\",\"sections\":[{\"section\":\"001\",\"day\":\"Wednesday\",\"date\":\"2016-04-13\",\"start_time\":\"12:30 PM\",\"end_time\":\"3:00 PM\",\"location\":\"RCH 301,302\",\"notes\":\"\"},{\"section\":\"002\",\"day\":\"Wednesday\",\"date\":\"2016-04-13\",\"start_time\":\"12:30 PM\",\"end_time\":\"3:00 PM\",\"location\":\"RCH 301,302\",\"notes\":\"\"}]},{\"course\":\"ECE 356\",\"sections\":[{\"section\":\"001\",\"day\":\"Friday\",\"date\":\"2016-04-22\",\"start_time\":\"9:00 AM\",\"end_time\":\"11:30 AM\",\"location\":\"DC 1350\",\"notes\":\"\"}]},{\"course\":\"ECE 358\",\"sections\":[{\"section\":\"001\",\"day\":\"Tuesday\",\"date\":\"2016-04-19\",\"start_time\":\"4:00 PM\",\"end_time\":\"6:30 PM\",\"location\":\"PAC 6\",\"notes\":\"\"}]}]}";

    // Empty private constructor
    private QueryUtils() {}

    // Parse out exam schedules from JSON payload into an ArrayList<Schedule>
    public static ArrayList<Schedule> extractSchedule() {
        ArrayList<Schedule> schedules = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(SAMPLE_JSON_RESPONSE);
            JSONArray data = root.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                JSONObject currentSchedule = data.getJSONObject(i);
                String classCode = currentSchedule.getString("course");
                JSONArray sections = currentSchedule.getJSONArray("sections");

                // Different sections will still return the same exam date, locations, time
                JSONObject section = sections.getJSONObject(0);
                String date = section.getString("date");
                String location = section.getString("location");
                String startTime = section.getString("start_time");
                String endTime = section.getString("end_time");

                // Append to the ArrayList
                schedules.add(new Schedule(classCode, date, location, startTime, endTime));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem occured while parsing exam schedule JSON", e);
        }

        return schedules;
    }
}
