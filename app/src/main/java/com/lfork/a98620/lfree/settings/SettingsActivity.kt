package com.lfork.a98620.lfree.settings

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.lfork.a98620.lfree.R
import java.util.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_act)
        setupActionBar()
    }

    private fun setupActionBar() {
        val actionBar = supportActionBar
        Objects.requireNonNull<ActionBar>(actionBar).setDisplayShowTitleEnabled(true)
        actionBar!!.title = "设置"
        // 决定左上角图标的右侧是否有向左的小箭头, true
        actionBar.setDisplayHomeAsUpEnabled(true)
        // 有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //        getMenuInflater().inflate(R.menu.common_action_bar, menu);
        //        MenuItem item = menu.getItem(0);
        //        item.setTitle("编辑");
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            //            case R.id.menu1:
            //                Intent intent = new Intent(UserInfoThisActivity.this, UserInfoEditActivity.class);
            //                startActivityForResult(intent, 4);
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

