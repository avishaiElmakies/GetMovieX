package com.example.android.getmoviex;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {
    private LayoutInflater layoutInflater;
    private TextView txtSubject;
    private TextView txtDes;
    private ImageView imageView;

    //private final static float MAX_DES_HEIGHT=MyApp.getContext().getResources().getDimension(R.dimen.max_height);
    //private final static float TEXT_SIZE=MyApp.getContext().getResources().getDimension(R.dimen.text_size);
    //private final static float MOVIE_WIDTH=MyApp.getContext().getResources().getDimension(R.dimen.movie_width);
    private final static int ITEM_HEIGHT_DP=138;
    private final static int IMG_WIDTH_DP=90;
    private final static int IMG_HEIGHT_DP=138;
    public MovieAdapter(@NonNull Context context, @NonNull List<Movie> objects) {
        super(context,0,objects);
        layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RelativeLayout relativeLayout=(RelativeLayout) layoutInflater.inflate(R.layout.main_item_layout,null);
        txtSubject=relativeLayout.findViewById(R.id.textSubject);
        txtDes=relativeLayout.findViewById(R.id.textDes);
        imageView=relativeLayout.findViewById(R.id.imageView);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,PixelCalc.getPixels(ITEM_HEIGHT_DP));
        relativeLayout.setLayoutParams(params);
        RelativeLayout.LayoutParams imgParams=(RelativeLayout.LayoutParams)imageView.getLayoutParams();
        imgParams.height=PixelCalc.getPixels(IMG_HEIGHT_DP);
        imgParams.height=PixelCalc.getPixels(IMG_WIDTH_DP);
        imgParams.addRule(RelativeLayout.CENTER_VERTICAL);
        imageView.setLayoutParams(imgParams);
        Movie movie=getItem(position);
        txtSubject.setText(movie.getSubject());
        txtDes.setText(movie.getBody());
        return relativeLayout;
    }

}
