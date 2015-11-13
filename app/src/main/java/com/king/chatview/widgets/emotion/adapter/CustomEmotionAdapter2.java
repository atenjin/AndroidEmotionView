package com.king.chatview.widgets.emotion.adapter;

import android.content.ContentResolver;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/13.
 */
public class CustomEmotionAdapter2 extends BaseEmotionAdapter<CustomEmotionAdapter2.CustomEmotionListAdapter> {

    public interface CustomEmotion {
        void OnAddCustomEmotions();

        void OnClickCustomEmotions(View v, String path);
    }

    private static final int ROW_COUNT = 2;
    private static final int COLUMN_COUNT = 4;
    private static final int P_COUNT = ROW_COUNT * COLUMN_COUNT;

    private CustomEmotionAdapter2.CustomEmotion customEmotionListener;
    private List<String> customImgPathList;

    public CustomEmotionAdapter2(Context context, ViewPager viewPager) {
        super(context, viewPager);
    }

    public CustomEmotionAdapter2(Context context, ViewPager viewPager, List<String> customImgPathList) {
        super(context, viewPager);
        this.customImgPathList = customImgPathList;
    }


    public CustomEmotionAdapter2(Context context, ViewPager viewPager, List<String> customImgPathList, CustomEmotion customEmotionListener) {
        super(context, viewPager);
        this.customEmotionListener = customEmotionListener;
        this.customImgPathList = customImgPathList;
    }

    @Override
    protected void initData() {
        // 暂时用作缺省使用。
        if (customImgPathList == null) {
            String addImageUrl = this.getResourceUriString(mContext, R.mipmap.ic_launcher);
            customImgPathList = new ArrayList<>();
            customImgPathList.add(addImageUrl);
        }
    }

    @Override
    protected int calcItemSize(int viewPageWidth) {
        return viewPageWidth / COLUMN_COUNT;
    }

    @Override
    protected int calcPageNumber() {
        if (customImgPathList == null)
            return 1;

        int count;
        if (customImgPathList.size() % P_COUNT > 0) {
            count = customImgPathList.size() / P_COUNT + 1;
        } else {
            count = customImgPathList.size() / P_COUNT;
        }

        return count;
    }

    @NonNull
    @Override
    public GridView instantiateGridView() {
        GridView gridView = (GridView) LayoutInflater.from(mContext).inflate(R.layout.bx_emotion, null);
        gridView.setScrollContainer(false);
        gridView.setNumColumns(COLUMN_COUNT);
        return gridView;
    }

    public String getResourceUriString(Context context, int resource) {
        return ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + context.getResources().getResourcePackageName(resource) + "/"
                + context.getResources().getResourceTypeName(resource) + "/"
                + context.getResources().getResourceEntryName(resource);
    }

    @NonNull
    @Override
    public CustomEmotionListAdapter createListAdapter() {
        return new CustomEmotionListAdapter();
    }

    @Override
    protected void setGridViewSpacing(GridView gridView, int viewPageHeight, int viewPageWeight) {
        int verticalSpacing = viewPageHeight / ROW_COUNT - mSize;
        if (verticalSpacing < 0)
            verticalSpacing = 0;
        gridView.setVerticalSpacing(verticalSpacing);
        // 这里不设置横向间距，靠RelativeLayout布局
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
        if (path != null && customEmotionListener != null) {
            String pathString = (String) path;
            if (pathString.equals(customImgPathList.get(0))) {
                customEmotionListener.OnAddCustomEmotions();
            } else {
                customEmotionListener.OnClickCustomEmotions(v, pathString);
            }
        }
    }

    public CustomEmotion getCustomEmotionListener() {
        return customEmotionListener;
    }

    public void setCustomEmotionListener(CustomEmotion customEmotionListener) {
        this.customEmotionListener = customEmotionListener;
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
