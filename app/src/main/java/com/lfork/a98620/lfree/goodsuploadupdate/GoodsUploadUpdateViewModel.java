package com.lfork.a98620.lfree.goodsuploadupdate;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.lfork.a98620.lfree.base.viewmodel.GoodsViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.base.entity.Category;
import com.lfork.a98620.lfree.data.base.entity.Goods;
import com.lfork.a98620.lfree.data.base.entity.GoodsDetailInfo;
import com.lfork.a98620.lfree.data.goods.GoodsDataRepository;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.main.MainActivity;
import com.lfork.a98620.lfree.base.Config;
import com.lfork.a98620.lfree.util.ToastUtil;
import com.lfork.a98620.lfree.util.file.UriHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * Created by 98620 on 2018/4/14.
 */

public class GoodsUploadUpdateViewModel extends GoodsViewModel {
    private static final String TAG = "GoodsUploadUpdateVM";

    public final ObservableField<String> tips = new ObservableField<>("已选择(0/5)");

    public final ObservableArrayList<Category> categories = new ObservableArrayList<>();

    public final ObservableArrayList<Boolean> imageVisible = new ObservableArrayList<>();

    private GoodsUploadUpdateNavigator navigator;

    private List<String> imagePathList;

    private GoodsDataRepository repository;

    private GoodsDetailInfo g;

    private int launchType;


    private int imageCount;

    private final int MAX_COUNT = 5;

    GoodsUploadUpdateViewModel(Context context) {
        super(context);
        setImageVisibility();
        imagePathList = new ArrayList<>();
        repository = GoodsDataRepository.INSTANCE;
        dataIsLoading.set(false);
        setCategoryId(1);
    }

    private void setImageVisibility() {

        Log.d(TAG, "setImageVisibility1: " + "image count" + imageCount);
        if (imageCount == 0) {
            imageVisible.clear();
            imageVisible.add(false);//默认第一个可见
            imageVisible.add(false);
            imageVisible.add(false);
            imageVisible.add(false);
            imageVisible.add(false);
            return;
        }


        int i = 0;
        for (; i < imageCount; i++) {
            if (i == MAX_COUNT) {
                return;
            }
            imageVisible.set(i, true);
        }

        Log.d(TAG, "setImageVisibility: " + i + "image count" + imageCount);

        for (; i < MAX_COUNT; i++) {
            imageVisible.set(i, false);
        }

    }

    /**
     * 静态的图片配合动态的数据  每次添加图片后
     *
     * @param index e
     */
    public void deleteImage(int index) {
        if (navigator != null) {
            navigator.deleteImage(index);
        }
    }

    /**
     * 这里就是根据选择的图片数量来设置imageView
     */
    public void selectImages() {
        if (imageCount == MAX_COUNT) {
            return;
        }
        if (navigator != null) {
            navigator.showMyDialog(MAX_COUNT - imageCount);
        }
    }

    public void setImages(List<Uri> mSelected) {
        imageCount = mSelected.size();
        imagePathList.clear();
        for (int i = 0; i < mSelected.size(); i++) {
            imagePathList.add(UriHelper.getPath(getContext(), mSelected.get(i)));
        }
        tips.set("已选择(" + imageCount + "/5)");
        setImageVisibility();
    }

    public void setImage(String path) {
        imagePathList.add(path);
        imageCount++;
        tips.set("已选择(" + imageCount + "/5)");
        setImageVisibility();
    }

    public void uploadGoods() {


        if (imagePathList.size() < 1) {
            ToastUtil.showShort(getContext(), "请添加图片");
            return;
        }
        if (TextUtils.isEmpty(name.get())) {
            ToastUtil.showShort(getContext(), "名称不能为空");
            return;
        }

        if (TextUtils.isEmpty(description.get())) {
            ToastUtil.showShort(getContext(), "描述不能为空");
            return;
        }

        if (TextUtils.isEmpty(description.get())) {
            ToastUtil.showShort(getContext(), "描述不能为空");
            return;
        }

        if (TextUtils.isEmpty(price.get())) {
            ToastUtil.showShort(getContext(), "价格不能为空");
            return;
        }

        dataIsLoading.set(true);
        Goods g = new Goods();
        g.setName(name.get());
        g.setDescription(description.get());
        g.setCoverImagePath(imagePathList.get(0));
        g.setUserId(UserDataRepository.INSTANCE.getUserId());
        g.setOriginPrice(originPrice.get());
        g.setPrice(price.get());
        g.setCategoryId(getCategoryId());

        List<File> newImages = new ArrayList<>();

        switch (launchType) {
            case MainActivity.CODE_UPLOAD:
                //图片压缩 +商品上传
                Flowable.just(imagePathList)
                        .observeOn(Schedulers.io())
                        .map(list -> {
                            // 同步方法直接返回压缩后的文件
                            newImages.addAll(
                                    Luban.with(getContext())
                                            .load(list)
                                            .setTargetDir(Objects.requireNonNull(getContext().getExternalCacheDir()).toString())
                                            .get());
                            for (File str : newImages) {
                                Log.d(TAG, "onSuccess: " + str.getPath());

                            }

                            if (imagePathList.size() >= 1) {
                                String[] imagesArray = new String[newImages.size()];

                                for (int i = 0; i < newImages.size(); i++) {
                                    imagesArray[i] = newImages.get(i).getPath();
                                }
                                g.setImagesPath(imagesArray);
                            }

                            repository.uploadGoods(new DataSource.GeneralCallback<String>() {
                                @Override
                                public void succeed(String data) {
                                    dataIsLoading.set(false);

                                    if (navigator != null) {
                                        navigator.uploadSucceed("上传成功");
                                    }
                                }

                                @Override
                                public void failed(String log) {
                                    dataIsLoading.set(false);
                                    if (navigator != null) {
                                        navigator.uploadFailed(log);
                                    }

                                }
                            }, g);


                            return newImages;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
                break;
            case MainActivity.CODE_UPDATE:
                //这里还需要处理本地图片和网络图片的压缩问题
                //已经上传了的图片就不用管了，但是本地图片还需要压缩一下
                //所以这里的操作应该比上传操作还要麻烦点
                new Thread(() -> {
                    if (navigator != null) {
                        navigator.uploadSucceed("假装修改成功:后台接口还在开发中");
                    }
                }).start();
                break;
        }


    }


    private void getCategories() {
        new Thread(() -> repository.getCategories(new DataSource.GeneralCallback<List<Category>>() {
            @Override
            public void succeed(List<Category> data) {
                categories.clear();
                categories.addAll(data);
                if (navigator != null) {
                    navigator.setDefaultCategory(getCategoryId());
                }

            }

            @Override
            public void failed(String log) {
                Log.d(TAG, "failed: " + log);
            }
        })).start();

    }

    private void getGoods(int goodsId) {
        repository = GoodsDataRepository.INSTANCE;

        new Thread(() -> repository.getGoods(new DataSource.GeneralCallback<GoodsDetailInfo>() {
            @Override
            public void succeed(GoodsDetailInfo data) {
                g = data;
                setData();
            }

            @Override
            public void failed(String log) {
                showMessage("加载失败:" + log);
            }
        }, goodsId)).start();
    }

    private void setData() {
        name.set(g.getName());
        description.set(g.getDescription());
        originPrice.set(g.getOriginPrice());
        price.set(g.getPrice());
        List<Uri> uris = new ArrayList<>();
        for (String url : g.getImages()) {
            uris.add(Uri.parse(Config.ServerImagePathRoot + url));
        }
        Objects.requireNonNull(navigator).setImages(uris);  //如果为空的话 会抛出 异常，但是不会崩溃....
    }

    public void start(int launchType, int goodsId, int categoryId) {

        //只初始化一次
        if (this.launchType == 0) {
            setCategoryId(categoryId);
            getCategories();
            this.launchType = launchType;
            if (launchType == MainActivity.CODE_UPDATE) {
                getGoods(goodsId);
            }
        }


    }

    private void showMessage(String msg) {
        if (navigator != null) {
            navigator.showToast(msg);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navigator = null;
    }


    public void setNavigator(GoodsUploadUpdateNavigator navigator) {
        this.navigator = navigator;
    }

}
