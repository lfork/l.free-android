package com.lfork.a98620.lfree.base.bindingadapter;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.util.GlideOptions;

/**
 * Created by 98620 on 2018/4/7.
 */

public class ImageBinding {
    @BindingAdapter({"setImageNoCache"})
    public static void setImageNoCache(ImageView view, String imageUrl) {
        GlideOptions options = GlideOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.login_logo).skipMemoryCache(true);
        Glide.with(view.getContext()).load(imageUrl).apply(options).into(view);
    }

    public static void setImageNoCache(ImageView view,Object path) {
        GlideOptions options = GlideOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).skipMemoryCache(true);
        Glide.with(view.getContext()).load( path).apply(options).into(view);
    }

    @BindingAdapter({"setImageWithDiskCache"})
    public static void setImageWithDiskCache(ImageView view, String imageUrl) {
        GlideOptions options = GlideOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).placeholder(R.drawable.login_logo).skipMemoryCache(false);
        Glide.with(view.getContext()).load(imageUrl).apply(options).into(view);
    }

    @BindingAdapter({"setImage"})
    public static void setImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext()).load(imageUrl).into(view);
    }

    public static void setImageNoCache(ImageView view, Uri uri) {
        GlideOptions options = GlideOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).skipMemoryCache(true);
        Glide.with(view.getContext()).load(uri).apply(options).into(view);
    }

    public static void setImage(ImageView view, Uri uri) {
        Glide.with(view.getContext()).load(uri).into(view);
    }


    public static void setImageWithDiskCache(ImageView view, Uri uri) {
        GlideOptions options = GlideOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).skipMemoryCache(false);
        Glide.with(view.getContext()).load(uri).apply(options).into(view);
    }

}
