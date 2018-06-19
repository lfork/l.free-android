package com.lfork.a98620.lfree.main.publishinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.goodsupload.GoodsUploadActivity;
import com.lfork.a98620.lfree.main.MainActivity;
import com.lfork.a98620.lfree.publisharticle.PublishArticleActivity;

import static android.app.Activity.RESULT_OK;

public class PublishInfoFragment extends Fragment {
    private static final String TAG = "PublishInfoFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_goods_upload_frag, container, false);
        Button upload = view.findViewById(R.id.btn_upload);
        upload.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), GoodsUploadActivity.class);
            startActivityForResult(intent, MainActivity.CODE_UPLOAD);
        });

        Button publishArticle = view.findViewById(R.id.btn_publish_article);
        publishArticle.setOnClickListener((view1) -> {
            Intent intent = new Intent(getContext(), PublishArticleActivity.class);
            startActivityForResult(intent, 1);
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        MainActivity activity = (MainActivity) getActivity();
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {


                    if (activity != null) {
                        activity.toCommunityFragment();
                    }
                }
            case MainActivity.CODE_UPLOAD:
                if (resultCode == RESULT_OK) {
                    if (activity != null) {
                        activity.refreshIndexFragment(data.getIntExtra("category", 0));
                    }
                }
                break;
            default:
                break;
        }
    }
}
