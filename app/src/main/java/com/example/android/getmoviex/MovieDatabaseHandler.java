package com.example.android.getmoviex;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MovieDatabaseHandler extends SQLiteOpenHelper {
    public MovieDatabaseHandler(){
        super(MyApp.getContext(),"MovieDatabase",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Movies(_id INTEGER PRIMARY KEY AUTOINCREMENT,subject TEXT NOT NULL,body TEXT,url TEXT,rating INTEGER,watched BIT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE MOVIES");
        onCreate(db);
    }
    public void addMovie(Movie movie){



        String str=String.format(("INSERT INTO Movies(subject,body,url) VALUES('%s','%s','%s')"),movie.getSubject().replace("\'","%&"),
                                                                                                 movie.getBody().replace("\'","%&"),
                                                                                                 movie.getUrl().replace("\'","%&"));
        SQLiteDatabase database=getWritableDatabase();
        database.execSQL(str);
        Cursor cursor=database.rawQuery("SELECT last_insert_rowid() ",null);
        cursor.moveToNext();
        int id=cursor.getInt(0);
        movie.setId(id);
        cursor.close();
        database.close();
    }
    public void deleteMovie(Movie movie){
        String sql=String.format("DELETE FROM Movies WHERE _id=%d",movie.getId());
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }
    public void updateMovie(Movie movie){
        ContentValues contentValues=new ContentValues();
        contentValues.put("subject",movie.getSubject());
        contentValues.put("body",movie.getBody());
        contentValues.put("url",movie.getUrl());
        contentValues.put("rating",movie.getRating());
        contentValues.put("watched",(movie.isWatched()?1:0));
        SQLiteDatabase db=getWritableDatabase();
        String str=""+movie.getId();
        db.update("Movies",contentValues,"_id=?",new String[] {""+movie.getId()});
        db.close();
    }
    public ArrayList<Movie> getAllMovies(){
        ArrayList<Movie> list=new ArrayList<>();
        SQLiteDatabase database=getReadableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM Movies",null);
        int idIndex=cursor.getColumnIndex("_id");
        int subjectIndex=cursor.getColumnIndex("subject");
        int bodyIndex=cursor.getColumnIndex("body");
        int urlIndex=cursor.getColumnIndex("url");
        int ratingIndex=cursor.getColumnIndex("rating");
        int watchedIndex=cursor.getColumnIndex("watched");
        while(cursor.moveToNext()){
            int id=cursor.getInt(idIndex);
            String subject=cursor.getString(subjectIndex).replace("%&","\'");
            String body=cursor.getString(bodyIndex).replace("%&","\'");
            String url=cursor.getString(urlIndex).replace("%&","\'");
            int rating=cursor.getInt(ratingIndex);
            boolean watched=(cursor.getInt(watchedIndex)==1);
            list.add(new Movie(id,subject,body,url,rating,watched));
        }
        return list;
    }
    public void deleteAll(ArrayList<Movie> movies){
        for(int i=0;i<movies.size();i++){
            Movie movie=movies.get(i);
            SQLiteDatabase database=getWritableDatabase();
            database.delete("Movies","_id="+movie.getId(),null);
            database.close();
        }
    }
    public Thread getThreadBasedOnRequest(final int request,final Movie movie){
        final Runnable runnable=new Runnable() {
            @Override
            public void run() {
                switch(request){
                    case 1:{
                        addMovie(movie);
                        break;
                    }
                    case 2: {
                        updateMovie(movie);
                        break;
                    }
                    case 3:
                        deleteMovie(movie);
                        break;
                }
            }
        };
        return new Thread(runnable);
    }
}
