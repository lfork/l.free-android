package com.lfork.a98620.lfree.goodsdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lfork.a98620.lfree.R;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_act);
    }


    public static void actionStart(Context context){
        Intent intent = new Intent(context, ImageActivity.class);
        context.startActivity(intent);
    }
}
