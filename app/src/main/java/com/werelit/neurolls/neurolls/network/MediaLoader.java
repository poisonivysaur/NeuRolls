package com.werelit.neurolls.neurolls.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;
import android.util.Log;

import com.werelit.neurolls.neurolls.model.Film;
import com.werelit.neurolls.neurolls.model.Media;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Loads a list of medias by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class MediaLoader extends AsyncTaskLoader<ArrayList<Media>> { // TODO change class name to MediaTaskLoader

    /** Tag for log messages */
    private static final String LOG_TAG = MediaTaskLoader.class.getName();

    /** Query URL */
    private String query;

    private boolean hasSearchedFilmAlready;

    private String filmdID = "";

    private int mediaCategory = 1; // TODO make a new attribute searchType to which method to call in movie db utils





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
    private static final String API_APPEND_CREDITS = "&append_to_response=credits";

    public static final String API_MOVIE_IMAGE_PATH = "https://image.tmdb.org/t/p/w1280";





    public static final String API_BOOKS = "https://www.googleapis.com/books/v1/volumes";
    public static final String BOOK_API_KEY = "&key=AIzaSyAfXEQyyt8FMtFd-AuisRSVpmZLtwsVK7k";
    public static final String BOOK_SEARCH = "?q=";
    public static final String BOOK_FIELDS = "&fields=kind,items(id,volumeInfo(title,authors,publisher,publishedDate,description,imageLinks,pageCount,categories))";











    /**
     * Constructs a new {@link MediaTaskLoader}.
     *
     * @param context of the activity
     * @param query to load data from
     */
    public MediaLoader(Context context, String query){
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
    public ArrayList<Media> loadInBackground() {

        if(hasSearchedFilmAlready){
            //Log.wtf(LOG_TAG, "MEDIA TASK LOADER IN HAS SEARCHED FILM ALREADY HAHAHA " + ConnectMovieDB.getMovieDetails(filmdID));
            //return getMovieDetails(filmdID);
        }else {
            Log.wtf(LOG_TAG, "GOING INSIDE ELSE ");
            // depending on what is the value of media category set in searchable activity
            switch (mediaCategory) {
                case Media.CATEGORY_FILMS:
                    return revisedSearchFilms(searchMovie(this.query));

                case Media.CATEGORY_BOOKS:
                    return null;//ConnectBookDB.searchBook(this.query);

                case Media.CATEGORY_GAMES:
                    return null;

                default: // search NeuRolls
                    // TODO search existing media from database and return an ArrayList<Media>
                    return null;//"TODO: search existing media in NeuRolls list";
            }
        }
        return revisedSearchFilms(searchMovie(this.query)); // for now
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

    public static ArrayList<Media> revisedSearchFilms(String filmSearchJson){
        if(TextUtils.isEmpty(filmSearchJson)){
            Log.wtf(LOG_TAG, "FILM IS EMPTY???!!!");
            return null;
        }
        ArrayList<Media> films = new ArrayList<>();

        try{
            JSONObject baseObject = new JSONObject(filmSearchJson);
            if(baseObject.getInt("total_results") == 0)
                throw new JSONException("Empty Result");
            JSONArray jsonArr = baseObject.getJSONArray("results");

            for(int i = 0; i < jsonArr.length(); i++) {
                JSONObject curObj = jsonArr.getJSONObject(i);
                String id = "" + curObj.getInt("id");
                String title = curObj.getString("title");
                String releaseDate = curObj.optString("release_date");
                releaseDate = formatDate(releaseDate);
                String genre = ConnectMovieDB.getGenre(curObj.getJSONArray("genre_ids"));
                /*
                String imageSource = "";
                if(curObj.optString("poster_path") != null){
                    imageSource = ConnectMovieDB.API_MOVIE_IMAGE_PATH + curObj.getString("poster_path");
                }*/
                String imageSource = "";
                URL imageUrl = null;
                Bitmap imageBmp = null;
                if(curObj.optString("poster_path") != null){
                    imageSource = "https://image.tmdb.org/t/p/w300" + curObj.getString("poster_path");
                    imageUrl = new URL(imageSource);
                    imageBmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                }
                Film m = new Film();
                m.setMediaID(id);
                m.setmMediaName(title);
                m.setmMediaGenre(genre);
                m.setmMediaYear(releaseDate);
                m.setThumbnailBmp(imageBmp);
                m.setImageDir(imageSource);
                films.add(m);
            }
        }catch(JSONException e){
            e.printStackTrace();
            Log.e(LOG_TAG, e.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return films;
    }

    private static String formatDate(String publishedDate){
        if(TextUtils.isEmpty(publishedDate))
            return "9999-01-01";
        String returnDate = "";
        String[] splitPublishedDate = publishedDate.split("-");

        switch (splitPublishedDate.length){
            case 1:
                returnDate = splitPublishedDate[0] + "-01-01";
                break;
            case 2:
                returnDate = splitPublishedDate[0] + "-" + splitPublishedDate[1] + "-01";
                break;
            case 3:
                returnDate = splitPublishedDate[0] + "-" + splitPublishedDate[1] + "-" + splitPublishedDate[2];
                break;
            default:
                returnDate = "9999-01-01";
        }

        return returnDate;
    }

    public static String getMovieDetails(String movieId){
        String url = API_URL + API_MOVIE + "/" + movieId + API_KEY + API_APPEND_CREDITS;
        return NetworkUtils.httpGetRequestToAny(url);
    }

}
