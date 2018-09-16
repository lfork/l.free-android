package com.lfork.a98620.lfree.main.index;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;

import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.base.entity.Category;
import com.lfork.a98620.lfree.data.goods.GoodsDataRepository;
import com.lfork.a98620.lfree.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by 98620 on 2018/3/31.
 */

public class IndexFragmentViewModel extends BaseObservable {

    public final ObservableArrayList<Category> categories = new ObservableArrayList<>();
    private IndexFragmentNavigator navigator;
    private Context context;
    private GoodsDataRepository repository;
    public final ObservableBoolean dataIsLoading = new ObservableBoolean(true);

    private ArrayList<String> announcementsUrls = new ArrayList<>();

    IndexFragmentViewModel(Context context) {
        this.context = context;
    }

    public void start(){
        initData();
    }

    /**
     *  数据只加载一次 ，第二次加载需要用户的手动刷新
     */
    private void initData() {

        if (categories.size() != 0){
            return;
        }
        announcementsUrls.add("http://www.lfork.top/luo/lfree/HTML/MS2.html");
        announcementsUrls.add("https://mp.weixin.qq.com/s/tcg4CFph33DR69sYBpQhBQ");
        announcementsUrls.add("https://mp.weixin.qq.com/s/tAImac9BHMfxp_xRSZQ9dg");

        repository = GoodsDataRepository.INSTANCE;
        new Thread(() -> {
            repository.getCategories(new DataSource.GeneralCallback<List<Category>>() {
                @Override
                public void succeed(List<Category> data) {
                        categories.clear();
                        categories.addAll(data);
//                        if (navigator != null) {
//                            navigator.onCategoriesLoaded(categories);
//                        }
                    ToastUtil.showShort(context, "分类数据加载成功");
                        dataIsLoading.set(false);
                }

                @Override
                public void failed(String log) {
                    if (navigator != null) {
                        ToastUtil.showShort(context, "数据加载失败，请检查网络连接");
                    }
                }
            });
        }).start();
    }

    public void openSearch(){
        if (navigator != null) {
            navigator.openSearchActivity();
        }
    }

    public void openUrl(int position){
        if (navigator != null) {
            navigator.openWebClient(announcementsUrls.get(position));
        }
    }



    public void setNavigator(IndexFragmentNavigator navigator) {
        this.navigator = navigator;
    }

    public void onDestroy(){
        this.navigator = null;
    }
}
