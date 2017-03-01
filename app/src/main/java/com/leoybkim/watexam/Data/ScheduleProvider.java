package com.leoybkim.watexam.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by leo on 05/02/17.
 */

public class ScheduleProvider extends ContentProvider {

    public static final String LOG_TAG = ScheduleProvider.class.getSimpleName();

    private static final int EXAMS = 100;
    private static final int EXAM_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        sUriMatcher.addURI(ScheduleContract.CONTENT_AUTHORITY, ScheduleContract.PATH_SCHEDUlE, EXAMS);
        sUriMatcher.addURI(ScheduleContract.CONTENT_AUTHORITY, ScheduleContract.PATH_SCHEDUlE  + "/#", EXAM_ID);
    }

    ScheduleDbHelper mDbHelper;

    @Override
    public boolean onCreate() {

        mDbHelper = new ScheduleDbHelper(this.getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor = null;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case EXAMS:
                cursor = database.query(ScheduleContract.ScheduleEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case EXAM_ID:
                selection = ScheduleContract.ScheduleEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(ScheduleContract.ScheduleEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // If data of this URI changes, cursor needs to be updated
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EXAMS:
                return insertExam(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertExam(Uri uri, ContentValues values) {

        String classCode = values.getAsString(ScheduleContract.ScheduleEntry.COLUMN_EXAM_CLASS);
        String location = values.getAsString(ScheduleContract.ScheduleEntry.COLUMN_EXAM_LOCATION);
        String date = values.getAsString(ScheduleContract.ScheduleEntry.COLUMN_EXAM_DATE);
        String startTime = values.getAsString(ScheduleContract.ScheduleEntry.COLUMN_EXAM_START_TIME);
        String endTime = values.getAsString(ScheduleContract.ScheduleEntry.COLUMN_EXAM_END_TIME);

        if (classCode == null || location == null | date == null | startTime == null | endTime == null) {
            throw new IllegalArgumentException("Exam requires more information to be saved to database");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(ScheduleContract.ScheduleEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete()");
        int rowsDeleted;
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EXAMS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(ScheduleContract.ScheduleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case EXAM_ID:
                // Delete a single row given by the ID in the URI
                selection = ScheduleContract.ScheduleEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted =  database.delete(ScheduleContract.ScheduleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    // TODO: update exam schedule upon API updates
    // There is no reason to update the database right now
    // Since it is just save and unsave exam schedule
    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EXAMS:
                return ScheduleContract.ScheduleEntry.CONTENT_LIST_TYPE;
            case EXAM_ID:
                return ScheduleContract.ScheduleEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}


