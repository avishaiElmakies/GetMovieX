package com.example.android.getmoviex;

import android.app.Application;
import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Android on 3/18/2018.
 */

public class MyApp extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context=getApplicationContext();
        super.onCreate();
    }
    public static Context getContext(){
        return context;
    }
    public static void setBackground(ViewGroup layout){
        layout.setBackground(context.getDrawable(R.drawable.background_gradient));
    }
}
