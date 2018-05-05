package com.lfork.a98620.lfree.base.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.lfork.a98620.lfree.R;


/**
 * Created by 98620 on 2018/5/5.  recyclerView或是ListView下拉刷新时的页脚
 */
public class RefreshFooterView extends LinearLayout {
    public RefreshFooterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from (context) . inflate (R.layout.refresh_footer, this);
    }
}
