package com.leoybkim.watexam;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by leo on 25/12/16.
 */

public class ScheduleLoader extends AsyncTaskLoader<List<Schedule>> {
    private String mUrl;

    public ScheduleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    // On background thread
    @Override
    public List<Schedule> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        return QueryUtils.fetchScheduleData(mUrl);
    }
}
