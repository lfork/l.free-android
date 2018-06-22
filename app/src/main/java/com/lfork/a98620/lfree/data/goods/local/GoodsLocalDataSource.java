package com.lfork.a98620.lfree.data.goods.local;

import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.Category;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 98620 on 2018/3/23.
 */

public class GoodsLocalDataSource  {

    private static GoodsLocalDataSource INSTANCE;
//    private ArrayList<ResponseGetUser.UserInfo> mCachedUserList = new ArrayList<>();

    private static final String TAG = "IMRemoteDataSource";

    private GoodsLocalDataSource() {
    }

    public static GoodsLocalDataSource getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            INSTANCE = new GoodsLocalDataSource();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public void getCategories(DataSource.GeneralCallback<List<Category>> callback) {
        List<Category> categories = DataSupport.findAll(Category.class);

        if (categories != null) {
            callback.succeed(categories);
        } else {
            callback.failed("本地数据为空");
        }
    }

}
