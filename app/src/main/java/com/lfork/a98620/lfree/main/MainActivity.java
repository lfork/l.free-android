package com.lfork.a98620.lfree.main;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.base.FreeApplication;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.base.entity.User;
import com.lfork.a98620.lfree.data.community.local.CommunityLocalDataSource;
import com.lfork.a98620.lfree.data.goods.GoodsDataRepository;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.databinding.MainActBinding;
import com.lfork.a98620.lfree.imservice.MessageService;
import com.lfork.a98620.lfree.main.chatlist.ChatListFragment;
import com.lfork.a98620.lfree.main.community.CommunityFragment;
import com.lfork.a98620.lfree.main.index.IndexFragment;
import com.lfork.a98620.lfree.main.myinfo.MyInfoFragment;
import com.lfork.a98620.lfree.main.publishinfo.PublishInfoFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public final static int CODE_UPLOAD = 3;
    public final static int CODE_UPDATE = 4;
    private static final String TAG = "MainActivity";
    private List<Fragment> fragments;
    MainActBinding binding;
    private MessageService.MessageBinder messageBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messageBinder = (MessageService.MessageBinder) iBinder;
            messageBinder.buildConnection();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_act);
        binding.setViewModel(this);

        initData();
        Log.d(TAG, "onCreate: ???"  + UserDataRepository.INSTANCE.getUserId());
        initFragments();
        startService();
        bindService();
        registerNotification();
        setupStatusBarColor();

        //取消默认背景，减少UI的过度绘制
        getWindow().setBackgroundDrawable(null);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ((MyInfoFragment) fragments.get(3)).refreshData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GoodsDataRepository.INSTANCE.destroyInstance();
        UserDataRepository.INSTANCE.destroyInstance();
        unBindService();
        stopService();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:2 " + requestCode);
        Log.d(TAG, "onActivityResult: " + requestCode);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    toCommunityFragment();
                }
                break;
            case MainActivity.CODE_UPLOAD:
                if (resultCode == RESULT_OK) {
                    refreshIndexFragment(data.getIntExtra("category", 0));
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private void setupStatusBarColor() {
        Objects.requireNonNull(getSupportActionBar()).hide();
//        QMUIStatusBarHelper.translucent(this, getResources().getColor(R.color.main_color));
    }


    private void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences(FreeApplication.APP_SHARED_PREF, MODE_PRIVATE);
        UserDataRepository.INSTANCE.setUserId(Integer.parseInt(sharedPreferences.getString("recent_user_id", "0")));
        UserDataRepository.INSTANCE.getUserInfo(new DataSource.GeneralCallback<User>() {
            @Override
            public void succeed(User data) {
                Log.d(TAG, "succeed: "  + "用户信息初始化成功");
            }

            @Override
            public void failed(@NotNull String log) {
                Log.d(TAG, "succeed: "  + "用户信息初始化失败:" + log);

            }
        },  UserDataRepository.INSTANCE.getUserId());

    }

    private void registerNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat";
            String channelName = "聊天消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);

            channelId = "subscribe";
            channelName = "订阅消息";
            importance = NotificationManager.IMPORTANCE_DEFAULT;
            createNotificationChannel(channelId, channelName, importance);
        }
    }


    public void onClick(View imageView, int index) {

        //通过设置按钮背景(配合样式选择器)
        binding.goodsBtn.setSelected(false);
        binding.chatBtn.setSelected(false);
        binding.myBtn.setSelected(false);
        binding.communityBtn.setSelected(false);

        //设置文本颜色
        binding.goodsText.setTextColor(getResources().getColor(R.color.Home_act_text));
        binding.chatText.setTextColor(getResources().getColor(R.color.Home_act_text));
        binding.myText.setTextColor(getResources().getColor(R.color.Home_act_text));
        binding.communityText.setTextColor(getResources().getColor(R.color.Home_act_text));

        switch (index) {
            case 0:
                break;
            case 1:
                binding.goodsText.setTextColor(getResources().getColor(R.color.Login_ForeColor));
                binding.goodsBtn.setSelected(true);
                break;
            case 2:
                binding.chatText.setTextColor(getResources().getColor(R.color.Login_ForeColor));
                binding.chatBtn.setSelected(true);
                break;
            case 3:
                binding.myText.setTextColor(getResources().getColor(R.color.Login_ForeColor));
                binding.myBtn.setSelected(true);
                break;
            case 4:
                binding.communityText.setTextColor(getResources().getColor(R.color.Login_ForeColor));
                binding.communityBtn.setSelected(true);
                break;
            default:
                break;
        }

        replaceFragment(fragments.get(index));

    }


    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
    }

    private void startService() {
        Intent startIntent = new Intent(this, MessageService.class);
        startService(startIntent);
    }

    private void bindService() {
        Intent bindIntent = new Intent(this, MessageService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
    }

    private void unBindService() {
        messageBinder.closeConnection();
        unbindService(connection);
        Log.d(TAG, "unBindService: Message服务已断开");
    }

    private void stopService() {
        Intent stopIntent = new Intent(this, MessageService.class);
        stopService(stopIntent);


    }

    private void initFragments() {
        fragments = new ArrayList<>();
        fragments.add(new PublishInfoFragment());
        fragments.add(new IndexFragment());
        fragments.add(new ChatListFragment());
        fragments.add(new MyInfoFragment());
        fragments.add(new CommunityFragment());
        replaceFragment(fragments.get(1));
        binding.goodsBtn.setSelected(true);
        binding.goodsText.setTextColor(getResources().getColor(R.color.Login_ForeColor));
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frag, fragment);
        transaction.commit();
    }

    public void toCommunityFragment() {
        CommunityLocalDataSource.getArticleList().clear();
        onClick(binding.communityBtn, 4);
    }

    public void refreshIndexFragment(int category) {
        IndexFragment indexFragment = (IndexFragment) fragments.get(1);
        replaceFragment(indexFragment);
        indexFragment.setTargetPosition(category);
    }

}
