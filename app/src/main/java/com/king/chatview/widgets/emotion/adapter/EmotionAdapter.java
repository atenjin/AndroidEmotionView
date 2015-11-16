package com.king.chatview.widgets.emotion.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.king.chatview.R;
import com.king.chatview.widgets.emotion.EmotionView;
import com.king.chatview.widgets.emotion.data.Emoticon;
import com.king.chatview.widgets.emotion.data.EmotionData;

import java.util.List;

/**
 * Created by Administrator on 2015/11/12.
 */
public class EmotionAdapter extends BaseEmotionAdapter<EmotionAdapter.EmotionListAdapter> {

    private Emoticon[][] mPageEmoticon;

    public int rate;
    private int item_length;

    public EmotionAdapter(Context context, ViewPager viewPager, EmotionData data, EmotionView.EmotionClickListener clickListener) {
        super(context, viewPager, data, clickListener);
        initData(data);
    }

    @Override
    protected int calcPageCount(EmotionData emotionData) {
        // 判断unique item 是否存在 存在就是-1 不存在就不处理
        int count = super.calcPageCount(emotionData);
        if (emotionData.getUniqueItem() != null)
            count = count - 1;
        return count;
    }

    private void initData(EmotionData data) {
        List<Emoticon> emojiList = data.getEmotionList();


        mPageEmoticon = new Emoticon[this.mCount][mRow * mColumn];

        for (int i = 0; i < this.mCount; i++) {
            for (int j = 0; (j < mPageCount) && (i * mPageCount + j < emojiList.size()); j++) {
                Emoticon emoji = emojiList.get(i * mPageCount + j);
                mPageEmoticon[i][j] = emoji;
            }
        }
        rate = (viewPageWidth / (mColumn * 10 + 3));
        item_length = (this.rate * 9);
    }

    @NonNull
    @Override
    public GridView instantiateGridView() {
        GridView gridView = (GridView) LayoutInflater.from(mContext).inflate(R.layout.bx_emotion, null);
        gridView.setScrollContainer(false);
        gridView.setPadding(this.rate * 2, this.rate * 2, this.rate * 2, 0);
        return gridView;
    }

    @NonNull
    @Override
    public EmotionListAdapter createListAdapter(int currentPageNumber) {
        return new EmotionListAdapter();
    }

    @Override
    public EmotionListAdapter bingData(EmotionListAdapter listAdapter, int position) {
        listAdapter.setData(this.mPageEmoticon[position]);
        return listAdapter;
    }

    @Override
    protected void setGridViewSpacing(GridView gridView, int viewPageHeight, int viewPageWeight) {
        super.setGridViewSpacing(gridView, viewPageHeight, viewPageWeight);
        gridView.setHorizontalSpacing(rate);
    }

    @Override
    public EmotionData getEmotionData() {
        return mEmotionData;
    }

    @Override
    public void setEmotionData(EmotionData emotionData) {
        this.mEmotionData = emotionData;
    }

    class EmotionListAdapter extends BaseEmotionAdapter.BaseListAdapter {
        private Emoticon[] emoticonArr;
        public void setData(Emoticon[] emoticons) {
            this.emoticonArr = emoticons;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (this.emoticonArr != null) {
                return this.emoticonArr.length;
            }
            return 0;
        }

        @Override
        public Emoticon getEmoticonItem(int position) {
            if (position == this.emoticonArr.length - 1) {
                return mEmotionData.getUniqueItem();
            }
            return this.emoticonArr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView img = new ImageView(mContext);

            img.setLayoutParams(new GridView.LayoutParams(item_length, item_length));
            img.setPadding(rate, rate, rate, rate);

            img.setVisibility(View.INVISIBLE);

            if (mEmotionData.getUniqueItem() == null) {
                if (this.emoticonArr[position].getResourceId() != 0) {
                    img.setImageResource(this.emoticonArr[position].getResourceId());
                } else {
                    Log.e(BaseEmotionAdapter.EMOTION_ADAPTER_TAG, "Emoticon的getResourceId必须被实现");
                }
                img.setVisibility(View.VISIBLE);
            } else {
                if (this.emoticonArr[position] != null) {
                    img.setImageResource(this.emoticonArr[position].getResourceId());
                    img.setVisibility(View.VISIBLE);
                } else if (position == this.emoticonArr.length - 1) {
                    if (mEmotionData.getUniqueItem().getResourceId() != 0)
                        img.setImageResource(mEmotionData.getUniqueItem().getResourceId());
                    else
                        Log.e(BaseEmotionAdapter.EMOTION_ADAPTER_TAG, "Emoticon的getResourceId必须被实现");
                    img.setVisibility(View.VISIBLE);
                }
            }
            return img;
        }
    }
}
