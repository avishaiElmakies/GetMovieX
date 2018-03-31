package com.example.android.getmoviex;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InterruptedIOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements ImageTask.CallBack,DialogOptions.Actions{
    private static int REQUST_CODE_ADD_MOVIE=1;
    private static int REQUST_CODE_EDIT_MOVIE=2;
    private MenuItem addMovieItem;
    private MenuItem addMovieItemInter;
    private MenuItem exitItem;
    private MenuItem deleteAllItem;
    private ListView myListView;
    MovieDatabaseHandler movieDatabaseHandler;
    private ArrayList<Movie> myMovie;
    MovieAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieDatabaseHandler=new MovieDatabaseHandler();
        myMovie=movieDatabaseHandler.getAllMovies();
        myListView=findViewById(R.id.myListView);
        MyApp.setBackground(myListView);
        myAdapter=new MovieAdapter(this,myMovie);
        myListView.setAdapter(myAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie m =(Movie)adapterView.getItemAtPosition(i);
                Intent intent=new Intent(getBaseContext(),AddMovieActivity.class);
                intent.putExtra("movie",m);
                intent.putExtra("index",i);
                startActivityForResult(intent,REQUST_CODE_EDIT_MOVIE);
            }
        });
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view,final int index, long l) {
                DialogOptions dialogOptions=new DialogOptions();
                dialogOptions.setMovie((Movie)adapterView.getItemAtPosition(index));
                dialogOptions.setIndex(index);
                dialogOptions.show(getSupportFragmentManager(),"dialog");
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        addMovieItem=findViewById(R.id.addItem);//todo create activity addMovie
        addMovieItemInter=findViewById(R.id.addItemFromInternet);//todo add activity addMovieInternet
        exitItem=findViewById(R.id.exitItem);// todo make the app exit
        deleteAllItem=findViewById(R.id.deleteAll);//todo make app delete all
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.addItem:{
                Intent intent=new Intent(this,AddMovieActivity.class);
                startActivityForResult(intent,REQUST_CODE_ADD_MOVIE);
                return true;
            }
            case R.id.addItemFromInternet:{
                Intent intent=new Intent(this,SearchActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUST_CODE_ADD_MOVIE){
            if(resultCode==RESULT_OK){
                Movie m=(Movie)data.getSerializableExtra("movie");
                myMovie.add(m);
                ImageTask imageTask=new ImageTask(this,myMovie.size()-1,REQUST_CODE_ADD_MOVIE);
                imageTask.execute(m.getUrl());
                //todo: add sqlHelper
            }else {
                if(resultCode==RESULT_CANCELED){
                    Toast.makeText(this,"canceled",Toast.LENGTH_SHORT).show();
                }
            }
        }
        else {
            if(requestCode==REQUST_CODE_EDIT_MOVIE) {
                if(resultCode==RESULT_OK) {
                    Movie m=(Movie)data.getSerializableExtra("movie");
                    int index=data.getIntExtra("index",-1);
                    Movie oldMovie=myMovie.remove(index);
                    myMovie.add(index,m);
                    if(!oldMovie.getUrl().equals(m.getUrl())){
                        ImageTask imageTask=new ImageTask(this,index,REQUST_CODE_EDIT_MOVIE);
                        imageTask.execute(m.getUrl());
                    }
                    else{
                        myAdapter.notifyDataSetChanged();
                    }

                }else{
                    if(resultCode==RESULT_CANCELED){
                        Toast.makeText(this,"canceled",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }



    @Override
    public void preExeucute() {

    }

    @Override
    public void onSuccess(Bitmap bitmap,int index,int requestCode) {
        Movie m=myMovie.get(index);
        ContextWrapper contextWrapper=new ContextWrapper(MyApp.getContext());
        File directory =contextWrapper.getDir("imageDir", Context.MODE_PRIVATE);
        File path=new File(directory,m.getSubject()+".jpg");
        FileOutputStream fos=null;
        try{
            fos=new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
        }catch (FileNotFoundException fnfe){
            fnfe.printStackTrace();
        }
        finally {
            try {
                fos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        m.setUrl(directory.getAbsolutePath());
        if(requestCode==REQUST_CODE_ADD_MOVIE) {
            startAddingToDatabase(m);
        }
        else if(requestCode==REQUST_CODE_EDIT_MOVIE){
            startUpdatingInDatabase(m);
        }
        myAdapter.notifyDataSetChanged();
    }
    @Override
    public void onFail(int index,int requestCode) {
        Movie movie=myMovie.get(index);
        if(requestCode==REQUST_CODE_ADD_MOVIE) {
            startAddingToDatabase(movie);
        }
        else if(requestCode==REQUST_CODE_EDIT_MOVIE){
           startUpdatingInDatabase(movie);
        }
        myAdapter.notifyDataSetChanged();
    }
    @Override
    public void goToUpdate(Movie movie,int index) {
        Intent intent=new Intent(this,AddMovieActivity.class);
        intent.putExtra("movie",movie);
        intent.putExtra("index",index);
        startActivityForResult(intent,REQUST_CODE_EDIT_MOVIE);
    }
    @Override
    public void delete(final Movie movie) {
        myAdapter.remove(movie);
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                movieDatabaseHandler.deleteMovie(movie);
            }
        };
        Thread thread=new Thread(runnable);
        thread.start();
        myAdapter.notifyDataSetChanged();
    }
    //think about making this a static function in databaseHandler
    public void startAddingToDatabase( final Movie movie){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                movieDatabaseHandler.addMovie(movie);
            }
        };
        Thread add=new Thread(runnable);
        add.start();
    }
    public void startUpdatingInDatabase(final Movie movie){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                movieDatabaseHandler.updateMovie(movie);
            }
        };
        Thread add=new Thread(runnable);
        add.start();
    }
}
