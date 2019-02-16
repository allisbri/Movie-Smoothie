package com.mobilepgh.movie_smoothie.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static String baseURL = "https://api.themoviedb.org/3/movie/";
    private static String APIkey;

    private static String sortBy = "popular";
    public enum SortOrder{
        NEW("new"),
        POPULAR("popular"),
        TOP_RATED("top_rated");
        private String sortOrder;

        SortOrder(String s){
            sortOrder = s;
        }
        public String getSortOrder(){
            return sortOrder;
        }
    }

    public NetworkUtils(SortOrder sortOrder){
        sortBy = sortOrder.getSortOrder();

    }

    public static URL buildURL(){
        Uri builtUri = Uri.parse(baseURL + sortBy).buildUpon()
            .appendQueryParameter("api_key", APIkey)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                //works because it blocks while waiting for input to scan
                return scanner.next();
            } else {
                return null;
            }
        } finally {
                urlConnection.disconnect();
        }
    }
}


