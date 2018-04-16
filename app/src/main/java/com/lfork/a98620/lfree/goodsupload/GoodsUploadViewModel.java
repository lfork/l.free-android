package com.lfork.a98620.lfree.goodsupload;

import android.databinding.ObservableArrayList;
import android.net.Uri;

import com.lfork.a98620.lfree.common.viewmodel.GoodsViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.data.source.GoodsDataRepository;
import com.lfork.a98620.lfree.data.source.UserDataRepository;
import com.lfork.a98620.lfree.util.StringUtil;
import com.lfork.a98620.lfree.util.ToastUtil;
import com.lfork.a98620.lfree.util.image.ImageTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 98620 on 2018/4/14.
 */

public class GoodsUploadViewModel extends GoodsViewModel {

    private GoodsUploadActivity activity;

    private List<String> imagePathList;

    private GoodsDataRepository repository;

    public final ObservableArrayList<Boolean> imageVisible = new ObservableArrayList<>();

    int imageCount;

    public final int MAX_COUNT = 5;

    public GoodsUploadViewModel(GoodsUploadActivity context) {
        super(context);
        activity = context;
        setImageVisibility();
        imagePathList = new ArrayList<>();
        repository = GoodsDataRepository.getInstance();
        dataIsLoading.set(false);

    }

    private void setImageVisibility() {

        if (imageCount == 0) {
            imageVisible.add(true);//默认第一个可见
            imageVisible.add(false);
            imageVisible.add(false);
            imageVisible.add(false);
            imageVisible.add(false);
            return;
        }


        int i = 0;
        for (; i < imageCount; i++) {
            if (i + 1 == MAX_COUNT) {
                return;
            }
            imageVisible.set(i + 1, true);
        }

        for (; i < MAX_COUNT - 1; i++)
            imageVisible.set(i + 1, false);

    }

    public void selectImages(int index) {

        if (imageCount == MAX_COUNT) {
            return;
        }
        if (imageCount == index) {
            activity.showMyDialog(MAX_COUNT - index, index);
        }
    }

    public void deleteImage() {
        imageCount--;
    }

    public void setImages(List<Uri> mSelected) {
        imageCount = mSelected.size();

        for (int i = 0; i < mSelected.size(); i++) {
            imagePathList.add(ImageTool.getPath(activity, mSelected.get(i)));
        }
        setImageVisibility();
    }

    public void setImage(String path) {
        imagePathList.add(path);
        imageCount++;
        setImageVisibility();

    }

    public void uploadGoods() {
        if (imagePathList.size() < 1){
            ToastUtil.showShort(context, "请上传图片");
            return;
        }
        if (StringUtil.isNull(name.get()) ){
            ToastUtil.showShort(context, "名称不能为空");
            return;
        }

        if (StringUtil.isNull(description.get()) ){
            ToastUtil.showShort(context, "描述不能为空");
            return;
        }

        if (StringUtil.isNull(description.get()) ){
            ToastUtil.showShort(context, "描述不能为空");
            return;
        }

        if (StringUtil.isNull(price.get()) ){
            ToastUtil.showShort(context, "价格不能为空");
            return;
        }



        dataIsLoading.set(true);
        Goods g = new Goods();
        g.setName(name.get());
        g.setDescription(description.get());
        g.setCoverImagePath(imagePathList.get(0));
        g.setUserId(UserDataRepository.getInstance().getThisUser().getUserId());
        g.setOriginPrice(originPrice.get());
        g.setPrice(price.get());
        g.setCategoryId(1);



        if (imagePathList.size() >= 1) {
            String[] images = new String[imagePathList.size()];

            for (int i = 0; i < imagePathList.size(); i++) {
                images[i] = imagePathList.get(i);
            }
            g.setImagesPath(images);
        }


        new Thread(() -> {
            repository.uploadGoods(new DataSource.GeneralCallback<String>() {
                @Override
                public void success(String data) {
                    dataIsLoading.set(false);
                    activity.runOnUiThread(() -> {
                                ToastUtil.showShort(activity, data);
                            }
                    );

                }

                @Override
                public void failed(String log) {
                    dataIsLoading.set(false);
                    activity.runOnUiThread(() -> {
                                ToastUtil.showShort(activity, log);
                            }
                    );


                }
            }, g);

        }).start();

    }


}
