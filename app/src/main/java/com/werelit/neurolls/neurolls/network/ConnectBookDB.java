package com.werelit.neurolls.neurolls.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ConnectBookDB {
    private static final String TAG = ConnectBookDB.class.getSimpleName();

    public static final String API_BOOKS = "https://www.googleapis.com/books/v1/volumes";
    public static final String BOOK_API_KEY = "&key=AIzaSyAfXEQyyt8FMtFd-AuisRSVpmZLtwsVK7k";
    public static final String BOOK_SEARCH = "?q=";
    public static final String BOOK_FIELDS = "&fields=kind,items(id,volumeInfo(title,authors,publisher,publishedDate,description,imageLinks,pageCount,categories))";


    private ConnectBookDB(){}

    public static String searchBook(String query){
        String response = "";
        String url = "";
        try {
            url = API_BOOKS + BOOK_SEARCH + URLEncoder.encode(query, "UTF-8") + BOOK_API_KEY + BOOK_FIELDS;
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return "";
        }

        response = NetworkUtils.httpGetRequestToAny(url);

        return response;
    }

    public static String getAuthor(JSONArray authors){
        if(authors.length() == 0)
            return "No Authors";
        String author = "";

        try {
            for (int i = 0; i < authors.length(); i++) {
                author += authors.getString(i) + ",";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //Log.e(TAG, e.toString());
            return "No Authors found";
        }

        return author.substring(0, author.length() - 1);
    }

    public static String getGenres(JSONArray genres){
        if(genres == null)
            return "No Genre";
        else if(genres.length() == 0)
            return "No Genre";
        String genre = "";
        try {
            for (int i = 0; i < genres.length(); i++)
                genre += genres.getString(i) + "/";
        } catch (JSONException e) {
            e.printStackTrace();
            //Log.e(TAG, e.toString());
        }
        return genre.substring(0, genre.length() - 1);
    }

}
