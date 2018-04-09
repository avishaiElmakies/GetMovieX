package com.example.android.getmoviex;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.StrictMode;
import android.os.TokenWatcher;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> { //implements ImageTask.CallBack {
    private LayoutInflater layoutInflater;
    private TextView txtSubject;
    private TextView txtDes;
    private ImageView imageView;
    private Context context;
    private ImageView imgShare;
    private SeekBar seekBar;
    private ToggleButton toggleButton;
    private final static int ITEM_HEIGHT_DP = 165;
    private final static int IMG_WIDTH_DP = 60;
    private final static int IMG_HEIGHT_DP = 90;
    private final static int IMG_SHARE_SIZE=20;
    private final static int SEEK_HEIGHT=20;
    public MovieAdapter(@NonNull Context context, @NonNull List<Movie> objects) {
        super(context, 0, objects);
        this.context=context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RelativeLayout relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.main_item_layout, null);
        txtSubject = relativeLayout.findViewById(R.id.textSubject);
        txtDes = relativeLayout.findViewById(R.id.textDes);
        imageView = relativeLayout.findViewById(R.id.imageView);
        imgShare=relativeLayout.findViewById(R.id.imageShare);
        seekBar=relativeLayout.findViewById(R.id.seekBar);
        toggleButton=relativeLayout.findViewById(R.id.toggleButton);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, PixelCalc.getPixels(ITEM_HEIGHT_DP));
        relativeLayout.setLayoutParams(params);
        LinearLayout.LayoutParams imgParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        imgParams.height = PixelCalc.getPixels(IMG_HEIGHT_DP);
        imgParams.width = PixelCalc.getPixels(IMG_WIDTH_DP);
        imageView.setLayoutParams(imgParams);
        LinearLayout.LayoutParams imgShareParams = (LinearLayout.LayoutParams) imgShare.getLayoutParams();
        imgShareParams.height = PixelCalc.getPixels(IMG_SHARE_SIZE);
        imgShareParams.height = PixelCalc.getPixels(IMG_SHARE_SIZE);
        imageView.setLayoutParams(imgParams);
        RelativeLayout.LayoutParams seekParams = (RelativeLayout.LayoutParams) seekBar.getLayoutParams();
        seekParams.height = PixelCalc.getPixels(SEEK_HEIGHT);
        seekBar.setLayoutParams(seekParams);
        final Movie movie = getItem(position);
        txtSubject.setText(movie.getSubject());
        txtDes.setText(movie.getBody());
        seekBar.setProgress(movie.getRating());
        toggleButton.setChecked(movie.isWatched());
        changeSeekColor(seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                changeSeekColor(seekBar);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                movie.setRating(seekBar.getProgress());
                new MovieDatabaseHandler().getThreadBasedOnRequest(MainActivity.REQUEST_CODE_EDIT_MOVIE,movie).start();
            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    movie.setWatched(b);
                new MovieDatabaseHandler().getThreadBasedOnRequest(MainActivity.REQUEST_CODE_EDIT_MOVIE,movie).start();

            }
        });

        String path=movie.getUrl();
        if(!path.equals("")){
            try{
                File file=new File(path);
                Bitmap bitmap=BitmapFactory.decodeStream(new FileInputStream(file));
                imageView.setImageBitmap(bitmap);
            }catch (FileNotFoundException fnfe){
                fnfe.printStackTrace();
            }catch (NullPointerException npe){
                npe.printStackTrace();
            }
        }
        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                try {
                    File file = new File(movie.getUrl());
                    Uri uri = FileProvider.getUriForFile(MyApp.getContext(), BuildConfig.APPLICATION_ID + ".provider", file);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setType("image/*");
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(intent.getType()==null){
                        intent.setType("text/plain");
                    }
                    intent.putExtra(Intent.EXTRA_TEXT,movie.getSubject()+":"+movie.getBody());
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    MyApp.getContext().startActivity(Intent.createChooser(intent,"select an app to share"));
                }

            }
        });
        return relativeLayout;
    }
    public void changeSeekColor(SeekBar seekBar){
        if(seekBar.getProgress()>=7) {
            seekBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            seekBar.getThumb().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
        }
        if(seekBar.getProgress()>=4 && seekBar.getProgress()<7)
        {
            seekBar.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
            seekBar.getThumb().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        }
        if(seekBar.getProgress()<=3) {
            seekBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
            seekBar.getThumb().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

        }
    }
}





