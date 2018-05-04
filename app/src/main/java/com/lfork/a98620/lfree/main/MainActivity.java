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
import android.view.View;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.goods.GoodsDataRepository;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.imservice.MessageService;
import com.lfork.a98620.lfree.databinding.MainActBinding;
import com.lfork.a98620.lfree.main.chatlist.ChatListFragment;
import com.lfork.a98620.lfree.main.community.CommunityFragment;
import com.lfork.a98620.lfree.main.goodsupload.GoodsUploadFragment;
import com.lfork.a98620.lfree.main.index.IndexFragment;
import com.lfork.a98620.lfree.main.myinfo.MyInforFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private List<Fragment> fragments;
    MainActBinding binding;
    private MessageService.MessageBinder messageBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: MessageSevice启动成功");
            messageBinder = (MessageService.MessageBinder) iBinder;
            messageBinder.buildConnection();
            Log.d(TAG, "buildUDPConnection: 不执行这里的吗？？6");

//            messageBinder.startDownload();
//            messageBinder.getProgress();
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

        int userId = UserDataRepository.getInstance().getUserId();

        if (userId != 0) {  //存
            SharedPreferences.Editor editor = getSharedPreferences("data_main", MODE_PRIVATE).edit();
            editor.putInt("user_id", UserDataRepository.getInstance().getUserId());
            editor.apply();
        } else {        //取
            SharedPreferences sharedPreferences = getSharedPreferences("data_main", MODE_PRIVATE);
            userId = sharedPreferences.getInt("user_id", 0);
            if (userId != 0) {//存
                UserDataRepository.getInstance().setUserId(userId);
            }
        }

        initFragments();
        UserDataRepository.getInstance(); //初始化user数据
        startService();
        bindService();

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

    @Override
    protected void onRestart() {
        super.onRestart();
        ((MyInforFragment) fragments.get(3)).refreshData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 退出后没有释放资源？？");
        GoodsDataRepository.destroyInstance();
        UserDataRepository.destroyInstance();
        unBindService();
        stopService();
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

        }

        Log.d(TAG, "onButton1Clicked: why there is no any response " + index);
        replaceFragment(fragments.get(index));

    }


    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    private void startService() {
        Intent startIntent = new Intent(this, MessageService.class);
        startService(startIntent); //launch service
    }

    private void bindService() {
        Intent bindIntent = new Intent(this, MessageService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);// bind service
        Log.d(TAG, "buildUDPConnection: 不执行这里的吗？？7");
    }

    private void unBindService() {

        messageBinder.closeConnection();
        unbindService(connection);
        Log.d(TAG, "unBindService: Message服务已断开" );
    }

    private void stopService() {
        Intent stopIntent = new Intent(this, MessageService.class);
        stopService(stopIntent);


    }

    private void initFragments() {
        fragments = new ArrayList<>();
        fragments.add(new GoodsUploadFragment());
        fragments.add(new IndexFragment());
        fragments.add(new ChatListFragment());
        fragments.add(new MyInforFragment());
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
}
