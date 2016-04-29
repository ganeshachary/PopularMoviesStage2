package com.spottechnician.popularmovies.data;

/**
 * Created by OnesTech on 23/04/2016.
 */
public class MovieContract {
    String id;
    String title;
    String overview;
    String date;
    String vote;
    String posterpath;
    String reviewurl;
    String trailorurl;

    public MovieContract() {

    }

    public MovieContract(String id, String title, String overview,
                         String date, String vote, String posterpath,
                         String reviewurl, String trailorurl) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.date = date;
        this.vote = vote;
        this.posterpath = posterpath;
        this.reviewurl = reviewurl;
        this.trailorurl = trailorurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getPosterpath() {
        return posterpath;
    }

    public void setPosterpath(String posterpath) {
        this.posterpath = posterpath;
    }

    public String getReviewurl() {
        return reviewurl;
    }

    public void setReviewurl(String reviewurl) {
        this.reviewurl = reviewurl;
    }

    public String getTrailorurl() {
        return trailorurl;
    }

    public void setTrailorurl(String trailorurl) {
        this.trailorurl = trailorurl;
    }
}
