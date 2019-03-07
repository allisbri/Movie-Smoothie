package com.mobilepgh.movie_smoothie.utilities;

import android.content.Context;
import android.net.Uri;

import com.mobilepgh.movie_smoothie.R;
import com.mobilepgh.movie_smoothie.entities.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class NetworkUtils {
    private static String baseURL = "https://api.themoviedb.org/3/movie/";

    public enum SortOrder{
        NOW_PLAYING("now_playing"),
        POPULAR("popular"),
        TOP_RATED("top_rated");
        private String sortOrder;

        public String getSortOrder() {
            return sortOrder;
        }

        SortOrder(String s){
            sortOrder = s;
        }
    }

    public NetworkUtils(){
    }

    public static URL buildMovieListURL(String APIkey, int pageNumber, SortOrder sortBy){
        String pageNumberString = Integer.toString(pageNumber);
        Uri builtUri = Uri.parse(baseURL).buildUpon()
                .appendPath(sortBy.getSortOrder())
                .appendQueryParameter("api_key", APIkey)
                .appendQueryParameter("page", pageNumberString)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildMovieDetailsURL(String APIkey, int movieId){
        Uri builtUri = Uri.parse(baseURL).buildUpon()
                .appendQueryParameter("api_key", APIkey)
                .appendPath(Integer.toString(movieId))
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

    public static ArrayList<Movie> parseJSONMoviePosterData(String JSONstring){
        try{
            JSONObject fullResponse = new JSONObject(JSONstring);
            JSONArray results = fullResponse.getJSONArray("results");
            ArrayList<Movie> movies = new ArrayList<>();
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieJSONObject = results.getJSONObject(i);
                String id = movieJSONObject.getString("id");
                String posterPath = movieJSONObject.getString("poster_path");
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(id));
                movie.setPosterPath(posterPath);
                movies.add(movie);
            }
            return movies;
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Movie parseJSONMovieDetailsData(String JSONstring){
        try{
            Movie movie = new Movie();
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            JSONObject movieJSONObject = new JSONObject(JSONstring);
            String stringId = movieJSONObject.getString("id");
            String posterPath = movieJSONObject.getString("poster_path");
            String title = movieJSONObject.getString("original_title");
            String plot = movieJSONObject.getString("overview");
            String releaseDateString = movieJSONObject.getString("release_date");
            double rating = Double.parseDouble(movieJSONObject.getString("vote_average"));
            movie.setId(Integer.parseInt(stringId));
            movie.setPosterPath(posterPath);
            movie.setTitle(title);
            movie.setPlot(plot);
            movie.setRating(rating);
            try {
                Date releaseDate = dateFormat.parse(releaseDateString);
                movie.setReleaseDate(releaseDate);
            }
            catch(ParseException e){
                e.printStackTrace();
            }



            return movie;
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}


