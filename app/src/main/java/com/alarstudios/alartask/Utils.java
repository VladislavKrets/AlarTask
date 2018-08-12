package com.alarstudios.alartask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {
    public static String getRequest(String url, String params) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(url +
                (params == null ? "" : "?" + params)).openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) builder.append(line); //getting request
        reader.close();
        reader = null;
        urlConnection.disconnect();
        urlConnection = null;
        return builder.toString();
    }
}
