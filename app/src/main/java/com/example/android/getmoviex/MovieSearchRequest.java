package com.example.android.getmoviex;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieSearchRequest implements HttpRequest.HttpCallback {
    private static String HTTP_KEY = "27ed300656700faa90fce59d65e3481e";

    private static String RQEUST_URL ="https://api.themoviedb.org/3/search/movie?api_key=27ed300656700faa90fce59d65e3481e&query=%s";
    private static String PAGE_RQEUST="&page=%d";
    private Activity activity;
    private ListView searchListView;
    private ProgressDialog progressDialog;
    private ArrayList<String> movieNames;
    public MovieSearchRequest(Activity activity){
        this.activity=activity;
        searchListView=activity.findViewById(R.id.listViewInternet);
        progressDialog=new ProgressDialog(activity);
        movieNames=new ArrayList<>();
        progressDialog.setTitle("Downloading");
        progressDialog.setMessage("please wait....");
    }
    public void searchMovies(String searchQuary){
        String request=String.format(RQEUST_URL,searchQuary);
        HttpRequest httpRequest=new HttpRequest(this);
        httpRequest.execute(request);
    }
    public void searchMovies(String searchQuary,int pageNum){
        String request=String.format(RQEUST_URL+PAGE_RQEUST,searchQuary,pageNum);
        HttpRequest httpRequest=new HttpRequest(this);
        httpRequest.execute(request);
    }
    @Override
    public void preExecuteHttp() {
        progressDialog.show();
    }


    @Override
    public void onSuccessHttp(String downloaded) {
        try{
            JSONObject baseJsonObject=new JSONObject(downloaded);
            final JSONArray jsonArray=baseJsonObject.getJSONArray("results");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String title=jsonObject.getString("title");
                movieNames.add(title);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
            return;
        }
        if(!movieNames.isEmpty()) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, R.layout.internet_item, R.id.textViewInternt, movieNames);
            searchListView.setAdapter(arrayAdapter);
        }else{
            Toast.makeText(activity,"No more Pages",Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String title=(String)adapterView.getItemAtPosition(i);
                Intent intent=new Intent(activity,AddMovieActivity.class);
                intent.putExtra("title",title);
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public void onErrorHttp(String errorMsg) {
        Toast.makeText(activity,errorMsg,Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }
}
