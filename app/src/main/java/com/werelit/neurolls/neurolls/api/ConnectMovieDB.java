package com.werelit.neurolls.neurolls.api;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

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

    private ConnectMovieDB(){}

    private static URL createURL(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        }catch(MalformedURLException e){
            Log.e(TAG, "Error @ConnectMovieDB:createURL\n" + e.toString());
        }
        return url;
    }

    private static String httpGetRequestTotmdb(String stringUrl) {
        URL url = createURL(stringUrl);
        String jsonResponse = "";
        if(url == null){
            return "";
        }

        HttpURLConnection http = null;
        InputStream inputStream = null;
        try{
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");

            http.connect();

            System.out.println("Response Code: " + http.getResponseCode());
            if(http.getResponseCode() == 200){
                inputStream = http.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                jsonResponse = "";
            }
        }catch(IOException e){
            Log.e(TAG, "Error @ConnectMovieDB:httpGetRequestTotmdb\n" + e.toString());
        }finally {
            if(http != null){
                http.disconnect();
            }
            if(inputStream != null){
                try{
                    inputStream.close();
                }catch(IOException e){}
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream stream) throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        if(stream != null){
            inputStreamReader = new InputStreamReader(stream);
            reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null){
                builder.append(line);
                line = reader.readLine();
            }
        }

        if(inputStreamReader != null) inputStreamReader.close();
        if(reader != null) reader.close();

        return builder.toString();
    }

    public static String searchMovie(String query){
        String url = "";
        String jsonResponse = "";
        try{
            url = API_URL + API_SEARCH + API_KEY + "&query=" + URLEncoder.encode(query, "UTF-8");
        }catch(UnsupportedEncodingException e){
            Log.e(TAG, "Error @ConnectMovieDB:createURL\n" + e.toString());
        }

        jsonResponse = httpGetRequestTotmdb(url);

        return jsonResponse;
    }

    public static String getMovieDetails(int movieId){
        String url = API_URL + API_MOVIE + "/" + movieId + API_KEY;
        return httpGetRequestTotmdb(url);
    }

}
