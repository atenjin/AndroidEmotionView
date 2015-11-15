package com.king.chatview.widgets.emotion.item;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.king.chatview.R;

/**
 * Created by Administrator on 2015/11/11.
 */
public class StickerItem extends ImageButton {

    private int itemWidth = 0;

    public StickerItem(Context context) {
        super(context);
        init(context);
    }

    public StickerItem(Context context, int backgroundResId) {
        this(context);
        Glide.with(context).load(backgroundResId).into(this);
    }

    public StickerItem(Context context, String path) {
        this(context);
        Glide.with(context).load(path).into(this);
    }

    public StickerItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickerItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.setScaleType(ScaleType.CENTER_INSIDE);
        this.setBackgroundResource(R.drawable.sticker_style);
        this.setPadding(5, 5, 5, 5);
        itemWidth = context.getResources().getDisplayMetrics().widthPixels / 4;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (itemWidth != 0) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY);
            setMeasuredDimension(widthMeasureSpec, getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}