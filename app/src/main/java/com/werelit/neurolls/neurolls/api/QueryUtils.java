package com.werelit.neurolls.neurolls.api;

import android.util.Log;

import com.werelit.neurolls.neurolls.model.Film;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by inoue on 03/03/2018.
 */

public class QueryUtils {

    private static final String TAG = QueryUtils.class.getSimpleName();

    private QueryUtils(){}

    public static List<Film> testExtractFilm(){
        List<Film> films = new ArrayList<>();

        String filmJson = ConnectMovieDB.searchMovie("Star Wars");

        try{
            JSONObject baseJsonResponse = new JSONObject(filmJson);
            JSONArray resultsArray = baseJsonResponse.getJSONArray("results");
            for(int i = 0; i < resultsArray.length(); i++){
                JSONObject currentObj = resultsArray.getJSONObject(i);
                String title = currentObj.getString("title");
                Log.v(TAG, "Title: " + title);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Testing @testExtractFilm\n" + e.toString());
        }

        return films;
    }

}
