package com.lfork.a98620.lfree.main.community;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.databinding.MainCommunityFragBinding;
import com.lfork.a98620.lfree.main.MainActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.yalantis.ucrop.UCropFragment.TAG;


public class CommunityFragment extends Fragment implements CommunityCallback {

    private View rootView;
    private MainCommunityFragBinding binding;
    private CommunityFragmentViewModel viewModel;
    private List<CommunityFragmentItemViewModel> viewModelList = new ArrayList<>();
    private CommunityArticleAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //加载数据成功
                    setRecyclerView();
                    binding.prompt.setVisibility(View.GONE);
                    binding.communityRecyclerView.setVisibility(View.VISIBLE);
                    Log.d(TAG, "handleMessage: 加载数据成功");
                    break;
                case 2:
                    //加载数据失败
                    binding.communityRecyclerView.setVisibility(View.GONE);
                    binding.prompt.setText("加载失败");
                    binding.prompt.setVisibility(View.VISIBLE);
                    Log.d(TAG, "handleMessage: 加载数据失败");
                    break;
                case 3:
                    //刷新数据成功
                    binding.prompt.setVisibility(View.GONE);
                    binding.communityRecyclerView.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    binding.swipeRefresh.setRefreshing(false);
                    Log.d(TAG, "handleMessage: 刷新数据成功");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_community_frag, container, false);
        binding.setViewModel(viewModel);
        if (rootView == null) {
            rootView = binding.getRoot();
        }
        viewModel = new CommunityFragmentViewModel((MainActivity) getActivity());
        viewModel.loadData(CommunityFragment.this, false);
        Log.d(TAG, "onCreateView: 开始加载数据");

        binding.swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.black));
        binding.swipeRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.loadData(CommunityFragment.this, true);
                Log.d(TAG, "onClick: 开始刷新数据");
            }
        });


        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    public void setRecyclerView() {
        Log.d(TAG, "setRecyclerView: 设置RecyclerView");
        adapter = new CommunityArticleAdapter(viewModelList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        binding.communityRecyclerView.setLayoutManager(linearLayoutManager);
        binding.communityRecyclerView.setAdapter(adapter);
    }

    @Override
    public void callback(List list, int type) {
        viewModelList = list;
        sendMessage(type);
    }

    private void sendMessage(int type) {
        Message message = new Message();
        message.what = type;
        handler.sendMessage(message);
    }
}
