package com.leoybkim.watexam;

/**
 * Created by leo on 23/12/16.
 */

public class Schedule {

    private String mClassCode;
    private String mDate;
    private String mLocation;
    private String mStartTime;
    private String mEndTime;

    // Constructor
    public Schedule (String classCode, String location, String date, String startTime, String endTime) {
        mClassCode = classCode;
        mDate = date;
        mLocation = location;
        mStartTime = startTime;
        mEndTime = endTime;
    }

    // Getter methods
    public String getClassCode() { return mClassCode; }
    public String getDate() { return mDate; }
    public String getLocation() { return mLocation; }
    public String getStartTime() { return mStartTime; }
    public String getEndTime() { return mEndTime; }
}
