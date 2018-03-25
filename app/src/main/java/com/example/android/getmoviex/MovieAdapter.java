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
    //private HashMap<String ,Bitmap > hashMap;
    //private final static float MAX_DES_HEIGHT=MyApp.getContext().getResources().getDimension(R.dimen.max_height);
    //private final static float TEXT_SIZE=MyApp.getContext().getResources().getDimension(R.dimen.text_size);
    //private final static float MOVIE_WIDTH=MyApp.getContext().getResources().getDimension(R.dimen.movie_width);
    private final static int ITEM_HEIGHT_DP = 138;
    private final static int IMG_WIDTH_DP = 90;
    private final static int IMG_HEIGHT_DP = 138;
    public MovieAdapter(@NonNull Context context, @NonNull List<Movie> objects) {
        super(context, 0, objects);
        this.context=context;
        layoutInflater = LayoutInflater.from(context);
        //hashMap=new HashMap<>();
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
       // if(movie.getBitmap()!=null){
       //     imageView.setImageBitmap(movie.getBitmap());
       // }
       // if(hashMap.containsKey(movie.getUrl())){
       //     Bitmap bitmap=hashMap.get(movie.getUrl());
       //     if(bitmap!=null){
       //         imageView.setImageBitmap(bitmap);
        //    }
       // }
       // else {
       //    hashMap.put(movie.getUrl(),null);
       // }
        String path=movie.getUrl();
        if(!path.equals("")){
            try{
                File file=new File(path,movie.getSubject()+".jpg");
                Bitmap bitmap=BitmapFactory.decodeStream(new FileInputStream(file));
                imageView.setImageBitmap(bitmap);
            }catch (FileNotFoundException fnfe){
                fnfe.printStackTrace();
            }
        }
        return relativeLayout;
    }
 //   public HashMap<String,Bitmap> getHashMap(){
  //      return hashMap;
  //  }
}





