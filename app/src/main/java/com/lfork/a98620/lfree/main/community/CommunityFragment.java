package com.lfork.a98620.lfree.main.community;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.databinding.MainCommunityFragBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CommunityFragment extends Fragment {
    private static final String TAG = "CommunityFragment";

    private View rootView;
    private Activity activity;
    private CommunityFragmentViewModel viewModel;

    private List<CommunityFragmentItemViewModel>  communityFragmentItemViewModelList = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setRecyclerView();
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainCommunityFragBinding binding = DataBindingUtil.inflate(inflater, R.layout.main_community_frag, container, false);
        this.activity = getActivity();
        viewModel = new CommunityFragmentViewModel(activity);
        binding.setViewModel(viewModel);
        if (rootView == null) {
            rootView = binding.getRoot();
        }

        loadData();

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    private void loadData() {
        Log.d(TAG, "run: 开始请求数据");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://imyth.top:8080/community_server/getcommunityarticle").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                communityFragmentItemViewModelList.clear();
                communityFragmentItemViewModelList.add(new CommunityFragmentItemViewModel("加载失败", 0, null, null));
                Log.d(TAG, "连接失败: 请求数据失败");
                sendMessage();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                Log.d(TAG, "onResponse: 请求数据成功");
                Log.d(TAG, "onResponse: json数据" + jsonData);
                try {
                    communityFragmentItemViewModelList = new Gson().fromJson(jsonData,
                            new TypeToken<List<CommunityArticle>>(){}.getType());
                    sendMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                    communityFragmentItemViewModelList.clear();
                    communityFragmentItemViewModelList.add(new CommunityFragmentItemViewModel("加载失败", 0, null, null));
                    Log.d(TAG, "服务器异常: 请求数据失败");
                    sendMessage();
                }
            }
        });
    }

    public void setRecyclerView() {
        Log.d(TAG, "setRecyclerView: 设置RecyclerView");
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.community_recycler_view);
        CommunityArticleAdapter adapter = new CommunityArticleAdapter(activity, communityFragmentItemViewModelList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void sendMessage() {
        Message message = new Message();
        message.what = 1;
        handler.sendMessage(message);
    }
}
