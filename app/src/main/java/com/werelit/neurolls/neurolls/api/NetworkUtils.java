package com.werelit.neurolls.neurolls.api;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by inoue on 06/03/2018.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private NetworkUtils(){}

    private static URL createURL(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        }catch(MalformedURLException e){
            //System.out.println("ConnectMovieDb:createURL");
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
        return url;
    }

    public static String httpGetRequestToAny(String urlString){
        if(urlString.equals("") || urlString == null)
            return null;
        String jsonResponse = "";

        URL url = createURL(urlString);

        HttpURLConnection conn = null;
        InputStream inputStream = null;

        try{
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            conn.connect();

            //System.out.println("Response Code: " + conn.getResponseCode());
            if(conn.getResponseCode() == 200){
                inputStream = conn.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                jsonResponse = "";
            }
        }catch(IOException e){
            //System.out.println("NetworkUtils:httpGetRequestToAny");
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }finally{
            if(inputStream != null){
                try{
                    inputStream.close();
                }catch(IOException e){}
            }
            if(conn != null)
                conn.disconnect();
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

}
