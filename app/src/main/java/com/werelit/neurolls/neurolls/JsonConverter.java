package com.werelit.neurolls.neurolls;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.werelit.neurolls.neurolls.api.ConnectBookDB;
import com.werelit.neurolls.neurolls.api.ConnectGameDB;
import com.werelit.neurolls.neurolls.api.ConnectMovieDB;
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

    //New Code Here
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
                String title = curObj.getString("title");
                String releaseDate = curObj.getString("release_date");
                if (releaseDate.equals(""))
                    releaseDate = "Unreleased";
                String genre = ConnectMovieDB.getGenre(curObj.getJSONArray("genre_ids"));
                Film m = new Film();
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
            return "No Genre found";
        }

        return genre;
    }

//    public static ArrayList<Media> getGamesSearchResult(String query){
//        ArrayList<Media> games = new ArrayList<>();
//        String searchResultJsonString = ConnectGameDB.revisedSearchGame(query);
//
//        try {
//            JSONArray searchResultJson = new JSONArray(searchResultJsonString);
//
//            for (int i = 0; i < searchResultJson.length(); i++) {
//                JSONObject curObj = searchResultJson.getJSONObject(i);
//
//                //System.out.println(curObj.getInt("id"));
//                int gameId = curObj.getInt("id");
//                String gameName = curObj.getString("name");
//                String summary = curObj.getString("summary");
//                String genre = ConnectGameDB.getCompany(curObj.getJSONArray("genres"));
//                String release = "No Release Revealed.";
//                if (!curObj.isNull("release_dates"))
//                    release = ConnectGameDB.getReleaseDate(curObj.getJSONArray("release_dates"));
//                String series = "No Attached Series.";
//                if (!curObj.isNull("collection"))
//                    series = curObj.getJSONObject("collection").getString("name");
//                String platforms = ConnectGameDB.getPlatforms(curObj.getJSONArray("platforms"));
//                String developers = ConnectGameDB.getCompany(curObj.getJSONArray("developers"));
//                String publishers = ConnectGameDB.getCompany(curObj.getJSONArray("publishers"));
//            /*
//            System.out.println("Title: " + gameName);
//            System.out.println("\tsummary: " + summary);
//            System.out.println("\tgenre: " + genre);
//            System.out.println("\tRelease: " + release);
//            System.out.println("\tseries: " + series);
//            System.out.println("\tplatforms: " + platforms);
//            System.out.println("\tdevelopers: " + developers);
//            System.out.println("\tpublishers: " + publishers);*/
//                Game g = new Game(gameName, genre, release, platforms, publishers, summary, series);
//                games.add(g);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e(TAG, e.toString());
//        }
//
//        return games;
//    }

    //New Code here
    public static ArrayList<Media> revisedGetGameSearchResult(String gameSearchJson){
        if(TextUtils.isEmpty(gameSearchJson))
            return null;

        ArrayList<Media> games = new ArrayList<>();
        try {
            JSONArray searchResultJson = new JSONArray(gameSearchJson);

            for (int i = 0; i < searchResultJson.length(); i++) {
                JSONObject curObj = searchResultJson.getJSONObject(i);

                //System.out.println(curObj.getInt("id"));
                int gameId = curObj.getInt("id");
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

                Game g = new Game(gameName, genre, release, platforms, publishers, summary, series);
                games.add(g);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }

        return games;
    }

    //TODO Not yet completed
    public static ArrayList<Media> getBookSearchResult(String query){
        ArrayList<Media> books = new ArrayList<>();
        String searchResultResponse = ConnectBookDB.searchBook(query);

        try {
            JSONArray items = new JSONObject(searchResultResponse).getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject curObj = items.getJSONObject(i);
                System.out.println("ID: " + curObj.getString("id"));
                System.out.println("Book: " + curObj.getJSONObject("volumeInfo").getString("title"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }

        return books;
    }

    public static ArrayList<Media> revisedBookSearchResult(String bookSearchJson){
        ArrayList<Media> books = new ArrayList<>();

        try {
            JSONArray items = new JSONObject(bookSearchJson).getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject curObj = items.getJSONObject(i);
                String id = curObj.getString("id");
                String title = curObj.getJSONObject("volumeInfo").getString("title");
                String pageCount = Integer.toString(curObj.getJSONObject("volumeInfo").getInt("pageCount")) + " pages";
                String author = ConnectBookDB.getAuthor(curObj.getJSONObject("volumeInfo").getJSONArray("authors"));
                String desc = curObj.getJSONObject("volumeInfo").getString("description");
                String publisher = curObj.getJSONObject("volumeInfo").getString("publisher");
                String publishedDate = curObj.getJSONObject("volumeInfo").getString("publishedDate");
                Book b = new Book(title, pageCount, publishedDate, author, publisher, desc);
                books.add(b);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }

        return books;
    }
}
