package com.example.android.getmoviex;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DialogOptions extends DialogFragment {
    private Actions actions;
    private Movie movie;
    private int index;
    public interface Actions{
        public void update(Movie movie,int index);
        public void delete(Movie movie);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity) {
            try {
                actions = (Actions) context;
            } catch (ClassCastException cce) {
                throw new ClassCastException(context.toString() + "must implemnt actions");
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity(),R.style.Theme_AppCompat_Dialog_Alert);
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

        AlertDialog dialog=builder.create();
        dialog.show();

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_gradient);
        Button pb=dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pb.setTextColor(getActivity().getResources().getColor(R.color.white,null));
        Button nb=dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nb.setTextColor(getActivity().getResources().getColor(R.color.white,null));
        return  dialog;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
