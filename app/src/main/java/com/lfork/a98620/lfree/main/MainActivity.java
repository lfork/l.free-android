package com.lfork.a98620.lfree.main;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.source.UserDataRepository;
import com.lfork.a98620.lfree.databinding.MainActBinding;
import com.lfork.a98620.lfree.main.chatlist.ChatListFragment;
import com.lfork.a98620.lfree.main.goodsupload.GoodsUploadFragment;
import com.lfork.a98620.lfree.main.index.IndexFragment;
import com.lfork.a98620.lfree.main.myinfo.MyInforFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private List<Fragment> fragments;
    MainActBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_act);
        binding.setViewModel(this);
        UserDataRepository mRepository = UserDataRepository.getInstance();
        initFragments();
    }

    public void onClick(View imageView, int index){

        //通过设置按钮背景(配合样式选择器)
        binding.goodsBtn.setSelected(false);
        binding.chatBtn.setSelected(false);
        binding.myBtn.setSelected(false);

        //设置文本颜色
        binding.goodsText.setTextColor(getResources().getColor(R.color.Home_act_text));
        binding.chatText.setTextColor(getResources().getColor(R.color.Home_act_text));
        binding.myText.setTextColor(getResources().getColor(R.color.Home_act_text));

        switch (index){
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
        }

        Log.d(TAG, "onClick: why there is no any response " + index );
        replaceFragment(fragments.get(index));

    }

    private void initFragments() {
        fragments = new ArrayList<>();
        fragments.add(new GoodsUploadFragment());
        fragments.add(new IndexFragment());
        fragments.add(new ChatListFragment());
        fragments.add(new MyInforFragment());
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
