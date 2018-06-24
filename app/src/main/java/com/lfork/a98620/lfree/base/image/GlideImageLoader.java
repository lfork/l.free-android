package com.lfork.a98620.lfree.base.image;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.lfork.a98620.lfree.base.bindingadapter.ImageBinding;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        if (isValidContextForGlide(context))
            ImageBinding.setImageNoCache(imageView, path);
    }

//    final Context  context = getContext();
//if (isValidContextForGlide(context) {
//        // Load image via Glide lib using context
//    }

    private static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            return !activity.isDestroyed() && !activity.isFinishing();
        }
        return true;
    }
}