package com.werelit.neurolls.neurolls.network;

import android.text.TextUtils;
import android.util.Log;

import com.werelit.neurolls.neurolls.model.Book;
import com.werelit.neurolls.neurolls.model.Film;
import com.werelit.neurolls.neurolls.model.Game;
import com.werelit.neurolls.neurolls.model.Media;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonConverter {
    private static final String TAG = JsonConverter.class.getSimpleName();

    public static ArrayList<Media> revisedSearchFilms(String filmSearchJson){
        if(TextUtils.isEmpty(filmSearchJson)){
            Log.wtf(TAG, "FILM IS EMPTY???!!!");
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
                String releaseDate = curObj.getString("release_date");
                if (releaseDate.equals(""))
                    releaseDate = "Unreleased";
                String genre = ConnectMovieDB.getGenre(curObj.getJSONArray("genre_ids"));
                Film m = new Film();
                m.setMediaID(id);
                m.setmMediaName(title);
                m.setmMediaGenre(genre);
                m.setmMediaYear(releaseDate);
                films.add(m);
            }
        }catch(JSONException e){
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }

        return films;
    }

    public static ArrayList<Media> revisedGetGameSearchResult(String gameSearchJson){
        if(TextUtils.isEmpty(gameSearchJson))
            return null;

        ArrayList<Media> games = new ArrayList<>();
        try {
            JSONArray searchResultJson = new JSONArray(gameSearchJson);

            for (int i = 0; i < searchResultJson.length(); i++) {
                JSONObject curObj = searchResultJson.getJSONObject(i);

                //System.out.println(curObj.getInt("id"));
                String gameId = "" + curObj.getInt("id");
                String gameName = curObj.getString("name");
                String summary = curObj.getString("summary");
                String genre = ConnectGameDB.getCompany(curObj.getJSONArray("genres"));
                String release = "No Release Revealed.";
                if (!curObj.isNull("release_dates"))
                    release = ConnectGameDB.getReleaseDate(curObj.getJSONArray("release_dates"));
                String series = "No Attached Series.";
                if (!curObj.isNull("collection"))
                    series = curObj.getJSONObject("collection").getString("name");
                String platforms = ConnectGameDB.getPlatforms(curObj.getJSONArray("platforms"));
                String developers = ConnectGameDB.getCompany(curObj.getJSONArray("developers"));
                String publishers = ConnectGameDB.getCompany(curObj.getJSONArray("publishers"));

                Game g = new Game(gameId, gameName, genre, release, platforms, publishers, summary, series);
                games.add(g);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }

        return games;
    }

    public static ArrayList<Media> revisedBookSearchResult(String bookSearchJson){
        ArrayList<Media> books = new ArrayList<>();

        try {
            JSONArray items = new JSONObject(bookSearchJson).getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject curObj = items.getJSONObject(i);
                String id = curObj.getString("id");
                String title = curObj.getJSONObject("volumeInfo").getString("title");
                String genres = ConnectBookDB.getGenres(curObj.getJSONObject("volumeInfo").optJSONArray("categories"));
                String pageCount = Integer.toString(curObj.getJSONObject("volumeInfo").getInt("pageCount")) + " pages";
                String author = ConnectBookDB.getAuthor(curObj.getJSONObject("volumeInfo").getJSONArray("authors"));
                String desc = curObj.getJSONObject("volumeInfo").getString("description");
                String publisher = curObj.getJSONObject("volumeInfo").getString("publisher");
                String publishedDate = curObj.getJSONObject("volumeInfo").getString("publishedDate");
                Book b = new Book(id, title, genres, publishedDate, author, publisher, desc);
                books.add(b);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }

        return books;
    }
}
