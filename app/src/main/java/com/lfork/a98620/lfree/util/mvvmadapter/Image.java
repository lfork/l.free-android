package com.lfork.a98620.lfree.util.mvvmadapter;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.util.GlideOptions;

/**
 * Created by 98620 on 2018/4/7.
 */

public class Image {
    @BindingAdapter({"setImageNoCache"})
    public static void setImage(ImageView view, String imageUrl) {
        GlideOptions options = GlideOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.login_logo).skipMemoryCache(true);
        Glide.with(view.getContext()).load(imageUrl).apply(options).into(view);
    }

    @BindingAdapter({"setImageWithDiskCache"})
    public static void setImage2(ImageView view, String imageUrl) {
        GlideOptions options = GlideOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).placeholder(R.drawable.login_logo).skipMemoryCache(false);
        Glide.with(view.getContext()).load(imageUrl).apply(options).into(view);
    }

    @BindingAdapter({"setImage"})
    public static void setImage3(ImageView view, String imageUrl) {
        Glide.with(view.getContext()).load(imageUrl).into(view);
    }



}
