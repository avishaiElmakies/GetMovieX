package com.example.android.getmoviex;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements ImageTask.CallBack,DialogOptions.Actions{
    public static int REQUEST_CODE_ADD_MOVIE =1;
    public static int REQUEST_CODE_EDIT_MOVIE =2;
    public static int REQUEST_CODE_DELETE_MOVIE =3;
    private MenuItem addMovieItem;
    private MenuItem addMovieItemInter;
    private MenuItem exitItem;
    private MenuItem deleteAllItem;
    private ListView myListView;
    private MovieDatabaseHandler movieDatabaseHandler;
    private ArrayList<Movie> myMovie;
    private MovieAdapter myAdapter;
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
                startActivityForResult(intent, REQUEST_CODE_EDIT_MOVIE);
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
        addMovieItem=findViewById(R.id.addItem);
        addMovieItemInter=findViewById(R.id.addItemFromInternet);
        exitItem=findViewById(R.id.exitItem);
        deleteAllItem=findViewById(R.id.deleteAll);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.addItem:{
                Intent intent=new Intent(this,AddMovieActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_MOVIE);
                return true;
            }
            case R.id.addItemFromInternet:{
                Intent intent=new Intent(this,SearchActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_MOVIE);
                return true;
            }
            case R.id.exitItem:{
                finish();
                return true;
            }
            case R.id.deleteAll:{
                final AlertDialog.Builder builder=new AlertDialog.Builder(this,R.style.Theme_AppCompat_Dialog_Alert);
                builder.setTitle("Are you sure?");
                builder.setMessage("you can't undo this ");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        movieDatabaseHandler.deleteAll(myMovie);
                        myAdapter.clear();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_gradient);
                Button pb=dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                pb.setTextColor(getResources().getColor(R.color.white,null));
                Button nb=dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                nb.setTextColor(getResources().getColor(R.color.white,null));
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== REQUEST_CODE_ADD_MOVIE){
            if(resultCode==RESULT_OK){
                Movie m=(Movie)data.getSerializableExtra("movie");
                myMovie.add(m);
                ImageTask imageTask=new ImageTask(this,myMovie.size()-1, REQUEST_CODE_ADD_MOVIE);
                imageTask.execute(m.getUrl());
            }else {
                if(resultCode==RESULT_CANCELED){
                    Toast.makeText(this,"canceled",Toast.LENGTH_SHORT).show();
                }
            }
        }
        else {
            if(requestCode== REQUEST_CODE_EDIT_MOVIE) {
                if(resultCode==RESULT_OK) {
                    Movie m=(Movie)data.getSerializableExtra("movie");
                    int index=data.getIntExtra("index",-1);
                    Movie oldMovie=myMovie.remove(index);
                    myMovie.add(index,m);
                    if(!oldMovie.getUrl().equals(m.getUrl())){
                        ImageTask imageTask=new ImageTask(this,index, REQUEST_CODE_EDIT_MOVIE);
                        imageTask.execute(m.getUrl());
                    }
                    else{
                        myAdapter.notifyDataSetChanged();
                        getThreadBasedOnRequest(requestCode,m).start();
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
        String date=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File path=new File(directory, m.getSubject()+date+".jpg");
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
        m.setUrl(path.getAbsolutePath());
        myAdapter.notifyDataSetChanged();
        getThreadBasedOnRequest(requestCode,m).start();
    }
    @Override
    public void onFail(int index,int requestCode) {
        Movie movie=myMovie.get(index);
        myAdapter.notifyDataSetChanged();
        getThreadBasedOnRequest(requestCode,movie).start();
    }
    @Override
    public void goToUpdate(Movie movie,int index) {
        Intent intent=new Intent(this,AddMovieActivity.class);
        intent.putExtra("movie",movie);
        intent.putExtra("index",index);
        startActivityForResult(intent, REQUEST_CODE_EDIT_MOVIE);
    }
    @Override
    public void delete(final Movie movie) {
        myAdapter.remove(movie);
        getThreadBasedOnRequest(REQUEST_CODE_DELETE_MOVIE,movie).start();
        myAdapter.notifyDataSetChanged();
    }
    //think about making this a static function in databaseHandler//think about adding this to myApp
    public Thread getThreadBasedOnRequest(final int request,final Movie movie){
        final Runnable runnable=new Runnable() {
            @Override
            public void run() {
                switch(request){
                    case 1:{
                        movieDatabaseHandler.addMovie(movie);
                        break;
                    }
                    case 2: {
                        movieDatabaseHandler.updateMovie(movie);
                        break;
                    }
                    case 3:
                        movieDatabaseHandler.deleteMovie(movie);
                        break;
                }
            }
        };
        return new Thread(runnable);
    }
}
