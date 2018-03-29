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
    private ArrayList<Movie> myMovie;
    MovieAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myMovie=new ArrayList <>();
        myListView=findViewById(R.id.myListView);
        MyApp.setBackground(myListView);
        myMovie.add(new Movie("Harry Potter","this is the body",""));
        myMovie.add(new Movie("Harry Potter","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",""));
        myMovie.add(new Movie("Harry Potter","this is the secend movie",""));
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
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUST_CODE_ADD_MOVIE){
            if(resultCode==RESULT_OK){
                Movie m=(Movie)data.getSerializableExtra("movie");
                add(m);
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
                        ImageTask imageTask=new ImageTask(this,index);
                        imageTask.execute(m.getUrl());
                    }
                    else{
                        myAdapter.notifyDataSetChanged();
                    }
                }
            }else{
                if(resultCode==RESULT_CANCELED){
                    Toast.makeText(this,"canceled",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    @Override
    public void preExucute() {

    }

    @Override
    public void onSucces(Bitmap bitmap,int index) {
        Movie m=myMovie.get(index);
        //HashMap<String,Bitmap> hashMap=myAdapter.getHashMap();
        //hashMap.remove(m.getUrl());
        //hashMap.put(m.getUrl(),bitmap);
        //myAdapter.notifyDataSetChanged();
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
        myAdapter.notifyDataSetChanged();
        }


    @Override
    public void onFail() {
        myAdapter.notifyDataSetChanged();
    }


    public void add(Movie movie) {
        myMovie.add(movie);
        ImageTask imageTask=new ImageTask(this,myMovie.size()-1);
        imageTask.execute(movie.getUrl());
    }

    @Override
    public void update(Movie movie,int index) {
        Intent intent=new Intent(this,AddMovieActivity.class);
        intent.putExtra("movie",movie);
        intent.putExtra("index",index);
        startActivityForResult(intent,REQUST_CODE_EDIT_MOVIE);
    }

    @Override
    public void delete(Movie movie) {
        myAdapter.remove(movie);
        myAdapter.notifyDataSetChanged();
    }
}
