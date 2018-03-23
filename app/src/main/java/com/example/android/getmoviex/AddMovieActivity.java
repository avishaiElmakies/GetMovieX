package com.example.android.getmoviex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

public class AddMovieActivity extends AppCompatActivity {
    //TODO change Scroller appearance
    private EditText editSubject;
    private EditText editBody;
    private ImageView imageView;
    private RelativeLayout relativeLayout;
    private static int IMAGE_VIEW_WIDTH_DP=150;
    private static int IMAGE_VIEW_HEiGHT_DP=225;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        relativeLayout=findViewById(R.id.relLayout);
        MyApp.setBackground(relativeLayout);
        editBody=findViewById(R.id.addMovieEdit);
        editBody.setScroller(new Scroller(this));
        imageView=findViewById(R.id.imageURL);
        LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)imageView.getLayoutParams();
        params.width=PixelCalc.getPixels(IMAGE_VIEW_WIDTH_DP);
        params.height=PixelCalc.getPixels(IMAGE_VIEW_HEiGHT_DP);
        imageView.setLayoutParams(params);
    }
}
