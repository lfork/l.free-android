package com.lfork.a98620.lfree.main.community;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.databinding.MainCommunityFragBinding;
import com.lfork.a98620.lfree.main.MainActivity;

public class CommunityFragment extends Fragment {

    private View rootView;

    private CommunityFragmentViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            MainCommunityFragBinding binding = DataBindingUtil.inflate(inflater, R.layout.main_community_frag, container, false);
            viewModel = new CommunityFragmentViewModel((MainActivity) getActivity());
            binding.setViewModel(viewModel);
            rootView = binding.getRoot();

        }

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

}
