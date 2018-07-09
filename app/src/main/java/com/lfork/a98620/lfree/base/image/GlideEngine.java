package com.lfork.a98620.lfree.base.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.lfork.a98620.lfree.base.bindingadapter.ImageBinding;
import com.zhihu.matisse.engine.ImageEngine;

/**
 * Created by 98620 on 2018/4/14.
 */


public class GlideEngine implements ImageEngine {

    @Override
    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        setImage(imageView, uri);
    }

    @Override
    public void loadAnimatedGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        setImage(imageView, uri);
    }

    @Override
    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        setImage(imageView, uri);
    }

    @Override
    public void loadAnimatedGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        setImage(imageView, uri);
    }
    
    private void setImage(ImageView imageView, Uri uri){
        ImageBinding.setImageWithDiskCache(imageView, uri);
    }


    @Override
    public boolean supportAnimatedGif() {
        return true;
    }
}
