package com.werelit.neurolls.neurolls;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.werelit.neurolls.neurolls.model.Media;

import java.util.List;

public class MediaLoader extends AsyncTaskLoader<List<Media>> {

    private String queryString;

    public MediaLoader(Context context){
        super(context);
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public List<Media> loadInBackground() {
        return null;
    }
}
