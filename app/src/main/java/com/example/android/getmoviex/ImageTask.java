package com.example.android.getmoviex;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 24565 on 24/03/2018.
 */

public class ImageTask extends AsyncTask<String,Void,Bitmap>{
    public interface CallBack{
        void preExucute();
        void onSucces(Bitmap bitmap,int index);
        void onFail();
    }
    private CallBack callBack;
    private int index;
    public ImageTask(CallBack callBack){
        this.callBack=callBack;
        index=-1;
    }
    public ImageTask(CallBack callBack,int index){
        this.callBack=callBack;
        this.index=index;
    }
    @Override
    protected void onPreExecute() {
        callBack.preExucute();
    }

    @Override

    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap=null;
        String urlDisplay=strings[0];
        try{
            URL url=new URL(urlDisplay);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input=connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
        }catch (Exception e){
//           Toast.makeText(MyApp.getContext(),"Error",Toast.LENGTH_SHORT).show();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(bitmap!=null) {
            callBack.onSucces(bitmap,index);
        }
        else
            callBack.onFail();
    }

}
