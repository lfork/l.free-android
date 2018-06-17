package com.lfork.a98620.lfree.main.index;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.data.entity.Category;

import java.util.ArrayList;
import java.util.List;

class PagerItemAdapter extends PagerAdapter implements TabLayout.OnTabSelectedListener {
    private List<Category> pageTitles;

    private List<PagerItemView> viewList = new ArrayList<>();//view数组

    private Activity context;

    PagerItemAdapter(List<Category> pageTitles, Activity context) {
        this.context = context;
        this.pageTitles = pageTitles;
    }

    //判断是否是由对象生成的界面
    @Override
    public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
        return arg0 == arg1;
    }

    public PagerItemView getPagerItemView(int index) {
        if (viewList.size() == 0) {
            return null;
        }
        return viewList.get(index);
    }


    /**
     * 这个 Count会影响 View的绘制，count = 目标view的个数，而不是已经绘制了的个数。
     *
     * @return 将会绘制的view个数
     */
    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position,
                            @NonNull Object object) {
        PagerItemView view = viewList.get(position);
        view.onDestroy();
        container.removeView(view);
    }


    /**
     * Create the page for the given position.  The adapter is responsible
     * for adding the view to the container given here, although it only
     * must ensure this is done by the time it returns from
     * {@link #finishUpdate(ViewGroup)}.
     *
     * @param parent   The containing View in which the page will be shown.
     * @param position The page position to be instantiated.
     * @return Returns an Object representing the new page.  This does not
     * need to be a View, but can be some other container of the page.
     * <p>
     * {@link #getCount()} > 0的时候才能调用这个方法 && notify的时候会重载当前页面(count>0)
     */

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup parent, int position) {
        PagerItemView itemView = viewList.get(position);
        parent.addView(itemView.onCreateView(parent, pageTitles.get(position)));
        return itemView.getView();
    }

    /**
     * 对需要加载的子view进行空初始化 ，需要的时候再onCreate
     */
    private void preInitItemView(ViewGroup parent) {
        for (Category ignored : pageTitles) {
            PagerItemView itemView = new PagerItemView(parent);
            viewList.add(itemView);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles.get(position).getName();
    }

    public void replaceData(List<Category> items, ViewPager parent) {
        this.pageTitles.addAll(items);
        preInitItemView(parent);
        notifyDataSetChanged(); //先通知数据改变，再绑定数据
    }

    /**
     * 先加载前3个页面，当点击到指定页面的时候就加载指定的前后三个页面的数据
     *
     * @param tab 被选中的标签
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        PagerItemView pagerItemView = viewList.get(tab.getPosition());
        pagerItemView.setActivityContext(context);
        pagerItemView.onResume();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        viewList.get(tab.getPosition()).onPause();
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        viewList.get(tab.getPosition()).onResume();
    }

//    public void onDestroy(){
        //缓存的问题  (对于常用的页面 我们应该做一下缓存  而不是直接回收掉)
        // 没错，当前的页面已经是做到位了，很好

        //然后，就算当前的activity被destroy了，这些相应的view  和context 根据 java gc “活的对象的操作”  也是会被回收掉的
//    }
}