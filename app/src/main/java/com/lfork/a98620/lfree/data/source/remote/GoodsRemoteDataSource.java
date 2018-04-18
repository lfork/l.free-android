package com.lfork.a98620.lfree.data.source.remote;

import com.google.gson.reflect.TypeToken;
import com.lfork.a98620.lfree.data.entity.Category;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.data.entity.GoodsDetailInfo;
import com.lfork.a98620.lfree.data.source.GoodsDataSource;
import com.lfork.a98620.lfree.data.source.remote.httpservice.Result;
import com.lfork.a98620.lfree.data.source.remote.httpservice.Service;
import com.lfork.a98620.lfree.util.Config;
import com.lfork.a98620.lfree.util.JSONUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
    public void getGoodsList(GeneralCallback<List<Goods>> callback, String cursor, int categoryId) {
        String url = Config.ServerURL + "/22y/goodsApp_getGoodsPageApp";

        RequestBody requestbody = new FormBody.Builder()
                .add("csId", categoryId + "")
                .add("cursor", cursor + "")
                .build();

        String responseData = new Service().sendPostRequest(url, requestbody);

        Result<ArrayList<Goods>> result = JSONUtil.parseJson(responseData, new TypeToken<Result<ArrayList<Goods>>>() {
        });

        if (result != null) {
            ArrayList<Goods> list = result.getData();
            if (list != null && list.size() > 0)
                callback.success(list);
            else {
                callback.failed(result.getMessage());
            }
        } else {
            callback.failed("error：服务器异常、或者是没有网络连接");
        }
    }

    @Override
    public void getCategories(GeneralCallback<List<Category>> callback) {
        String url = Config.ServerURL + "/22y/cs_getCsList";

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
            callback.failed("error:1 服务器异常");
        }
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void getGoods(GeneralCallback<GoodsDetailInfo> callback, int goodsId) {
        String url = Config.ServerURL + "/22y/goods_getGoodsById";

        RequestBody requestbody = new FormBody.Builder()
                .add("goodsId", goodsId + "")
                .build();
        String responseData = new Service().sendPostRequest(url, requestbody);
        Result<GoodsDetailInfo> result = JSONUtil.parseJson(responseData, new TypeToken<Result<GoodsDetailInfo>>() {
        });

        if (result != null) {
            if (result.getCode() == 1)
                callback.success(result.getData());
            else {
                callback.failed(result.getMessage());
            }
        } else {
            callback.failed("error:1 服务器异常");
        }

    }

    @Override
    public void uploadGoods(GeneralCallback<String> callback, Goods g) {
        String[] images = g.getImagesPath();

        RequestBody[] files = new RequestBody[images.length];

        for (int i = 0; i < images.length; i++) {
            files[i] = RequestBody.create(MediaType.parse("image/png"), new File(images[i]));
        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        builder
                .addFormDataPart("studentId", g.getUserId() + "")
                .addFormDataPart("csId", g.getCategoryId() + "")
                .addFormDataPart("gName", g.getName())
                .addFormDataPart("gBuyPrice", g.getOriginPrice())
                .addFormDataPart("gSellPrice", g.getPrice())
                .addFormDataPart("gDesc", g.getDescription())
                .addFormDataPart("coverImage", System.currentTimeMillis() + "image.png", files[0]);

        for (int i = 1; i < files.length; i++) {
            builder.addFormDataPart("images", System.currentTimeMillis() + "image.png", files[i])
                    .addFormDataPart("desc", "这个拿来干啥？？");
        }

        if (files.length == 1) {
            builder.addFormDataPart("desc", "只传了一张图片，没有描述");
        }

        RequestBody requestBody = builder.build();


        String responseData = new Service().sendPostRequest("http://www.lfork.top/22y/goods_upload", requestBody);
        Result<String> result = JSONUtil.parseJson(responseData, new TypeToken<Result<String>>() {
        });

        if (result != null) {
            if (result.getCode() == 1) {
                callback.success(result.getMessage());
            } else {
                callback.failed(result.getMessage());
            }
        } else {
            callback.failed("error 服务器异常");
        }
    }
}
