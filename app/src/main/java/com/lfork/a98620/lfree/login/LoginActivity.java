package com.lfork.a98620.lfree.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.main.MainActivity;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_act);
        ImageView loginLogo = findViewById(R.id.login_logo);
        Glide.with(this).load(getResources().getDrawable(R.drawable.login_bg))
                .into(loginLogo);
        Log.d(TAG, "onCreate:  image setted successfully");
    }

    private void startApp(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
