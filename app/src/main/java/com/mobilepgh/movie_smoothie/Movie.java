package com.mobilepgh.movie_smoothie;

import java.util.Date;

public class Movie {
    //movie class
    int id;
    double rating;
    int popularity;
    String title;
    String plot;
    String posterLocation;
    double userRating;

    public int getId() {
        return id;
    }

    public double getRating() {
        return rating;
    }

    public int getPopularity() {
        return popularity;
    }

    public String getTitle() {
        return title;
    }

    public String getPlot() {
        return plot;
    }

    public String getPosterLocation() {
        return posterLocation;
    }

    public double getUserRating() {
        return userRating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setPosterLocation(String posterLocation) {
        this.posterLocation = posterLocation;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    Date releaseDate;

    public Movie() {

    }

}
