package com.king.chatview.widgets.emotion.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

/**
 * Created by Administrator on 2015/11/12.
 */
public abstract class BaseEmotionAdapter<T extends BaseEmotionAdapter.BaseListAdapter> extends PagerAdapter implements View.OnClickListener {

    protected Context mContext;
    protected ViewPager mEmotionViewPager;
    protected int mSize;
    protected int mCount;

    protected int viewPageWidth;

    protected ViewHolder mViewHolder;

    public BaseEmotionAdapter(Context context, ViewPager viewPager) {
        this.mContext = context;
        this.mEmotionViewPager = viewPager;

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        viewPageWidth = dm.widthPixels;
        initData();
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        ViewHolder holder = (ViewHolder) object;
        return view == holder.gridView;
    }

    @Override
    public final Object instantiateItem(ViewGroup container, int position) {
        GridView gridView;
        T adapter;
        ViewHolder holder;
        if (this.mViewHolder == null) {
            gridView = instantiateGridView();
            // emotionViewPager在initData阶段是可能没有高度的(gone) 只有在emotionViewPager进行页面填充的时候才一定会有高度
            int viewPageHeight = getEmotionPageViewHeight();
            gridView = setGridViewMinimumHeight(gridView, viewPageHeight);
            gridView = setGridViewSpacing(gridView, viewPageHeight, viewPageWidth);

            adapter = createListAdapter();
            holder = new ViewHolder();
            holder.gridView = gridView;
            holder.adapter = adapter;
        } else {
            gridView = mViewHolder.gridView;
            adapter = mViewHolder.adapter;
            holder = mViewHolder;
            mViewHolder = null;
        }
        adapter = bingData(adapter, position);
        gridView.setAdapter(adapter);
        container.addView(gridView);
        return holder;
    }

    public abstract GridView instantiateGridView();

    public abstract T createListAdapter();

    public abstract T bingData(T listAdapter, int position);

    protected abstract void initData();

    protected GridView setGridViewMinimumHeight(GridView gridView, int height) {
        gridView.setMinimumHeight(height);
        return gridView;
    }

    protected abstract GridView setGridViewSpacing(GridView gridView, int viewPageHeight, int viewPageWeight);

    private int getEmotionPageViewHeight() {
        return View.MeasureSpec.getSize(mEmotionViewPager.getMeasuredHeight());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        this.mViewHolder = ((ViewHolder) object);
        container.removeView(this.mViewHolder.gridView);
    }


    public abstract class BaseListAdapter extends BaseAdapter {
    }

    protected class ViewHolder {
        GridView gridView;
        T adapter;
    }
}
