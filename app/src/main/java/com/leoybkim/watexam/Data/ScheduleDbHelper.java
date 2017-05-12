package com.leoybkim.watexam.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.leoybkim.watexam.Data.ScheduleContract.ScheduleEntry;
/**
 * Created by leo on 05/02/17.
 */

public class ScheduleDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = ScheduleDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "watexam.db";
    private static final int DATABASE_VERSION = 1;

    public ScheduleDbHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Table for exam schedules
        String SQL_CREATE_EXAM_TABLE =  "CREATE TABLE " + ScheduleEntry.TABLE_NAME + " ("
                + ScheduleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ScheduleEntry.COLUMN_EXAM_CLASS + " TEXT NOT NULL, "
                + ScheduleEntry.COLUMN_EXAM_DATE + " TEXT, "
                + ScheduleEntry.COLUMN_EXAM_LOCATION + " TEXT, "
                + ScheduleEntry.COLUMN_EXAM_START_TIME + " TEXT, "
                + ScheduleEntry.COLUMN_EXAM_END_TIME + " TEXT);";
        db.execSQL(SQL_CREATE_EXAM_TABLE);

        // Table for term lists
//        String SQL_CREATE_TERM_TABLE = "CREATE TABLE " + TermEntry.TABLE_NAME + " ("
//                + TermEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + TermEntry.COLUMN_TERM_NAME + " TEXT NOT NULL);";
//        db.execSQL(SQL_CREATE_TERM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + "exams");
//        db.execSQL("DROP IF TABLE EXISTS " + "terms");
        onCreate(db);
    }
}
