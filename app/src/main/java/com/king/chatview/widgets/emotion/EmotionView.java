package com.king.chatview.widgets.emotion;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.king.chatview.R;
import com.king.chatview.tools.DImenUtil;
import com.king.chatview.widgets.CustomIndicator;
import com.king.chatview.widgets.emotion.adapter.BaseEmotionAdapter;
import com.king.chatview.widgets.emotion.adapter.CustomEmotionAdapter2;
import com.king.chatview.widgets.emotion.adapter.EmotionAdapter2;
import com.king.chatview.widgets.emotion.item.StickerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/11.
 */
public class EmotionView extends LinearLayout {

    private CustomEmotionAdapter2.CustomEmotion customEmotion;
    private RelativeLayout emotionLinearLayout;
    private ViewPager emotionViewPager;
    private CustomIndicator emotionIndicator;

    private LinearLayout stickersSlider;
    private List<ImageButton> stickerList = new ArrayList<>();
    // 用来添加表情包的按钮
    private ImageView addStickers;


    List<BaseEmotionAdapter> emotionAdapterList;

    private BaseEmotionAdapter emotionAdapter;
    private BaseEmotionAdapter customAdapter;

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

    private void init(Context context) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //加载布局文件
        mInflater.inflate(R.layout.emotion_view, this, true);

        emotionLinearLayout = (RelativeLayout) findViewById(R.id.emotionLinearLayout);
        emotionViewPager = (ViewPager) findViewById(R.id.emotionViewPager);
        emotionIndicator = (CustomIndicator) findViewById(R.id.emotionIndicator);

        stickersSlider = (LinearLayout) findViewById(R.id.stickers_slider);
        addStickers = (ImageView) findViewById(R.id.add_stickers);

        emotionAdapterList = new ArrayList<>();

        emotionAdapter = new EmotionAdapter2(context, emotionViewPager);

        emotionAdapterList.add(emotionAdapter);
        emotionAdapterList.add(customAdapter);

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
        this.setEmotionAdapter(emotionAdapter);

        // emoji
        ImageButton emoji = new StickerItem(context, R.drawable.u1f004);
        this.addStickerButton(emoji);

        // custom
        ImageButton custom = new StickerItem(context, R.mipmap.ic_launcher);
        this.addStickerButton(custom);

        emoji.setSelected(true);
        initStickerOnClickListener();
    }

    private void addStickerButton(ImageButton button) {
        stickersSlider.addView(button);
        stickerList.add(button);
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
        BaseEmotionAdapter adapter;
        if (index == 0) {
            adapter = emotionAdapter;
        } else {
            adapter = emotionAdapterList.get(index);
            if (adapter == null) {
                adapter = new CustomEmotionAdapter2(mContext, emotionViewPager);
                emotionAdapterList.set(index, adapter);
            }
        }
        this.setEmotionAdapter(adapter);
    }

    private void setEmotionAdapter(BaseEmotionAdapter adapter) {
        emotionViewPager.setAdapter(adapter);
        showEmotionIndicator(adapter.getCount());
    }

    public CustomEmotionAdapter2.CustomEmotion getCustomEmotionListener() {
        return customEmotion;
    }

    public void setCustomEmotionListener(CustomEmotionAdapter2.CustomEmotion customEmotion) {
        this.customEmotion = customEmotion;
    }

    public void setCustomEmotionPathList(List<String> pathList) {
        if (emotionAdapterList == null)
            return;
        CustomEmotionAdapter2 adapter = (CustomEmotionAdapter2) emotionAdapterList.get(1);
        if (adapter == null) {
            adapter = new CustomEmotionAdapter2(mContext, emotionViewPager, pathList, customEmotion);
            emotionAdapterList.set(1, adapter);
        } else {
            adapter.setImgList(pathList);
        }
    }
}
