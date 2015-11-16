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
import com.king.chatview.widgets.emotion.data.EmotionData;
import com.king.chatview.widgets.emotion.item.StickerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/11.
 */
public class EmotionView extends LinearLayout {

    public EmotionClickListener getEmotionClickListener() {
        return emotionClickListener;
    }

    public void setEmotionClickListener(EmotionClickListener emotionClickListener) {
        this.emotionClickListener = emotionClickListener;
        if (emotionAdapterList == null) {
            throw new NullPointerException("设置监听前必须先设定表情的List");
        }
        for (BaseEmotionAdapter adapter : emotionAdapterList) {
            adapter.setEmotionClickListener(emotionClickListener);
        }
    }


    public interface EmotionClickListener {
        void OnEmotionClick(Object emotionData, View v, EmotionData.EmotionCategory category);

        void OnUniqueEmotionClick(Object uniqueItem, View v, EmotionData.EmotionCategory category);
    }

    private EmotionClickListener emotionClickListener;

    private List<EmotionData> emotionDataList;

    private RelativeLayout emotionLinearLayout;
    private ViewPager emotionViewPager;
    private CustomIndicator emotionIndicator;

    private LinearLayout stickersSlider;
    private List<ImageButton> stickerList = new ArrayList<>();
    // 用来添加表情包的按钮
    private ImageView addStickers;

    private List<BaseEmotionAdapter> emotionAdapterList;

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
    }

    private void init(Context context, List<EmotionData> emotionDataList) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //加载布局文件
        mInflater.inflate(R.layout.emotion_view, this, true);

        emotionLinearLayout = (RelativeLayout) findViewById(R.id.emotionLinearLayout);
        emotionViewPager = (ViewPager) findViewById(R.id.emotionViewPager);
        emotionIndicator = (CustomIndicator) findViewById(R.id.emotionIndicator);

        stickersSlider = (LinearLayout) findViewById(R.id.stickers_slider);
        // 暂时未使用
        addStickers = (ImageView) findViewById(R.id.add_stickers);

        // 重构
        // 初始化tab
        initStickers(emotionDataList);

        // 点开界面第一个tab元素必须被初始化
        emotionAdapterList = new ArrayList<>();
        int index = 0;
        for (EmotionData data : emotionDataList) {
            BaseEmotionAdapter emotionAdapter = this.createEmotionAdapter(data);
            if (index == 0) {
                // emoji
                this.setEmotionAdapter(emotionAdapter);
            }
            emotionAdapterList.add(emotionAdapter);
            index++;
        }

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

    private void initStickers(List<EmotionData> emotionDataList) {
        int index = 0;
        for (EmotionData data : emotionDataList) {
            final StickerItem stickerTab = new StickerItem(mContext, data.getStickerIcon());
            this.addStickerButton(stickerTab);
            if (index == 0) {
                // 初始化选中第一个tab
                stickerTab.setSelected(true);
            }
            // 设置监听
            final int tempIndex = index;
            stickerTab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (ImageButton temp : stickerList) {
                        temp.setSelected(false);
                    }
                    stickerTab.setSelected(true);
                    switchOtherStickers(tempIndex);
                }
            });
            index++;
        }
    }

    private int currentStickerIndex = 0;

    private void switchOtherStickers(int index) {
        this.currentStickerIndex = index;
        this.setEmotionAdapter(emotionAdapterList.get(index));
    }

    private void setEmotionAdapter(BaseEmotionAdapter adapter) {
        emotionViewPager.setAdapter(adapter);
        showEmotionIndicator(adapter.getCount());
    }

    public void setEmotionDataList(List<EmotionData> emotionDataList) {
        this.emotionDataList = emotionDataList;
        init(mContext, emotionDataList);
    }

    public List<EmotionData> getEmotionDataList() {
        return emotionDataList;
    }

    public void modifyEmotionDataList(EmotionData data, int position) {
        emotionDataList.set(position, data);
        BaseEmotionAdapter adapter = createEmotionAdapter(data);
        emotionAdapterList.set(position, adapter);
        if (position == currentStickerIndex) {
            setEmotionAdapter(adapter);
        }
    }

    private BaseEmotionAdapter createEmotionAdapter(EmotionData data) {
        BaseEmotionAdapter adapter = null;
        switch (data.getCategory()) {
            case emoji:
                adapter = new EmotionAdapter2(mContext, emotionViewPager, data, emotionClickListener);
                break;
            case image:
                adapter = new CustomEmotionAdapter2(mContext, emotionViewPager, data, emotionClickListener);
            default:
        }
        return adapter;
    }
}
