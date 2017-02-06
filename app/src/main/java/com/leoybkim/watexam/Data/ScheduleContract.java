package com.leoybkim.watexam.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by leo on 05/02/17.
 */

public final class ScheduleContract {

    public static final String CONTENT_AUTHORITY = "com.leoybkim.watexam";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_SCHEDUlE = "schedules";

    private ScheduleContract() {}

    public static final class ScheduleEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SCHEDUlE);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCHEDUlE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCHEDUlE;

        public static final String TABLE_NAME ="exams";
        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_EXAM_CLASS ="class";
        public static final String COLUMN_EXAM_DATE ="date";
        public static final String COLUMN_EXAM_LOCATION ="location";
        public static final String COLUMN_EXAM_START_TIME ="start_time";
        public static final String COLUMN_EXAM_END_TIME ="end_time";
    }
}
