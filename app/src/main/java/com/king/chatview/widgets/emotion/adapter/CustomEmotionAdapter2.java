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

import java.util.List;

/**
 * Created by Administrator on 2015/11/13.
 */
public class CustomEmotionAdapter2 extends BaseEmotionAdapter<CustomEmotionAdapter2.CustomEmotionListAdapter> {
    private List<String> customImgPathList;

    public CustomEmotionAdapter2(Context context, ViewPager viewPager, EmotionData<String> emotionData, EmotionView.EmotionClickListener emotionClickListener) {
        super(context, viewPager, emotionData, emotionClickListener);
        initData(emotionData);
    }

    private void initData(EmotionData<String> emotionData) {
        if (emotionData.getUniqueItem() != null) {
            List<String> list = emotionData.getEmotionList();
            list.add(0, emotionData.getUniqueItem());
            emotionData.setEmotionList(list);
        }
        customImgPathList = emotionData.getEmotionList();
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
    public CustomEmotionListAdapter createListAdapter() {
        return new CustomEmotionListAdapter();
    }

    @Override
    public CustomEmotionListAdapter bingData(CustomEmotionListAdapter listAdapter, int position) {
        int startP = position * 8;
        int endP = (position + 1) * 8 > customImgPathList.size() ? customImgPathList.size() : (position + 1) * 8;
        String[] list = customImgPathList.subList(startP, endP).toArray(new String[endP - startP]);
        listAdapter.setData(list);
        return listAdapter;
    }

    @Override
    public void onClick(View v) {
        Object path = v.getTag();
        if (path != null && mEmotionClickListener != null) {
            String pathString = (String) path;
            if (mEmotionData.getUniqueItem() != null) {
                if (pathString.equals(mEmotionData.getUniqueItem())) {
//                    mEmotionClickListener.OnUniqueEmotionClick();
                } else {
//                    mEmotionClickListener.OnEmotionClick();
                }
            } else {
//                mEmotionClickListener.OnEmotionClick();
            }
        }
    }

    public List<String> getImgList() {
        return customImgPathList;
    }

    public void setImgList(List<String> customImgPathList) {
        this.customImgPathList = customImgPathList;
        notifyDataSetChanged();
    }

    class CustomEmotionListAdapter extends BaseEmotionAdapter.BaseListAdapter {
        private String[] pathList;

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
            // tag 中保留了这张图片的路径
            if (pathList[position] != null) {
                ImageButton img = new ImageButton(mContext);
                img.setBackgroundResource(R.drawable.sticker_style);
                containerLayout.setLayoutParams(new GridView.LayoutParams(mSize, mSize));
                containerLayout.setGravity(Gravity.CENTER);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.setMargins(mSize / 9, mSize / 9, mSize / 9, mSize / 9);
                img.setLayoutParams(params);
                img.setOnClickListener(CustomEmotionAdapter2.this);
                // 加载
                Glide.with(mContext).load(pathList[position]).centerCrop().into(img);
                img.setTag(pathList[position]);
                containerLayout.addView(img);
            }
            return containerLayout;
        }
    }
}
