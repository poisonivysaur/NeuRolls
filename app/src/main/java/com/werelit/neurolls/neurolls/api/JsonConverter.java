package com.werelit.neurolls.neurolls.api;

import android.util.Log;

import com.werelit.neurolls.neurolls.model.Film;
import com.werelit.neurolls.neurolls.model.Media;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by inoue on 06/03/2018.
 */

public class JsonConverter {

    private static final String TAG = JsonConverter.class.getSimpleName();

    public static ArrayList<Media> searchFilmsFromKeyword(String query){
        if(query.equals("") || query == null)
            return null;

        ArrayList<Media> films = new ArrayList<>();
        String response = ConnectMovieDB.searchMovie(query);
        if(response == null || response.equals(""))
            return null;

        try{
            JSONObject baseObject = new JSONObject(response);
            if(baseObject.getInt("total_results") == 0)
                throw new Exception("Empty Result");
            JSONArray jsonArr = baseObject.getJSONArray("results");

            for(int i = 0; i < jsonArr.length(); i++){
                JSONObject curObj = jsonArr.getJSONObject(i);
                String title = curObj.getString("title");
                String releaseDate = curObj.getString("release_date");
                if(releaseDate.equals(""))
                    releaseDate = "Unreleased";
                String genre = ConnectMovieDB.getGenre(curObj.getJSONArray("genre_ids"));
                /*
                System.out.println(title);
                System.out.println(releaseDate);
                for(int j = 0; j < genreIds.length(); j++){
                    System.out.println("\tGenre IDS: " + genreIds.get(j));
                }
                System.out.println(genre);
                */
                Media m = new Film();
                m.setmMediaName(title);
                m.setmMediaGenre(genre);
                m.setmMediaYear(releaseDate);
                films.add(m);
            }

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return films;
    }

    public static Film getFilmById(int filmId){
        Film film = null;

        String response = ConnectMovieDB.getMovieDetails(filmId);
        if(response == null || response.equals(""))
            return null;

        try{
            JSONObject baseObject = new JSONObject(response);

            String title = baseObject.getString("title");
            String releaseDate = baseObject.getString("release_date");
            String genre = getGenre(baseObject.getJSONArray("genres"));
            int duration = baseObject.getInt("runtime");
            String director = ConnectMovieDB.getDirector(filmId);
            String synopsis = baseObject.getString("overview");
            String production = "";
            if(baseObject.getJSONArray("production_companies").length() != 0)
                production = baseObject.getJSONArray("production_companies").getJSONObject(0).getString("name");


            film = new Film(title, genre, releaseDate, duration, director, production, synopsis);

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return film;
    }

    private static String getGenre(JSONArray genreArr){
        if(genreArr.length() == 0)
            return "";
        String genre = "";

        try {
            for (int i = 0; i < genreArr.length(); i++) {
                JSONObject curObj = genreArr.getJSONObject(i);
                genre += curObj.getString("name") + "/";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
            return "";
        }

        return genre;
    }

}
