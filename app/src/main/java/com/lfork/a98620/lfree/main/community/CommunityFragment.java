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
    private RecyclerView recyclerView;
    private CommunityArticleAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onStart() {
        super.onStart();
        viewModel = new CommunityFragmentViewModel((MainActivity) getActivity());
        startLoadData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_community_frag, container, false);
        binding.setViewModel(viewModel);
        if (rootView == null) {
            rootView = binding.getRoot();
        }

        swipeRefreshLayout = binding.swipeRefresh;
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.black));
        swipeRefreshLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoadData();
            }
        });


        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    public void setRecyclerView(List<CommunityArticle> communityArticleList) {
        Log.d(TAG, "setRecyclerView: 设置RecyclerView");
        recyclerView = (RecyclerView) rootView.findViewById(R.id.community_recycler_view);
        adapter = new CommunityArticleAdapter(communityArticleList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        UserDataRepository.getInstance().getUserInfo(new DataSource.GeneralCallback<User>() {
            @Override
            public void succeed(User data) {
            }

            @Override
            public void failed(String log) {
            }
        }, 23);
    }

    @Override
    public void callback(List list) {
        if (list != null) {
            setRecyclerView(list);
            binding.prompt.setVisibility(View.GONE);
            binding.communityRecyclerView.setVisibility(View.VISIBLE);
        } else {
            binding.communityRecyclerView.setVisibility(View.GONE);
            binding.prompt.setVisibility(View.VISIBLE);
        }
    }

    private void startLoadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                viewModel.start(CommunityFragment.this);
            }
        }).start();
    }
}
