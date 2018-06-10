package com.lfork.a98620.lfree.main.publishinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.goodsupload.GoodsUploadActivity;
import com.lfork.a98620.lfree.main.MainActivity;
import com.lfork.a98620.lfree.main.community.CommunityFragment;
import com.lfork.a98620.lfree.main.community.PublishArticleActivity;

import static android.app.Activity.RESULT_OK;

public class PublishInfoFragment extends Fragment {
    private static final String TAG = "PublishInfoFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_goods_upload_frag, container, false);
        Button upload = view.findViewById(R.id.btn_upload);
        upload.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), GoodsUploadActivity.class);
            startActivity(intent);
        });
        Button publishArticle = (Button) view.findViewById(R.id.btn_publish_article);
        publishArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PublishArticleActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "onActivityResult: " + requestCode);
                    MainActivity activity = (MainActivity) getActivity();
                    activity.toCommunityFragmentFromPublishFragment();
                }
                break;
            default:
                break;
        }
    }
}
