package com.werelit.neurolls.neurolls.network;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StringUtils {
    private static final String TAG = StringUtils.class.getSimpleName();

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }
}
