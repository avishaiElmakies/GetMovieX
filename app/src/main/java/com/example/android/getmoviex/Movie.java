package com.example.android.getmoviex;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Android on 3/18/2018.
 */

public class Movie implements Serializable {
    public interface Actions{
        public void add(Movie movie);
        public void update(Movie movie,int index);
        public void delete(Movie movie);
    }
    private int id;
    private String subject;
    private String body;
    private String url;
   // private Bitmap bitmap;
    public Movie(){

    }
    public Movie(String subject,String body,String url){
        this.subject=subject;
        this.body=body;
        this.url=url;
    }
    public Movie(int id ,String subject,String body,String url){
        this.id=id;
        this.subject=subject;
        this.body=body;
        this.url=url;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
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
  /*  public Bitmap getBitmap(){
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap){
        this.bitmap=bitmap;
    }*/
}
