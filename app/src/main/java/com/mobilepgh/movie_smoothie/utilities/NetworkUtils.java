package com.mobilepgh.movie_smoothie.utilities;

import android.net.Uri;
import android.util.Log;

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

import static com.android.volley.VolleyLog.TAG;

public class NetworkUtils {
    private static String baseURL = "https://api.themoviedb.org/3/movie/";
    private static String APIkey;
    //private static String sortBy = "popular";

    public enum SortOrder{
        NOW_PLAYING("now_playing"),
        popular("popular"),
        TOP_RATED("top_rated");
        private String sortOrder;

        SortOrder(String s){
            sortOrder = s;
        }
    }

    public NetworkUtils(){

    }

    public static URL buildURL(int pageNumber, SortOrder sortBy){
        String pageNumberString = Integer.toString(pageNumber);
        Uri builtUri = Uri.parse(baseURL + sortBy).buildUpon()
            .appendQueryParameter("api_key", APIkey)
                .appendQueryParameter("page", pageNumberString)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.d(TAG, "buildURL: " + url.toString());
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

    public int parseJSONPageData(String JSONstring){
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
            Log.d(TAG, "parseJSONPosterData: " + results.length());
            for (int i = 0; i < results.length(); i++) {
                JSONObject movie = results.getJSONObject(i);
                String id = movie.getString("id");
                Log.d(TAG, "parseJSONPosterData: " + id + " " + i);
                String posterPath = movie.getString("poster_path");
                Poster poster = new Poster(Integer.parseInt(id), posterPath);
                posters.add(poster);
            }
            return posters;
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }

}


