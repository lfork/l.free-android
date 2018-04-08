package com.lfork.a98620.lfree.data.source.remote;

import com.google.gson.reflect.TypeToken;
import com.lfork.a98620.lfree.data.entity.Category;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.data.source.GoodsDataSource;
import com.lfork.a98620.lfree.data.source.remote.httpservice.Result;
import com.lfork.a98620.lfree.data.source.remote.httpservice.Service;
import com.lfork.a98620.lfree.util.Config;
import com.lfork.a98620.lfree.util.JSONUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by 98620 on 2018/3/23.
 */

public class GoodsRemoteDataSource implements GoodsDataSource {

    private static GoodsRemoteDataSource INSTANCE;
//    private ArrayList<ResponseGetUser.UserInfo> mCachedUserList = new ArrayList<>();

    private static final String TAG = "UserRemoteDataSource";

    private GoodsRemoteDataSource() {
    }

    public static GoodsRemoteDataSource getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            INSTANCE = new GoodsRemoteDataSource();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getGoodsList(GeneralCallback<List<Goods>> callback, int pageIndex, int categoryId) {
        String url = Config.ServerURL + "22y/cs_getPageCsList";

        RequestBody requestbody = new FormBody.Builder()
                .add("csId", categoryId + "")
                .add("pageNo", pageIndex + "")
                .build();

        ArrayList<Goods> list1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Goods category = new Goods();
            category.setName("Java从入门到精通");
            category.setCoverImagePath("https://img10.360buyimg.com/n1/jfs/t3775/118/1324998209/351033/2016778a/5823e5f9N2eee28eb.jpg");
            category.setPublishDate("2018.4.7");
            category.setPrice(String.valueOf(i * 100) + "元");
            category.setUserId(i);
            category.setId(i);
            list1.add(category);
        }
        callback.success(list1);

        String responseData = new Service().sendPostRequest(url, requestbody);

        Result<ArrayList<Goods>> result = JSONUtil.parseJson(responseData, new TypeToken<Result<ArrayList<Goods>>>() {
        });

//        if (result != null) {
//            ArrayList<Goods> list = result.getData();
//            if (list != null)
//                callback.success(list);
//            else {
//                callback.failed(result.getMessage());
//            }
//        } else {
//            callback.failed("error");
//        }
    }

    @Override
    public void getCategories(GeneralCallback<List<Category>> callback) {
        String url = Config.ServerURL + "22y/cs_getCsList";

        RequestBody requestbody = new FormBody.Builder()
                .build();
        String responseData = new Service().sendPostRequest(url, requestbody);
        Result<List<Category>> result = JSONUtil.parseJson(responseData, new TypeToken<Result<List<Category>>>() {
        });

        if (result != null) {
            List<Category> list = result.getData();
            if (list != null)
                callback.success(list);
            else {
                callback.failed(result.getMessage());
            }
        } else {
            callback.failed("error");
        }
    }

    @Override
    public void refreshData() {

    }
}
