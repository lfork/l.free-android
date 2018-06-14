package com.lfork.a98620.lfree.main.community;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.community.local.CommunityLocalDataSource;
import com.lfork.a98620.lfree.databinding.MainCommunityFragBinding;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.yalantis.ucrop.UCropFragment.TAG;

public class CommunityFragment extends Fragment implements CommunityCallback {

    private View rootView = null;
    private MainCommunityFragBinding binding = null;
    private CommunityFragmentViewModel viewModel = null;
    private List<CommunityArticle> articleList = null;
    private CommunityArticleAdapter adapter = null;
    private MyHandler myHandler = null;
    private boolean recyclerviewIsLoad = false;
    private boolean isCanRefresh = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articleList = CommunityLocalDataSource.getArticleList();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.main_community_frag, container, false);
        }
        if (viewModel == null) {
            viewModel = new CommunityFragmentViewModel(getActivity());
        }
        if (myHandler == null) {
            myHandler = new MyHandler(binding, this);
        }

        binding.setViewModel(viewModel);
        binding.swipeRefresh.setSize(SwipeRefreshLayout.LARGE);
        binding.swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.community_refresh));
        binding.swipeRefresh.setColorSchemeColors(Color.BLUE);
        if (rootView == null) {
            rootView = binding.getRoot();
        }

        binding.swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.black));

        if (articleList.size() == 0) {
            binding.swipeRefresh.post(() -> {//第一次加载数据
                Log.d(TAG, "onCreateView: 开始加载数据");
                CommunityLocalDataSource.getArticleList().clear();
                binding.swipeRefresh.setRefreshing(true);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                String fromTime = DateFormat.format("yyyy-MM-dd-HH-mm-ss", calendar.getTime()).toString();
                Log.d(TAG, "onCreateView: 前一天时间" + fromTime);
                viewModel.loadData(">" + fromTime, CommunityFragment.this);
            });
        }

        binding.swipeRefresh.setOnRefreshListener(() -> {
            binding.swipeRefresh.setRefreshing(true);
            Log.d(TAG, "onClick: 开始刷新数据");
            String fromTime = articleList.get(0).getArticleTime();
            CommunityLocalDataSource.getArticleList().clear();
            isCanRefresh = true;
            viewModel.loadData(">" + fromTime, CommunityFragment.this);
            Log.d(TAG, "onCreateView: "+binding.swipeRefresh);
        });

        return rootView;
    }

    public void setRecyclerView() {
        Log.d(TAG, "setRecyclerView: 设置RecyclerView");
        adapter = new CommunityArticleAdapter(articleList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        binding.communityRecyclerView.setLayoutManager(linearLayoutManager);
        binding.communityRecyclerView.setAdapter(adapter);
        binding.communityRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!binding.communityRecyclerView.canScrollVertically(1)) {//滚动到底部,上拉加载
                    String fromTime = articleList.get(articleList.size() - 1).getArticleTime();
                    if ((articleList.size() == 0)) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.DAY_OF_MONTH, -1);
                        fromTime = DateFormat.format("yyyy-MM-dd-HH-mm-ss", calendar.getTime()).toString();
                    }
                    Log.d(TAG, "onScrollStateChanged: 最后一个动态的时间" + fromTime);
                    if (isCanRefresh) {
                        viewModel.loadData("<" + fromTime, CommunityFragment.this);
                    } else {
                        Toast.makeText(binding.getRoot().getContext(), "已经到底了哦", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        recyclerviewIsLoad = true;
    }

    @Override
    public void callback(Object data, int type) {
        if (data != null) {
            int dataSize =(int) data;
            if (dataSize != 0) {
                articleList = CommunityLocalDataSource.getArticleList();
                sendMessage(type);
            } else {
                isCanRefresh = false;
            }
        } else {
            sendMessage(2);
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
                    if (fragment.recyclerviewIsLoad) {
                        fragment.adapter.notifyDataSetChanged();
                    } else {
                        fragment.setRecyclerView();
                    }
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
}