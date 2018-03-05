package com.werelit.neurolls.neurolls.api;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class ConnectMovieDB {

    private static final String TAG = ConnectMovieDB.class.getSimpleName();

    /**
     * API key for The Movie DB.
     */
    private static final String API_KEY = "?api_key=07d9adcc57400a9e0037f806286a7c3c";
    /**
     * The general URI for calling the API.
     */
    private static final String API_URL = "https://api.themoviedb.org/3";

    /**
     * The URI for searching movies. (ex: https://api.themoviedb.org/3/search/movie?query=Black Panther)
     */
    private static final String API_SEARCH = "/search/movie";
    /**
     * The URI for getting a specific movie. (ex: https://api.themoviedb.org/3/movie/284054)
     */
    private static final String API_MOVIE = "/movie";

    private static final String API_GENRE = "/genre/movie/list";
    private static final String API_PEOPLE = "/person";
    private static final String API_CREDIT = "/credits";

    private ConnectMovieDB(){}

    public static String searchMovie(String query){
        String url = "";
        String jsonResponse = "";
        try{
            url = API_URL + API_SEARCH + API_KEY + "&query=" + URLEncoder.encode(query, "UTF-8");
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }

        jsonResponse = NetworkUtils.httpGetRequestToAny(url);

        return jsonResponse;
    }

    public static String getMovieDetails(int movieId){
        String url = API_URL + API_MOVIE + "/" + movieId + API_KEY;
        return NetworkUtils.httpGetRequestToAny(url);
    }

    public static String getGenre(JSONArray genreIds){
        if(genreIds.length() == 0)
            return "";
        String genreList = NetworkUtils.httpGetRequestToAny(API_URL + API_GENRE + API_KEY);
        String genre = "";
        HashMap<Integer, String> allGenres = new HashMap<>();
        try{
            JSONObject baseObject = new JSONObject(genreList);
            JSONArray jsonArray = baseObject.getJSONArray("genres");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject genreObj = jsonArray.getJSONObject(i);
                allGenres.put(genreObj.getInt("id"), genreObj.getString("name"));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

        try {
            for (int i = 0; i < genreIds.length(); i++) {
                if (allGenres.containsKey(genreIds.getInt(i))) {
                    genre += allGenres.get(genreIds.getInt(i)) + "/";
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

        return genre.substring(0, genre.length() - 1);
    }

    public static String getDirector(int filmId){
        String director = "";
        String response = NetworkUtils.httpGetRequestToAny(API_URL + API_MOVIE + "/" + filmId + API_CREDIT + API_KEY);
        if(response == null)
            return "";
        try{
            JSONObject baseObject = new JSONObject(response);
            JSONArray crewArray = baseObject.getJSONArray("crew");

            for(int i = 0; i < crewArray.length(); i++){
                JSONObject curObj = crewArray.getJSONObject(i);
                if(curObj.getString("job").equals("Director")){
                    director = curObj.getString("name");
                    break;
                }
            }

        }catch(Exception e){
            e.printStackTrace();
            return "";
        }

        return director;
    }

}
