package com.king.chatview.widgets.emotion;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.king.chatview.R;
import com.king.chatview.tools.DImenUtil;
import com.king.chatview.widgets.CustomIndicator;
import com.king.chatview.widgets.emotion.adapter.CustomEmotionAdapter;
import com.king.chatview.widgets.emotion.adapter.EmotionAdapter;
import com.king.chatview.widgets.emotion.item.StickerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/11.
 */
public class EmotionView extends LinearLayout {

    private CustomEmotionAdapter.CustomEmotion customEmotion;
    private RelativeLayout emotionLinearLayout;
    private ViewPager emotionViewPager;
    private CustomIndicator emotionIndicator;

    private LinearLayout stickersSlider;
    private List<ImageButton> stickerList = new ArrayList<>();
    private ImageView addStickers;

    private EmotionAdapter emotionAdapter;
    private Context mContext;

    public EmotionView(Context context) {
        this(context, null);
    }

    public EmotionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmotionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        Log.d("hehe", "EmotionView height" + MeasureSpec.getSize(heightMeasureSpec));
//        if (emotionAdapter != null) {
//
//            Log.d("hehe", "emotionViewPager.getHeight()" + emotionViewPager.getHeight());
//            Log.d("hehe", "emotionViewPager.getMeasuredHeight()" + MeasureSpec.getSize(emotionViewPager.getMeasuredHeight()));
//        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init(Context context) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //加载布局文件
        mInflater.inflate(R.layout.emotion_view, this, true);

        emotionLinearLayout = (RelativeLayout) findViewById(R.id.emotionLinearLayout);
        emotionViewPager = (ViewPager) findViewById(R.id.emotionViewPager);
        emotionIndicator = (CustomIndicator) findViewById(R.id.emotionIndicator);

        Log.d("hehe", "emotion view pager:" + emotionViewPager.getWidth());

        stickersSlider = (LinearLayout) findViewById(R.id.stickers_slider);
        addStickers = (ImageView) findViewById(R.id.add_stickers);

        emotionAdapter = new EmotionAdapter(context, emotionViewPager);

        emotionViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                emotionIndicator.setCurrentPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        emotionViewPager.setAdapter(emotionAdapter);
        showEmotionIndicator(emotionAdapter.getCount());

        // blank
//        stickersSlider.addView(new StickerItem(context));
        // emoj
        ImageButton emoj = new StickerItem(context, R.drawable.u1f004);
        stickersSlider.addView(emoj);
        // custom
        ImageButton custom = new StickerItem(context, R.mipmap.ic_launcher);
        stickersSlider.addView(custom);

        stickerList.add(emoj);
        stickerList.add(custom);

        emoj.setSelected(true);
        initStickerOnClickListener();
    }

    private void showEmotionIndicator(int count) {
        emotionIndicator.setDotCount(count);
        emotionIndicator.setDotHeight(DImenUtil.dip2px(mContext, 5));
        emotionIndicator.setDotWidth(DImenUtil.dip2px(mContext, 5));
        emotionIndicator.setDotMargin(DImenUtil.dip2px(mContext, 10));
        emotionIndicator.show();
    }

    private void initStickerOnClickListener() {
        int index = 0;
        for (final ImageButton sticker : stickerList) {
            final int tempIndex = index;
            sticker.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (ImageButton temp : stickerList) {
                        temp.setSelected(false);
                    }
                    sticker.setSelected(true);
                    switchOtherStickers(tempIndex);
                }
            });

            index++;
        }
    }

    private void switchOtherStickers(int index) {
        if (index == 0) {
            emotionViewPager.setAdapter(emotionAdapter);
            showEmotionIndicator(emotionAdapter.getCount());
        }
        if (index == 1) {
            CustomEmotionAdapter adapter = new CustomEmotionAdapter(mContext, customEmotion, emotionViewPager);
            emotionViewPager.setAdapter(adapter);
            showEmotionIndicator(adapter.getCount());
        }

    }

    public CustomEmotionAdapter.CustomEmotion getCustomEmotion() {
        return customEmotion;
    }

    public void setCustomEmotionListener(CustomEmotionAdapter.CustomEmotion customEmotion) {
        this.customEmotion = customEmotion;
    }
}
