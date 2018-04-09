package com.lfork.a98620.lfree.main.goodsupload;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.goodsupload.GoodsUploadActivity;

public class GoodsUploadFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_goods_upload_frag, container, false);

        Button upload = view.findViewById(R.id.btn_upload);
        upload.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), GoodsUploadActivity.class);
            getActivity().startActivity(intent);
        });

        // Inflate the layout for this fragment
        return view;
    }

}
