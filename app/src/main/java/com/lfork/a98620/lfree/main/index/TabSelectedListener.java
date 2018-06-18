package com.lfork.a98620.lfree.main.index;

import android.support.design.widget.TabLayout;
import android.util.Log;

public class TabSelectedListener implements TabLayout.OnTabSelectedListener{
        private static final String TAG = "TabSelectedListener";
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            Log.d(TAG, "onTabSelected: " + tab.getPosition() + tab.getTag());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    }