package com.example.android.getmoviex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class AddMovieActivity extends AppCompatActivity implements ImageTask.CallBack ,HttpRequest.HttpCallback{
    //TODO change Scroller appearance
    private EditText editSubject;
    private EditText editBody;
    private EditText editUrl;
    private ImageView imageView;
    private ScrollView scrollView;
    private Movie m;
    private RelativeLayout relativeLayout;
    private Intent intent;
    ProgressDialog progressDialog;
    private static String IMAGE_KEY = "https://image.tmdb.org/t/p/w500/";
    private static int IMAGE_VIEW_WIDTH_DP=150;
    private static int IMAGE_VIEW_HEiGHT_DP=225;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        intent=getIntent();
        scrollView=findViewById(R.id.scroll);
        relativeLayout=findViewById(R.id.relLayout);
        MyApp.setBackground(scrollView);
        editBody=findViewById(R.id.addMovieEdit);
        editSubject=findViewById(R.id.movieNameEdit);
        editUrl=findViewById(R.id.imageUrlEdit);
        editBody.setScroller(new Scroller(this));
        imageView=findViewById(R.id.imageURL);
        LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)imageView.getLayoutParams();
        params.width=PixelCalc.getPixels(IMAGE_VIEW_WIDTH_DP);
        params.height=PixelCalc.getPixels(IMAGE_VIEW_HEiGHT_DP);
        imageView.setLayoutParams(params);
        if(intent.hasExtra("movie")) {
            m = (Movie) intent.getSerializableExtra("movie");
            if (m != null) {
                editBody.setText(m.getBody());
                editSubject.setText(m.getSubject());
                editUrl.setText(m.getUrl());
                if (m.getUrl() != null) {
                    try {
                        File file = new File(m.getUrl(), m.getSubject() + ".jpg");
                        Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                        imageView.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            if(intent.hasExtra("title")){
            String title=intent.getStringExtra("title");
                String request=title.replace(" ","+");
                MovieSearchRequest movieSearchRequest=new MovieSearchRequest(this);
                movieSearchRequest.searchMovies(request);
            }
        }
    }
    public void onOkClick(View view) {
        if(!editSubject.getText().toString().equals("")) {
            Intent intentOk = new Intent();
            int id = 0;
            if (m != null) {
                id = m.getId();
            }
            intentOk.putExtra("movie", new Movie(id, editSubject.getText().toString(), editBody.getText().toString(), editUrl.getText().toString()));
            if (intent.hasExtra("index")) {
                intentOk.putExtra("index", intent.getIntExtra("index", -1));
            }
            setResult(RESULT_OK, intentOk);
            finish();
        }
        else{
            Toast.makeText(this,"Please Enter Movie Name",Toast.LENGTH_LONG).show();
        }
    }
    public void onCancelClick(View view) {
        finish();
    }
    public void onShowClick(View view) {
        ImageTask imageTask=new ImageTask(this);
        imageTask.execute(editUrl.getText().toString());
    }
    @Override
    public void preExeucute() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Downloading");
        progressDialog.setMessage("please wait....");
        progressDialog.show();
    }
    @Override
    public void onSuccess(Bitmap bitmap,int index,int requestCode) {
        imageView.setImageBitmap(bitmap);
        progressDialog.dismiss();

    }
    @Override
    public void onFail(int index,int requestCode) {
        progressDialog.dismiss();
        Toast.makeText(this,"Failed to load Picture from url",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void preExecuteHttp() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading data");
        progressDialog.setMessage("please wait....");
        progressDialog.show();
    }

    @Override
    public void onSuccessHttp(String downloaded) {

    }

    @Override
    public void onErrorHttp(String errormsg) {

    }
}
