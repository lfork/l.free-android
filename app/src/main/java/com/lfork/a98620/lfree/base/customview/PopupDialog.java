package com.lfork.a98620.lfree.base.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.lfork.a98620.lfree.R;

/**
 * Created by 98620 on 2018/4/14.
 */

public class PopupDialog {
    private PopupWindow avatorPop;

    public PopupDialog(Context context, PopupDialogOnclickListener listener, View parent) {
        this.context = context;
        this.listener = listener;
        this.parent = parent;
    }

    private Context context;

    private  PopupDialogOnclickListener listener;

    private View parent;

    private int mScreenWidth;

    @SuppressLint("ClickableViewAccessibility")
    public void show() {
        View view = LayoutInflater.from(context).inflate(R.layout.user_info_this_pop_show_dialog, null);
        RelativeLayout layout_choose = view.findViewById(R.id.layout_choose);
        RelativeLayout layout_photo = view.findViewById(R.id.layout_photo);
        RelativeLayout layout_close = view.findViewById(R.id.layout_close);
        layout_photo.setOnClickListener(arg0 -> {
            avatorPop.dismiss();
            listener.onFirstButtonClicked(this);

        });

        layout_choose.setOnClickListener(arg0 -> {
            avatorPop.dismiss();
            listener.onSecondButtonClicked(this);

        });

        layout_close.setOnClickListener(view1 -> {
            avatorPop.dismiss();
            listener.onCanceledClicked(PopupDialog.this);
        });
        DisplayMetrics metric = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        avatorPop = new PopupWindow(view, mScreenWidth, 200);
        avatorPop.setTouchInterceptor((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                avatorPop.dismiss();
                return true;
            }
            return false;
        });
        avatorPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        avatorPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        avatorPop.setTouchable(true);
        avatorPop.setFocusable(true);
        avatorPop.setOutsideTouchable(true);
        avatorPop.setBackgroundDrawable(new BitmapDrawable());
        // 动画效果 从底部弹起
        avatorPop.setAnimationStyle(R.style.AppTheme);
        avatorPop.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

}
