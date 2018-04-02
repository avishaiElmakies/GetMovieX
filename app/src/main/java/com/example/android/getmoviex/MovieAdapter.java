package com.example.android.getmoviex;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> { //implements ImageTask.CallBack {
    private LayoutInflater layoutInflater;
    private TextView txtSubject;
    private TextView txtDes;
    private ImageView imageView;
    private Context context;
    private final static int ITEM_HEIGHT_DP = 138;
    private final static int IMG_WIDTH_DP = 90;
    private final static int IMG_HEIGHT_DP = 138;
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
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, PixelCalc.getPixels(ITEM_HEIGHT_DP));
        relativeLayout.setLayoutParams(params);
        RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        imgParams.height = PixelCalc.getPixels(IMG_HEIGHT_DP);
        imgParams.height = PixelCalc.getPixels(IMG_WIDTH_DP);
        imgParams.addRule(RelativeLayout.CENTER_VERTICAL);
        imageView.setLayoutParams(imgParams);
        Movie movie = getItem(position);
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
        return relativeLayout;
    }
}





