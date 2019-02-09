package com.mobilepgh.movie_smoothie;

import java.util.Date;

public class Movie {
    //movie class
    double rating;
    int popularity;
    String title;
    String plot;
    double userRating;
    Date releaseDate;

    public Movie(double rating, int popularity, String title, String plot, double userRating, Date releaseDate) {
        this.rating = rating;
        this.popularity = popularity;
        this.title = title;
        this.plot = plot;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }
}
