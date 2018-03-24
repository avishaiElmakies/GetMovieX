package com.example.android.getmoviex;

import android.content.Intent;
import android.graphics.Bitmap;
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

public class AddMovieActivity extends AppCompatActivity implements ImageTask.CallBack {
    //TODO change Scroller appearance
    //todo add show picture
    private EditText editSubject;
    private EditText editBody;
    private EditText editUrl;
    private ImageView imageView;
    private ScrollView scrollView;
    private RelativeLayout relativeLayout;
    private static int IMAGE_VIEW_WIDTH_DP=150;
    private static int IMAGE_VIEW_HEiGHT_DP=225;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        Intent intent=getIntent();
        Movie m=(Movie) intent.getSerializableExtra("movie");
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
        if(m!=null){
            editBody.setText(m.getBody());
            editSubject.setText(m.getSubject());
            editUrl.setText(m.getUrl());
            imageView.setImageBitmap(m.getBitmap());
        }
    }

    public void onOkClick(View view) {
       Intent intent=new Intent();
        intent.putExtra("movie",new Movie(editSubject.getText().toString(),editBody.getText().toString(),editUrl.getText().toString()));
        setResult(RESULT_OK,intent);
        finish();
    }

    public void onCancelClick(View view) {
        finish();
    }

    public void onShowClick(View view) {
        ImageTask imageTask=new ImageTask(this);
        imageTask.execute(editUrl.getText().toString());
    }

    @Override
    public void preExucute() {
        Toast.makeText(this,"Starting",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSucces(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onFail() {

    }
}
