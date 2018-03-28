package com.example.android.getmoviex;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

public class DialogOptions extends DialogFragment {
    private Movie.Actions actions;
    private Movie movie;
    private int index;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            actions=(Movie.Actions)activity;
        }catch(ClassCastException cce){
            throw new ClassCastException(activity.toString()+"must implemnt actions");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit or delete?");
        builder.setMessage("do you want to update or delete this Movie?");
        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                actions.update(movie,index);
            }
        });
        builder.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                actions.delete(movie);
            }
        });
        return  builder.create();
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
