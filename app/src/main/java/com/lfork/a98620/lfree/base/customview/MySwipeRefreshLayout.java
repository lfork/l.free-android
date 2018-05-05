package com.lfork.a98620.lfree.base.customview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.daimajia.swipe.SwipeLayout;


/**
 * Created by 98620 on 2018/4/12.
 */

public class MySwipeRefreshLayout extends SwipeLayout {

    private int pressX, pressY;//按下时的X Y坐标
    private boolean isIntercept=true;//是否拦截此次事件
    private boolean isJustClose =false;//本次按下时是否是关闭swipeLayout
    private RecyclerView listView;//绑定的listview
    private SwipeLayout swipeLayout;//当前打开的SwipeLayout

    private static final String TAG = "MySwipeRefreshLayout";

    public MySwipeRefreshLayout(Context context) {
        super(context);
    }

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN :
                pressX = (int) ev.getX();
                pressY = (int) ev.getY();
                if (swipeLayout!=null ){//如果SwipeLayout不为null说明已经有过打开的SwipeLayout所以就行关闭
                    swipeLayout.close();
                    isJustClose =true;//本次DOWN事件中关闭了close
                }
                break;

            case MotionEvent.ACTION_MOVE :

                Log.d(TAG, "onInterceptTouchEvent: ");
                if ( ! isIntercept)
                    return false ;
                if (isJustClose)//本次DOWN事件中关闭了SwipeLayout所以本次不会再走move事件，即不会滑出SwipeLayout
                    break ;
                double angle= Math.atan((ev.getY()- pressY)/(ev.getX()- pressX));//计算滑动的角度
                int degrees= (int) Math.toDegrees(angle);
                if (degrees>=-45&&degrees<=45){//如果滑动在45度以内则滑出SwipeLayout
                    isIntercept=false;//滑出SwipeLayout后需要进行拦截listview的滑动
                    swipeLayout=getChildSwipeLayout();//获得当前滑出的SwipeLayout
                    return false;
                }else{
                    break;
                }

            case  MotionEvent.ACTION_UP :
                isJustClose =false;//将次参数还原为默认值
                if (swipeLayout!=null && isIntercept){//SwipeLayout不为null说明有展开的SwipeLayout，并且滑出SwipeLayout的事件已经结束
                    if (swipeLayout==getChildSwipeLayout())//如果当前点击的SwipeLayout和展开的是一样的则做默认处理
                        swipeLayout.close();
                    swipeLayout=null ;
                    return true ;
                }
                isIntercept=true;////将次参数还原为默认值
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 绑定对当前的listivew
     */
    public void setRecyclerView(RecyclerView listView){
        this.listView=listView;
    }

    /**
     * 获取当前点击位置的子View即SwipeLayout
     * @return 当前的SwipeLayout
     */
    private SwipeLayout getChildSwipeLayout(){
        int position=listView.getChildLayoutPosition(this);
        position=position- listView.getChildLayoutPosition(this);//获取当前显示view的下标

        return (SwipeLayout) listView.getChildAt(position);//获取当前SwipeLayout
    }
}
