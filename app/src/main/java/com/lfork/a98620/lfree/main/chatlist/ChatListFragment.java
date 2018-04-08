package com.lfork.a98620.lfree.main.chatlist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.main.RecyclerViewItemAdapter;
import com.lfork.a98620.lfree.main.index.GoodsItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatListFragment extends Fragment {
    private List<Goods> contactsList;
    private List<GoodsItemViewModel> models = new ArrayList<>();
    private static final String TAG = "ChatListFragment";
    RecyclerView recyclerView ;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_chat_list_frag, container, false);
        recyclerView = view.findViewById(R.id.main_chat_recycler);
        initGoodsList();
        return view;
    }


    private void initGoodsList() {
        contactsList = new ArrayList<>();
        for (int i =0; i < 10; i++){
            Goods goods = new Goods();
            goods.setName("goods" + i);
            goods.setId(i);
            goods.setPrice(String.valueOf(i * 100));
            goods.setCoverImagePath("???");
            contactsList.add(goods);
            Log.d(TAG, "initGoodsList: test1" + i );
        }

        for (Goods g : contactsList) {
            models.add(new GoodsItemViewModel(g));
        }
        Log.d(TAG, "initGoodsList: test1" );




        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //        recyclerView .setLayoutManager(new LinearLayoutManager(context,
//                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new RecyclerViewItemAdapter<>(models, R.layout.contacts_item));
    }



}
