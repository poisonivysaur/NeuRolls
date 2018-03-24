package com.werelit.neurolls.neurolls.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConnectGameDB {

    private static final String TAG = ConnectGameDB.class.getSimpleName();

    //public static final String USER_KEY = "dfdc187e8ee5ee5673d66e5b5435a37d";
    public static final String USER_KEY = "a2fc64829017cd594a7ebecaf2959029";
    public static final String API_ENDPOINT = "https://api-endpoint.igdb.com";

    public static final String GAME = "/games";
    public static final String PLATFORMS = "/platforms";
    public static final String COMPANY = "/companies";
    public static final String SEARCH = "/?search=";

    public static final String SEARCH_FILTER = "&fields=name,summary,collection,cover,release_dates,publishers,developers,platforms,genres&expand=game,collection,developers,publishers,platforms,genres&filter[developers][exists]=true&filter[publishers][exists]=true&filter[category][eq]=0&filter[platforms][any]=6,48,9,38,11,12,37,20,130,5,41";

    public static final String GAME_IMAGE_URL = "https://images.igdb.com/igdb/image/upload/t_cover_big";

    private ConnectGameDB() {}

//    public static String revisedSearchGame(String query){
//        String response = "";
//        String url = "";
//
//        try {
//            url = API_ENDPOINT + GAME + SEARCH + URLEncoder.encode(query, "UTF-8") + SEARCH_FILTER;
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        response = NetworkUtils.makeHttpRequestToigdb(url);
//
//        return response;
//    }

//    public static String searchGame(String query){
//        String url = "";
//        String response = "";
//
//        try{
//            url = API_ENDPOINT + GAME + SEARCH + URLEncoder.encode(query, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        response = NetworkUtils.makeHttpRequestToigdb(url);
//
//        return response;
//    }

//    public static String getGameDetail(int gameId){
//        return NetworkUtils.makeHttpRequestToigdb(API_ENDPOINT + GAME + "/" + gameId);
//    }

    public static String getPlatforms(JSONArray platformsJson){
        if(platformsJson.length() == 0)
            return "No Available Platform";
        String platform = "";

        try {
            for (int i = 0; i < platformsJson.length(); i++) {
                JSONObject basePlatform = platformsJson.getJSONObject(i);
                platform += basePlatform.getString("name") + "/";

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
            return "No Platform available";
        }

        return platform.substring(0, platform.length() - 1);
    }

    public static String getCompany(JSONArray companyJson){
        if(companyJson.length() == 0)
            return "No Available Platform";
        String company = "";

        try {
            for (int i = 0; i < companyJson.length(); i++) {
                JSONObject basePlatform = companyJson.getJSONObject(i);
                company += basePlatform.getString("name") + ",";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
            return "No Developer/Publisher Found";
        }

        return company.substring(0, company.length() - 1);
    }

    public static String getReleaseDate(JSONArray release){
        if(release.length() == 0)
            return "No Release Date";
        String releaseYear = "No Release Date";

        try {
            JSONObject releaseObj = release.getJSONObject(0);
            releaseYear = releaseObj.getString("human");
            releaseYear = getReleaseDate(releaseYear);
        }catch (JSONException e){
            e.printStackTrace();
            Log.e(TAG, e.toString());
            return "No Release yet";
        }

        return releaseYear;
    }

    private static String getReleaseDate(String releaseDate){
        String returnDate = "2019-01-01";
        String[] splitRelease = releaseDate.split("-");

        switch (splitRelease.length) {
            case 1:
                returnDate = splitRelease[0] + "-01-01";
                break;
            case 2:
                returnDate = splitRelease[0] + "-" + changeMonthToNum(splitRelease[1]) + "-01";
                break;
            case 3:
                returnDate = splitRelease[0] + "-" + changeMonthToNum(splitRelease[1]) + "-" + splitRelease[2];
                break;
            default:
                break;
        }

        return returnDate;
    }

    private static String changeMonthToNum(String month){
        String returnMonth = "01";
        month = month.toLowerCase();
        switch(month){
            case "jan":
                returnMonth = "01";
                break;
            case "feb":
                returnMonth = "02";
                break;
            case "mar":
                returnMonth = "03";
                break;
            case "apr":
                returnMonth = "04";
                break;
            case "may":
                returnMonth = "05";
                break;
            case "jun":
                returnMonth = "06";
                break;
            case "jul":
                returnMonth = "07";
                break;
            case "aug":
                returnMonth = "08";
                break;
            case "sep":
                returnMonth = "09";
                break;
            case "oct":
                returnMonth = "10";
                break;
            case "nov":
                returnMonth = "11";
                break;
            case "dec":
                returnMonth = "12";
                break;
            default:
                returnMonth = "01";
        }

        return returnMonth;
    }

}
