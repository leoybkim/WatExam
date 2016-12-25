package com.leoybkim.watexam;

import android.text.TextUtils;
import android.util.Log;
import android.util.StringBuilderPrinter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 24/12/16.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    // Empty private constructor
    private QueryUtils() {}

    // Query UWaterloo open data API and return list of Schedule objects
    public static List<Schedule> fetchScheduleData(String requestUrl){
        URL url = createUrl(requestUrl);

        String JSONresponse  = null;
        try {
            JSONresponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Schedule> schedules = extractSchedule(JSONresponse);
        return schedules;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem occured while building the url", e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String JSONResponse = "";

        if (url == null) {
            return JSONResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                JSONResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) { urlConnection.disconnect(); }
            if (inputStream != null) { inputStream.close(); }
        }

        return JSONResponse;
    }

    // Convert InputStream to String
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();

            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }

        return output.toString();
    }

    // Parse out exam schedules from JSON payload into an ArrayList<Schedule>
    public static ArrayList<Schedule> extractSchedule(String scheduleJSON) {
        if (TextUtils.isEmpty(scheduleJSON)) {
            return null;
        }

        ArrayList<Schedule> schedules = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(scheduleJSON);
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
