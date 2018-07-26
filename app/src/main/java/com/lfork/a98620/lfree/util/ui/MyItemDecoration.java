package com.lfork.a98620.lfree.util.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MyItemDecoration extends RecyclerView.ItemDecoration{
    private int halfSpace;
    private Paint paint;
    /**
     * @param space item之间的间隙
     */
    public MyItemDecoration(int space, Context context, int color) {
        setSpace(space);
        paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿
        paint.setColor(color);//设置背景色
    }
    public void setSpace(int space) {
        this.halfSpace = space / 2;
    }
    /**
     *
     * 重写onDraw 方法以实现recyclerview的item之间的间隙的背景
     * @param c 画布
     * @param parent 使用该 ItemDecoration 的 RecyclerView 对象实例
     * @param state 使用该 ItemDecoration 的 RecyclerView 对象实例的状态
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int outLeft, outTop, outRight, outBottom,viewLeft,viewTop,viewRight,viewBottom;
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            viewLeft = view.getLeft();
            viewTop = view.getTop();
            viewRight = view.getRight();
            viewBottom = view.getBottom();
// item外层的rect在RecyclerView中的坐标
            outLeft = viewLeft - halfSpace;
            outTop = viewTop - halfSpace;
            outRight = viewRight + halfSpace;
            outBottom = viewBottom + halfSpace;
//item 上方的矩形
            c.drawRect(outLeft, outTop, outRight,viewTop, paint);
//item 左边的矩形
            c.drawRect(outLeft,viewTop,viewLeft,viewBottom,paint);
//item 右边的矩形
            c.drawRect(viewRight,viewTop,outRight,viewBottom,paint);
//item 下方的矩形
            c.drawRect(outLeft,viewBottom,outRight,outBottom,paint);
        }
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = halfSpace;
        outRect.bottom = halfSpace;
        outRect.left = halfSpace;
        outRect.right = halfSpace;
    }
}