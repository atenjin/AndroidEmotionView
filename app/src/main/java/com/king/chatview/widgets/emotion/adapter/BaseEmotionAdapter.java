package com.king.chatview.widgets.emotion.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;

/**
 * Created by Administrator on 2015/11/12.
 */
public abstract class BaseEmotionAdapter extends PagerAdapter implements View.OnClickListener {

    protected Context mContext;
    protected ViewPager mEmotionViewPager;
    protected int mSize;
    protected int mCount;

    public BaseEmotionAdapter(Context context, ViewPager viewPager) {
        this.mContext = context;
        this.mEmotionViewPager = viewPager;
        initData();
    }

    protected abstract void initData();


    public abstract class BaseListAdapter extends BaseAdapter {
    }

    protected class ViewHolder<T extends BaseListAdapter> {
        GridView gridView;
        T adapter;
    }
}
