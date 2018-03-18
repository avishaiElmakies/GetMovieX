package com.example.android.getmoviex;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {
    private LayoutInflater layoutInflater;
    private TextView txtSubject;
    private TextView txtDes;
    private ImageView imageView;

    private final static float MAX_DES_HEIGHT=MyApp.getContext().getResources().getDimension(R.dimen.max_height);
    private final static float TEXT_SIZE=MyApp.getContext().getResources().getDimension(R.dimen.text_size);
    private final static float MOVIE_WIDTH=MyApp.getContext().getResources().getDimension(R.dimen.movie_width);
    public MovieAdapter(@NonNull Context context, int resource, @NonNull List<Movie> objects) {
        super(context, resource, objects);
        layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RelativeLayout relativeLayout=(RelativeLayout) layoutInflater.inflate(R.layout.main_item_layout,null);
        txtSubject=relativeLayout.findViewById(R.id.textSubject);
        txtDes=relativeLayout.findViewById(R.id.textDes);
        imageView=relativeLayout.findViewById(R.id.imageView);
        RelativeLayout.LayoutParams imgParams=(RelativeLayout.LayoutParams)imageView.getLayoutParams();
        int px=PixelCalc.getPixels(MOVIE_WIDTH);
        imgParams.width=px;
        imageView.setLayoutParams(imgParams);
        px=PixelCalc.getPixels(MAX_DES_HEIGHT);
        txtDes.setMaxHeight(px);
        px=PixelCalc.getPixels(TEXT_SIZE);
        txtDes.setTextSize(px);
        txtSubject.setTextSize(px);
        Movie movie=getItem(position);
        txtSubject.setText(movie.getSubject());
        txtDes.setText(movie.getSubject());

        return relativeLayout;
    }

}
