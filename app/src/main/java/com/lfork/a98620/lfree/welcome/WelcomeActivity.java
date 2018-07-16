package com.lfork.a98620.lfree.welcome;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.base.FreeApplication;
import com.lfork.a98620.lfree.login.LoginActivity;
import com.lfork.a98620.lfree.util.GlideOptions;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";
    private WelcomeActivity welcomeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_act);
        ImageView welcome = findViewById(R.id.welcome_image);
        GlideOptions options = GlideOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).skipMemoryCache(true);
        Glide.with(this).load(R.drawable.welcome_pic).apply(options).into(welcome);
        welcomeActivity = this;
        hideSystemNavigationBar();

        FreeApplication.getDefaultThreadPool().execute(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                Intent intent = new Intent(welcomeActivity, LoginActivity.class);
                welcomeActivity.startActivity(intent);
                welcomeActivity.finish();
            });
        });


    }

    private void hideSystemNavigationBar() {
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

}
