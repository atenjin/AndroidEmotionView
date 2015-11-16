package com.king.chatview.widgets.emotion.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.king.chatview.R;
import com.king.chatview.widgets.emotion.EmotionView;
import com.king.chatview.widgets.emotion.data.EmotionData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/13.
 */
public class CustomEmotionAdapter extends BaseEmotionAdapter<CustomEmotionAdapter.CustomEmotionListAdapter> {
    /**
     * 可能会添加uniqueItem至list的index为0的在该adapter操作的List副本
     */
    private List<String> customImgPathList;
    private List<CustomEmotionListAdapter> pageAdapterList = new ArrayList<>();

    public CustomEmotionAdapter(Context context, ViewPager viewPager, EmotionData<String> emotionData, EmotionView.EmotionClickListener emotionClickListener) {
        super(context, viewPager, emotionData, emotionClickListener);
        initData(emotionData);
    }

    private void initData(EmotionData<String> emotionData) {
        if (emotionData.getUniqueItem() != null) {
            customImgPathList = new ArrayList<>(emotionData.getEmotionList());
            customImgPathList.add(0, emotionData.getUniqueItem());
        } else {
            customImgPathList = emotionData.getEmotionList();
        }
    }

    @Override
    protected int calcPageNumber(EmotionData emotionData, int pageCount) {
        int listSize = emotionData.getEmotionList().size();
        if (emotionData.getUniqueItem() != null)
            listSize++;

        int pageNumber;
        if (listSize % mPageCount > 0)
            pageNumber = listSize / mPageCount + 1;
        else {
            pageNumber = listSize / mPageCount;
        }
        return pageNumber;
    }

    @NonNull
    @Override
    public GridView instantiateGridView() {
        GridView gridView = (GridView) LayoutInflater.from(mContext).inflate(R.layout.bx_emotion, null);
        gridView.setScrollContainer(false);
        return gridView;
    }

    @NonNull
    @Override
    public CustomEmotionListAdapter createListAdapter(int currentPageNumber) {
        CustomEmotionListAdapter adapter = new CustomEmotionListAdapter(currentPageNumber);
        pageAdapterList.add(adapter);
        return adapter;
    }

    @Override
    public CustomEmotionListAdapter bingData(CustomEmotionListAdapter listAdapter, int position) {
        int startP = position * mPageCount;
        int endP = (position + 1) * mPageCount > customImgPathList.size() ? customImgPathList.size() : (position + 1) * mPageCount;
        String[] list = customImgPathList.subList(startP, endP).toArray(new String[endP - startP]);
        listAdapter.setData(list);
        return listAdapter;
    }

    @Override
    public EmotionData<String> getEmotionData() {
        return mEmotionData;
    }

    @Override
    public void setEmotionData(EmotionData emotionData) {
        List<String> list = emotionData.getEmotionList();
        customImgPathList = new ArrayList<>(list);
        customImgPathList.add(0, (String) emotionData.getUniqueItem());
        notifyDataSetChanged();
        for (CustomEmotionListAdapter adapter : pageAdapterList) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        int index = (Integer) v.getTag(CustomEmotionAdapter.INDEX_TAG);
        if (mEmotionClickListener != null) {
            if (mEmotionData.getUniqueItem() != null) {
                if (index == 0) {
                    mEmotionClickListener.OnUniqueEmotionClick(mEmotionData.getUniqueItem(), v, mEmotionData.getCategory());
                    return;
                }
                index--;
            }
            mEmotionClickListener.OnEmotionClick(mEmotionData.getEmotionList().get(index), v, mEmotionData.getCategory());
        }
    }


    class CustomEmotionListAdapter extends BaseEmotionAdapter.BaseListAdapter {
        private String[] pathList;

        CustomEmotionListAdapter(int currentPageNumber) {
            super(currentPageNumber);
        }

        public void setData(String[] pathList) {
            this.pathList = pathList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return pathList == null ? 0 : pathList.length;
        }

        @Override
        public Object getItem(int position) {
            return pathList == null ? null : pathList[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RelativeLayout containerLayout = new RelativeLayout(mContext);
            if (pathList == null) {
                return containerLayout;
            }
            if (pathList[position] != null) {
                ImageButton img = new ImageButton(mContext);
                img.setBackgroundResource(R.drawable.sticker_style);
                containerLayout.setLayoutParams(new GridView.LayoutParams(mSize, mSize));
                containerLayout.setGravity(Gravity.CENTER);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.setMargins(mSize / 9, mSize / 9, mSize / 9, mSize / 9);
                img.setLayoutParams(params);
                img.setOnClickListener(CustomEmotionAdapter.this);
                // 加载
                Glide.with(mContext).load(pathList[position]).centerCrop().into(img);

                // 因为特殊元素是直接插入到list的第一个位置上，所以这里不需要区分是否是特殊元素
                int index = mCurrentPageNumber * mPageCount + position;
                img.setTag(CustomEmotionAdapter.INDEX_TAG, index);

                containerLayout.addView(img);
            }
            return containerLayout;
        }
    }

}
