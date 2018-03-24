package com.lfork.a98620.lfree.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.source.UserDataRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act);
        TextView textView = findViewById(R.id.helloWorld);
        UserDataRepository mRepository = UserDataRepository.getInstance();
        textView.setText(mRepository.getThisUser().toString());

    }
}
