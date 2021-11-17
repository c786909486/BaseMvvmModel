package com.ckz.baselibrary.utils;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ToolbarUtils {

    public static void setToolbar(final AppCompatActivity activity, Toolbar toolbar){
       activity. setSupportActionBar(toolbar);
       activity. getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       activity. getSupportActionBar().setDisplayShowHomeEnabled(true);
       activity. getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity. finish();
            }
        });
    }

}
