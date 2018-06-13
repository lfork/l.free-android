package com.lfork.a98620.lfree.settings;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.lfork.a98620.lfree.R;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_act);

        setupActionBar();
    }
    public void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayShowTitleEnabled(true);
        actionBar.setTitle("设置");
        actionBar.setDisplayHomeAsUpEnabled(true); // 决定左上角图标的右侧是否有向左的小箭头, true
        // 有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.common_action_bar, menu);
//        MenuItem item = menu.getItem(0);
//        item.setTitle("编辑");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
//            case R.id.menu1:
//                Intent intent = new Intent(UserInfoThisActivity.this, UserInfoEditActivity.class);
//                startActivityForResult(intent, 4);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
