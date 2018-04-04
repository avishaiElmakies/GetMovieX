package com.example.android.getmoviex;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private final static int ITEM_HEIGHT_DP = 138;
    private final static int IMG_WIDTH_DP = 60;
    private final static int IMG_HEIGHT_DP = 90;
    private final static int IMG_SHARE_SIZE=20;
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
        final Movie movie = getItem(position);
        txtSubject.setText(movie.getSubject());
        txtDes.setText(movie.getBody());
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
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_SEND);
                File file=new File(movie.getUrl());
                Uri uri= FileProvider.getUriForFile(MyApp.getContext(),BuildConfig.APPLICATION_ID+".provider",file);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_TEXT,movie.getSubject()+":"+movie.getBody());
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                MyApp.getContext().startActivity(Intent.createChooser(intent,"select an app to share"));
            }
        });
        return relativeLayout;
    }
}





