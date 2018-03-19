package com.example.android.getmoviex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    private static int REQUST_CODE_ADD_MOVIE=1;
    private static int REQUST_CODE_EDIT_MOVIE=2;
    private MenuItem addMovieItem;
    private MenuItem addMovieItemInter;
    private MenuItem exitItem;
    private MenuItem deleteAllItem;
    private ListView myListView;
    private ArrayList<Movie> myMovie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myMovie=new ArrayList <>();
        myListView=findViewById(R.id.myListView);
        MyApp.setBackground(myListView);
        myMovie.add(new Movie("Harry Potter","this is the body","url"));
        myMovie.add(new Movie("Harry Potter","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","url"));
        myMovie.add(new Movie("Harry Potter","this is the secend movie","url"));
        MovieAdapter myAdapter=new MovieAdapter(this,myMovie);
        myListView.setAdapter(myAdapter);

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
}
