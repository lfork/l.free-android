package com.lfork.a98620.lfree.data.goods.remote;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.lfork.a98620.lfree.base.network.HttpService;
import com.lfork.a98620.lfree.base.network.Result;
import com.lfork.a98620.lfree.data.entity.Category;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.data.entity.GoodsDetailInfo;
import com.lfork.a98620.lfree.data.entity.Review;
import com.lfork.a98620.lfree.data.goods.GoodsDataSource;
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

    private static final String TAG = "IMRemoteDataSource";

    private static GoodsRemoteDataSource INSTANCE;

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

        Log.d(TAG, "getGoodsList: " + cursor);
        RequestBody requestbody = new FormBody.Builder()
                .add("csId", categoryId + "")
                .add("cursor", cursor + "")
                .build();

        String responseData = HttpService.getInstance().sendPostRequest(url, requestbody);

        Result<ArrayList<Goods>> result = JSONUtil.parseJson(responseData, new TypeToken<Result<ArrayList<Goods>>>() {
        });

        if (result != null) {
            ArrayList<Goods> list = result.getData();
            if (list != null && list.size() > 0)
                callback.succeed(list);
            else {
                callback.failed(result.getMessage());
            }
        } else {
            callback.failed("error：服务器异常、或者是没有网络连接");
        }
    }

    @Override
    public void getUserGoodsList(GeneralCallback<List<Goods>> callback, String cursor, String userId) {
        String url = Config.ServerURL + "/22y/user_getUserGoodsByUid";
       // http://www.lfork.top/22y/user_getUserGoodsByUid?studentId=2015215064&cursor=2018-04-08%2008:03:07
        RequestBody requestbody = new FormBody.Builder()
                .add("studentId", userId)
                .add("cursor", cursor)
                .build();

        String responseData = HttpService.getInstance().sendPostRequest(url, requestbody);

        Result<ArrayList<Goods>> result = JSONUtil.parseJson(responseData, new TypeToken<Result<ArrayList<Goods>>>() {
        });

        if (result != null) {
            ArrayList<Goods> list = result.getData();
            if (list != null && list.size() > 0)
                callback.succeed(list);
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
        String responseData = HttpService.getInstance().sendPostRequest(url, requestbody);
        Result<List<Category>> result = JSONUtil.parseJson(responseData, new TypeToken<Result<List<Category>>>() {
        });

        if (result != null) {
            List<Category> list = result.getData();
            if (list != null)
                callback.succeed(list);
            else {
                callback.failed(result.getMessage());
            }
        } else {
            callback.failed("error:1 服务器异常");
        }
    }

    @Override
    public void getGoods(GeneralCallback<GoodsDetailInfo> callback, int goodsId) {
        String url = Config.ServerURL + "/22y/goods_getGoodsById";

        RequestBody requestbody = new FormBody.Builder()
                .add("goodsId", goodsId + "")
                .build();
        String responseData = HttpService.getInstance().sendPostRequest(url, requestbody);
        Result<GoodsDetailInfo> result = JSONUtil.parseJson(responseData, new TypeToken<Result<GoodsDetailInfo>>() {
        });

        if (result != null) {
            if (result.getCode() == 1)
                callback.succeed(result.getData());
            else {
                callback.failed(result.getMessage());
            }
        } else {
            callback.failed("error: 服务器异常");
        }

    }

    @Override
    public void uploadGoods(GeneralCallback<String> callback, Goods g) {
        String[] images = g.getImagesPath();
        Log.d(TAG, "uploadGoods: " + g.getPublishDate());

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


        String responseData = HttpService.getInstance().sendPostRequest("http://www.lfork.top/22y/goods_upload", requestBody);
        Result<String> result = JSONUtil.parseJson(responseData, new TypeToken<Result<String>>() {
        });

        if (result != null) {
            if (result.getCode() == 1) {
                callback.succeed(result.getMessage());
            } else {
                callback.failed(result.getMessage());
            }
        } else {
            callback.failed("error 服务器异常");
        }
    }

    @Override
    public void goodsSearch(GeneralCallback<List<Goods>> callback, String keyword) {
        String url = Config.ServerURL + "/22y/goodsSerach_getGoodsByName";
        RequestBody requestbody = new FormBody.Builder()
                .add("goodsLikeName", keyword)
                .build();
        String responseData = HttpService.getInstance().sendPostRequest(url, requestbody);
        Result<List<Goods>> result = JSONUtil.parseJson(responseData, new TypeToken<Result<List<Goods>>>() {
        });

        if (result != null) {
            if (result.getCode() == 1)
                callback.succeed(result.getData());
            else {
                callback.failed(result.getMessage());
            }
        } else {
            callback.failed("error: 服务器异常");
        }

        //http://www.lfork.top/22y/goodsSerach_getGoodsByName?=Java
    }

    @Override
    public void deleteGoods(GeneralCallback<String> callback, int goodsId) {
        callback.succeed("删除成功");
    }

    @Override
    public void updateGoods(GeneralCallback<String> callback, Goods g) {
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


        String responseData = HttpService.getInstance().sendPostRequest("http://www.lfork.top/22y/goods_upload", requestBody);
        Result<String> result = JSONUtil.parseJson(responseData, new TypeToken<Result<String>>() {
        });

        if (result != null) {
            if (result.getCode() == 1) {
                callback.succeed(result.getMessage());
            } else {
                callback.failed(result.getMessage());
            }
        } else {
            callback.failed("error 服务器异常");
        }
    }


    @Override
    public void addReview(GeneralCallback<Review> callback, Review review) {
        String url = Config.ServerURL + "/22y/review_reviewSave";
        RequestBody requestbody = new FormBody.Builder()
                .add("userId", review.getUserId())
                .add("goodsId", review.getGoodsId())
                .add("reviewContext", review.getContent())
                .build();
        String responseData = HttpService.getInstance().sendPostRequest(url, requestbody);
        Result result = JSONUtil.parseJson(responseData, new TypeToken<Result>() {
        });

        if (result != null) {
            if (result.getCode() == 1)
                callback.succeed(review);
            else {
                callback.failed(result.getMessage());
            }
        } else {
            callback.failed("error: 服务器异常");
        }
    }
}
