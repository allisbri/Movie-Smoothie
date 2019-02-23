package com.mobilepgh.movie_smoothie.utilities;

import android.net.Uri;

import com.mobilepgh.movie_smoothie.Poster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    public static int parseJSONPageData(String JSONstring){
        int pageTotal = 0;
        try{
            JSONObject fullResponse = new JSONObject(JSONstring);
            String totalPages = fullResponse.getString("total_pages");
            pageTotal = Integer.parseInt(totalPages);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return pageTotal;
    }

    public static ArrayList<Poster> parseJSONPosterData(String JSONstring){
        try{
            JSONObject fullResponse = new JSONObject(JSONstring);
            JSONArray results = fullResponse.getJSONArray("results");
            ArrayList<Poster> posters = new ArrayList<Poster>();
            for (int i = 0; i < results.length(); i++) {
                JSONObject movie = results.getJSONObject(i);
                String id = movie.getString("id");
                String posterPath = movie.getString("poster_path");
                Poster poster = new Poster(Integer.parseInt(id), posterPath);
                posters.add(poster);
                return posters;
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }

}


