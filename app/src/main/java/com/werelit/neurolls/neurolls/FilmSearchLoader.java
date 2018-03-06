package com.werelit.neurolls.neurolls;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class FilmSearchLoader extends AsyncTaskLoader<String> {

    private String query;

    public FilmSearchLoader(Context context, String query){
        super(context);
        this.query = query;
    }

    @Override
    protected void onStartLoading(){forceLoad();}

    @Override
    public String loadInBackground() {
        return MovieDBUtils.searchMovie(this.query);
    }
}
