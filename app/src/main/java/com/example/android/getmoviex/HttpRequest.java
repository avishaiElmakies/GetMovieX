package com.example.android.getmoviex;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpRequest extends AsyncTask<String,Void,String> {

    private String errorMsg;
    private HttpCallback httpCallback;
    public interface HttpCallback{
        void preExecuteHttp();
        void onSuccessHttp(String downloaded);
        void onErrorHttp(String errormsg);
    }
    public HttpRequest(HttpCallback httpCallback){
        this.httpCallback=httpCallback;
    }
    @Override
    protected void onPreExecute() {
        httpCallback.preExecuteHttp();
    }

    @Override
    protected String doInBackground(String... strings) {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            int httpStatus = urlConnection.getResponseCode();
            if (httpStatus != HttpURLConnection.HTTP_OK) {
                errorMsg = urlConnection.getResponseMessage();
                return null;
            }
            inputStream = urlConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String downloadText = "";
            String oneLine = bufferedReader.readLine();
            while (oneLine != null) {
                downloadText += oneLine;
                oneLine = bufferedReader.readLine();
            }
            return downloadText;
        } catch (Exception e) {
            errorMsg = e.getMessage();
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(String download) {
        if(errorMsg==null){
            httpCallback.onSuccessHttp(download);
        }
        else{
            httpCallback.onErrorHttp(errorMsg);
        }
    }
}
