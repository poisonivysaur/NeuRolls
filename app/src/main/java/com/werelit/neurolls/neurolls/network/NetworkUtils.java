package com.werelit.neurolls.neurolls.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private NetworkUtils(){}

    private static URL createURL(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        }catch(MalformedURLException e){
            System.out.println("ConnectMovieDb:createURL");
            e.printStackTrace();
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

        try {
            //setProxy();

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            conn.connect();

            //System.out.println("Response Code: " + conn.getResponseCode());
            if(conn.getResponseCode() == 200){
                //Log.wtf(TAG, "CODE IS 200");
                inputStream = conn.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                jsonResponse = "";
            }
        }catch(IOException e){
            System.out.println("NetworkUtils:httpGetRequestToAny");
            e.printStackTrace();
        }finally{
            if(inputStream != null){
                try{
                    inputStream.close();
                }catch(IOException e){}
            }
            System.setProperty("http.proxyHost", "");
        }
        //Log.wtf(TAG, jsonResponse);
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

    public static void setProxy() {
        System.setProperty("java.net.useSystemProxies", "true");
        //System.out.println("detecting proxies");
        List l = null;
        try {
            l = ProxySelector.getDefault().select(new URI("http://foo/bar"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (l != null) {
            for (Iterator iter = l.iterator(); iter.hasNext();) {
                java.net.Proxy proxy = (java.net.Proxy) iter.next();
                //System.out.println("proxy type: " + proxy.type());

                InetSocketAddress addr = (InetSocketAddress) proxy.address();

                if (addr == null) {
                    //System.out.println("No Proxy");
                } else {
                    // System.out.println("proxy hostname: " + addr.getHostName());
                    System.setProperty("http.proxyHost", addr.getHostName());
                    //System.out.println("proxy port: " + addr.getPort());
                    System.setProperty("http.proxyPort", Integer.toString(addr.getPort()));
                }
            }
        }
    }

}
