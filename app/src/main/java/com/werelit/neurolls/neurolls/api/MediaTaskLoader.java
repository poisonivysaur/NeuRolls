package com.werelit.neurolls.neurolls.api;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.werelit.neurolls.neurolls.model.Media;

import java.util.List;

/**
 * Loads a list of medias by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class MediaLoader extends AsyncTaskLoader<List<Media>> {

    /** Tag for log messages */
    private static final String LOG_TAG = MediaLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link MediaLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public MediaLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Media> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of medias.
        List<Media> medias = QueryUtils.fetchMediaData(mUrl);
        return medias;
    }
}