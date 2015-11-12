package com.king.chatview.widgets.emotion.item;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.king.chatview.R;

/**
 * Created by Administrator on 2015/11/11.
 */
public class StickerItem extends ImageButton {

    private Context mContext;

    public StickerItem(Context context) {
        this(context, null);
    }

    public StickerItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickerItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        this.setImageResource(R.mipmap.ic_launcher);
        init(context);
    }

    public StickerItem(Context context, int backgroundResId) {
        this(context);
        init(context);
        this.setImageResource(backgroundResId);
    }

    private void init(Context context) {
        this.mContext = context;
        this.setScaleType(ScaleType.CENTER_INSIDE);
        this.setBackgroundResource(R.drawable.sticker_style);
        this.setPadding(5, 5, 5, 5);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hehe", "sticker click");
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = mContext.getResources().getDisplayMetrics().widthPixels / 4;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
        //        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}

