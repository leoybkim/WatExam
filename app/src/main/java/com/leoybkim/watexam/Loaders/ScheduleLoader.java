package com.leoybkim.watexam.Loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.leoybkim.watexam.Models.Schedule;
import com.leoybkim.watexam.QueryUtils;

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


        if (QueryUtils.fetchScheduleData(mUrl).isEmpty() || QueryUtils.fetchScheduleData(mUrl) == null) {
            Log.d("Fuck", QueryUtils.fetchScheduleData(mUrl).toString());
        }

        return QueryUtils.fetchScheduleData(mUrl);
    }
}
