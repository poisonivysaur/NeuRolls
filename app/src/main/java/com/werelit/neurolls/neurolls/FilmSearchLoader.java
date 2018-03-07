package com.werelit.neurolls.neurolls;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class FilmSearchLoader extends AsyncTaskLoader<String> {

    private String query;

    // TODO make a new attribute searchType to which method to call in movie db utils

    public FilmSearchLoader(Context context, String query){
        super(context);
        this.query = query;
    }

    @Override
    protected void onStartLoading(){forceLoad();}

    @Override
    public String loadInBackground() {
        // TODO do the checking of searchType attribute here and use the method  from dbutils
        // depending on what is the value of searchType set in searchable activity
        return MovieDBUtils.searchMovie(this.query);
    }
}
