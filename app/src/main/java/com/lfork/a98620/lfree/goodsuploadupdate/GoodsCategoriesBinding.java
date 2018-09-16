package com.lfork.a98620.lfree.goodsuploadupdate;

import android.databinding.BindingAdapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.lfork.a98620.lfree.data.base.entity.Category;

import java.util.List;

/**
 * Created by 98620 on 2018/6/2.
 */
public class GoodsCategoriesBinding {
    private static final String TAG = "GoodsListBinding";
    @SuppressWarnings("unchecked")
    @BindingAdapter("categories_items")
    public static void setItems(Spinner spinner, List<Category> items) {
        ArrayAdapter<Category> arrayAdapter = new ArrayAdapter<Category>(spinner.getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(arrayAdapter);
    }
}
