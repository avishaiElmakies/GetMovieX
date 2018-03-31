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
        void preExeucute();
        void onSuccess(Bitmap bitmap,int index,int requestCode);
        void onFail(int index,int requestCode);
    }
    private CallBack callBack;
    private int index;
    private int request;
    public ImageTask(CallBack callBack){
        this.callBack=callBack;
        index=-1;
        this.request=-1;
    }
    public ImageTask(CallBack callBack,int index,int request){
        this.callBack=callBack;
        this.index=index;
        this.request=request;
    }
    @Override
    protected void onPreExecute() {
        callBack.preExeucute();
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
            callBack.onSuccess(bitmap,index,request);
        }
        else
            callBack.onFail(index,request);
    }

}
