package com.example.android.getmoviex;

import android.app.Application;
import android.content.Context;

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
}
