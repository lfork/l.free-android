package com.lfork.a98620.lfree.main.myinfo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.databinding.MainMyInforFragBinding;
import com.lfork.a98620.lfree.login.LoginActivity;
import com.lfork.a98620.lfree.main.MainActivity;
import com.lfork.a98620.lfree.mygoods.MyGoodsActivity;
import com.lfork.a98620.lfree.settings.SettingsActivity;
import com.lfork.a98620.lfree.userinfothis.UserInfoThisActivity;
import com.lfork.a98620.lfree.util.ToastUtil;

import java.util.Objects;

public class MyInfoFragment extends Fragment implements MyInfoFragmentNavigator {

    private View rootView;//页面缓存 ，这样做，当fragment被destroy的时候 可以让view不被回收，等下就可以快速恢复。

    private MyInfoFragmentViewModel viewModel;

    public void refreshData() {
//        if (viewModel != null){
//            viewModel.refreshData();
//        }
        //TODO 修改用户资料后的回执操作
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            MainMyInforFragBinding binding = DataBindingUtil.inflate(inflater, R.layout.main_my_infor_frag, container, false);
            viewModel = new MyInfoFragmentViewModel((MainActivity) getActivity());
            binding.setViewModel(viewModel);
            rootView = binding.getRoot();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();  //时时刻刻都要保证数据的最新状态
        viewModel.start();
        viewModel.setNavigator(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }

    @Override
    public void openMyGoods() {
        Intent intent = new Intent(getContext(), MyGoodsActivity.class);
        startActivity(intent);
    }

    @Override
    public void openSettings() {
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        startActivity(intent);
    }


    @Override
    public void openUserInfoDetail() {
        Intent intent = new Intent(getContext(), UserInfoThisActivity.class);
        startActivity(intent);
    }

    @Override
    public void logoff() {
        LoginActivity.start(getContext(),LoginActivity.STATUS_LOGOUT );
        //释放资源
        Objects.requireNonNull(getActivity()).finish();
    }

    @Override
    public void showToast(String msg) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            ToastUtil.showShort(getContext(), msg);
        });
    }
}
