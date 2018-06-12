package com.lfork.a98620.lfree.main.community;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.databinding.MainCommunityFragBinding;
import com.lfork.a98620.lfree.main.MainActivity;
import java.util.List;

import static com.yalantis.ucrop.UCropFragment.TAG;


public class CommunityFragment extends Fragment implements CommunityCallback {

    private View rootView;
    private MainCommunityFragBinding binding;
    private CommunityFragmentViewModel viewModel;
    private List<CommunityArticle> viewModelList;
    private CommunityArticleAdapter adapter;
    private MyHandler myHandler;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_community_frag, container, false);
        binding.setViewModel(viewModel);
        binding.swipeRefresh.setSize(SwipeRefreshLayout.LARGE);
        binding.swipeRefresh.setProgressBackgroundColorSchemeColor(Color.BLUE);
        binding.swipeRefresh.setColorSchemeColors(Color.BLUE);
        myHandler = new MyHandler(binding, this);
        if (rootView == null) {
            rootView = binding.getRoot();
        }
        viewModel = new CommunityFragmentViewModel((MainActivity) getActivity());
        viewModel.loadData(CommunityFragment.this);
        Log.d(TAG, "onCreateView: 开始加载数据");

        binding.swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.black));
        binding.swipeRefresh.post(() -> {//第一次加载数据
            binding.swipeRefresh.setRefreshing(true);
            viewModel.loadData(CommunityFragment.this);
        });
        binding.swipeRefresh.setEnabled(true);
        binding.swipeRefresh.setOnRefreshListener(() -> {
            viewModel.loadData(CommunityFragment.this);
            Log.d(TAG, "onClick: 开始刷新数据");
        });

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    public void setRecyclerView() {
        if (adapter == null) {
            Log.d(TAG, "setRecyclerView: 设置RecyclerView");
            adapter = new CommunityArticleAdapter(viewModelList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
            binding.communityRecyclerView.setLayoutManager(linearLayoutManager);
            binding.communityRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void callback(Object data, int type) {
        if (data != null) {
            viewModelList = (List<CommunityArticle>) data;
            sendMessage(type);
        } else {
            sendMessage(2);//加载数据成功但数据为空
        }
    }

    private void sendMessage(int type) {
        Message message = new Message();
        message.what = type;
        myHandler.sendMessage(message);
    }

    static class MyHandler extends Handler {
        private MainCommunityFragBinding binding;
        private CommunityFragment fragment;
        MyHandler(MainCommunityFragBinding binding, CommunityFragment fragment) {
            this.binding = binding;
            this.fragment = fragment;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //加载数据成功
                    Log.d(TAG, "handleMessage: isRefreshing = " + binding.swipeRefresh.isRefreshing());
                    binding.swipeRefresh.setRefreshing(false);
                    Log.d(TAG, "handleMessage: setRefreshing = false");
                    fragment.setRecyclerView();
                    binding.prompt.setVisibility(View.GONE);
                    binding.communityRecyclerView.setVisibility(View.VISIBLE);
                    Log.d(TAG, "handleMessage: 加载动态数据成功");
                    break;
                case 2:
                    //加载数据失败
                    Log.d(TAG, "handleMessage: isRefreshing = " + binding.swipeRefresh.isRefreshing());
                    binding.swipeRefresh.setRefreshing(false);
                    Log.d(TAG, "handleMessage: setRefreshing = false");
                    binding.prompt.setText("加载失败");
                    binding.prompt.setVisibility(View.VISIBLE);
                    Log.d(TAG, "handleMessage: 加载动态数据失败");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.swipeRefresh.setEnabled(true);
        binding.swipeRefresh.setRefreshing(true);
    }
}
