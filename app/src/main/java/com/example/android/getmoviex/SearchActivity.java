package com.example.android.getmoviex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class SearchActivity extends AppCompatActivity {
    private ListView listViewInternet;
    private ArrayAdapter<String> arrayAdapter;
    private LinearLayout linearLayout;
    private static int pageNum;
    Button preButton;
    Button nextButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        linearLayout=findViewById(R.id.linearLayout);
        MyApp.setBackground(linearLayout);
        listViewInternet=findViewById(R.id.listViewInternet);
        preButton=findViewById(R.id.buttonPrevPage);
        nextButton=findViewById(R.id.buttonNextPage);
        preButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);

    }

    public void searchClick(View view) {
        pageNum=1;
        startSearch(pageNum);
        nextButton.setVisibility(View.VISIBLE);
        preButton.setVisibility(View.VISIBLE);
        preButton.setEnabled(false);
    }

    public void nextPageSearch(View view) {
        pageNum++;
        startSearch(pageNum);
        preButton.setEnabled(true);
    }

    public void prevPageSearch(View view) {
        pageNum--;
        if(pageNum!=0){
            startSearch(pageNum);
        }
        if(pageNum==1){
            preButton.setEnabled(false);
        }
    }
    public void startSearch(int page){
        EditText editText=findViewById(R.id.searchText);
        MovieSearchRequest movieSearchRequset=new MovieSearchRequest(this);
        String str=editText.getText().toString();
        String request=str.replace(" ","+");
        movieSearchRequset.searchMovies(request,page);
    }
}
