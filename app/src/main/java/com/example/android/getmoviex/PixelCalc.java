package com.example.android.getmoviex;

import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by Android on 3/18/2018.
 */

public class PixelCalc {
    public static int getPixels(int dp){
        DisplayMetrics displayMetrics=MyApp.getContext().getResources().getDisplayMetrics();
        float px= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,displayMetrics);
        return Math.round(px);
    }
}
