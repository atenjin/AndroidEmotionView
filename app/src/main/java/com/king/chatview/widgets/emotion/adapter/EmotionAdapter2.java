package com.king.chatview.widgets.emotion.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Administrator on 2015/11/12.
 */
public class EmotionAdapter2 extends BaseEmotionAdapter {
    private static final int ROW_COUNT = 3;
    private static final int COLUMN_COUNT = 7;
    private static final int P_COUNT = ROW_COUNT * COLUMN_COUNT - 1;

    private int[] mDrawableResId;
    private int[][] mDrawablePageId;
    private int[][] decId;

    private int rate = 0;
    private int item_length = 0;
    private int grid_height = 0;

    private EditText editText;


    public EmotionAdapter2(Context context, ViewPager viewPager) {
        this(context, viewPager, null);
    }

    @Override
    protected void initData() {

    }

    public EmotionAdapter2(Context context, ViewPager viewPager, EditText editText) {
        super(context, viewPager);
        this.editText = editText;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
}
