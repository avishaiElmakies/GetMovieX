package com.example.android.getmoviex;

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
        db.execSQL("CREATE TABLE Movies(_id INTEGER PRIMARY KEY AUTOINCREMENT,subject TEXT NOT NULL,body TEXT,url TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE MOVIES");
    }
    //todo think about using Async to add to database
    public void addMovie(Movie movie){
        String str=String.format(("INSERT INTO Movies(subject,body,url) VALUES('%s','%s','%s')"),movie.getSubject(),movie.getBody(),movie.getUrl());
        SQLiteDatabase database=getWritableDatabase();
        database.execSQL(str);
        Cursor cursor=database.rawQuery("SELECT last_insert_rowid() ",null);
        int idIndex=cursor.getColumnIndex("_id");
        movie.setId(idIndex);
        cursor.close();
        database.close();
    }
    public ArrayList<Movie> getAllMovies(){
        ArrayList<Movie> list=new ArrayList<>();
        SQLiteDatabase database=getReadableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM Movies",null);
        int idIndex=cursor.getColumnIndex("_id");
        int subjectIndex=cursor.getColumnIndex("subject");
        int bodyIndex=cursor.getColumnIndex("body");
        int urlIndex=cursor.getColumnIndex("url");
        while(cursor.moveToNext()){
            int id=cursor.getInt(idIndex);
            String subject=cursor.getString(subjectIndex);
            String body=cursor.getString(bodyIndex);
            String url=cursor.getString(urlIndex);
            list.add(new Movie(id,subject,body,url));
        }
        return list;
    }
}
