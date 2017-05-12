package com.leoybkim.watexam.Loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.leoybkim.watexam.Models.Term;

import java.util.List;

/**
 * Created by leo on 11/05/17.
 */

public class TermLoader extends AsyncTaskLoader<List<Term>> {

    private String mUrl;

    public TermLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Term> loadInBackground() {
        return null;
    }
}
