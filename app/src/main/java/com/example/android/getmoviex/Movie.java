package com.example.android.getmoviex;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Android on 3/18/2018.
 */

public class Movie implements Serializable {
    private int id;
    private String subject;
    private String body;
    private String url;
    private int rating;
    private boolean watched;

   // private Bitmap bitmap;
    public Movie(String subject,String body,String url){
        setSubject(subject);
        setBody(body);
        setUrl(url);
        setRating(0);
        setWatched(false);
    }
    public Movie(int id ,String subject,String body,String url,int rating,boolean watched){
        setId(id);
        setSubject(subject);
        setBody(body);
        setUrl(url);
        setRating(rating);
        setWatched(watched);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }
}
