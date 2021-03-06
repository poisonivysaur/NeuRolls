package com.werelit.neurolls.neurolls.network;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.werelit.neurolls.neurolls.model.Film;
import com.werelit.neurolls.neurolls.model.Media;

/**
 * Loads a list of medias by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class MediaTaskLoader extends AsyncTaskLoader<String> { // TODO change class name to MediaTaskLoader

    /** Tag for log messages */
    private static final String LOG_TAG = MediaTaskLoader.class.getName();

    /** Query URL */
    private String query;

    private boolean hasSearchedFilmAlready;

    private String filmdID = "";

    private int mediaCategory = 1; // TODO make a new attribute searchType to which method to call in movie db utils

    /**
     * Constructs a new {@link MediaTaskLoader}.
     *
     * @param context of the activity
     * @param query to load data from
     */
    public MediaTaskLoader(Context context, String query){
        super(context);
        this.query = query;
    }

    public void setMediaCategory(int mediaCategory) {
        this.mediaCategory = mediaCategory;
    }

    @Override
    protected void onStartLoading(){forceLoad();}

    /**
     * This is on a background thread.
     */
    @Override
    public String loadInBackground() {

        if(hasSearchedFilmAlready){
            //Log.wtf(LOG_TAG, "MEDIA TASK LOADER IN HAS SEARCHED FILM ALREADY HAHAHA " + ConnectMovieDB.getMovieDetails(filmdID));
            return ConnectMovieDB.getMovieDetails(filmdID);
        }else {
            Log.wtf(LOG_TAG, "GOING INSIDE ELSE ");
            // depending on what is the value of media category set in searchable activity
            switch (mediaCategory) {
                case Media.CATEGORY_FILMS:
                    return ConnectMovieDB.searchMovie(this.query);

                case Media.CATEGORY_BOOKS:
                    return ConnectBookDB.searchBook(this.query);

                case Media.CATEGORY_GAMES:
                    return "";

                default: // search NeuRolls
                    // TODO search existing media from database and return an ArrayList<Media>
                    return "TODO: search existing media in NeuRolls list";
            }
        }
    }

    public boolean isHasSearchedFilmAlready() {
        return hasSearchedFilmAlready;
    }

    public void setHasSearchedFilmAlready(boolean hasSearchedFilmAlready) {
        this.hasSearchedFilmAlready = hasSearchedFilmAlready;
    }

    public String getFilmdID() {
        return filmdID;
    }

    public void setFilmdID(String filmdID) {
        this.filmdID = filmdID;
    }
}
