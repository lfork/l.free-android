package com.lfork.a98620.lfree.ImageBrowser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lfork.a98620.lfree.R;

/**
 * 图片浏览器
 */
public class ImageBrowserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_browser_act);
    }


    public static void actionStart(Context context){
        Intent intent = new Intent(context, ImageBrowserActivity.class);
        context.startActivity(intent);
    }
}
