package com.king.chatview.widgets.emotion.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.king.chatview.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2015/11/11.
 */
public class CustomEmotionAdapter extends PagerAdapter implements View.OnClickListener {


    public interface CustomEmotion {
        void OnAddCustomEmotions();

        void OnClickCustomEmotions(View v, String path);
    }

    private enum StickCategory {
        customAdd, custom
    }

    private CustomEmotion customEmotion;
    private Context mContext;
    private ViewPager emotionViewPager;
    private List<String> customList;


    private static final int ROW_COUNT = 2;
    private static final int COLUMN_COUNT = 4;
    private static final int P_COUNT = ROW_COUNT * COLUMN_COUNT;

    private int mCount = 1;

    private int size;
    private int rate;

    private ViewHolder mViewHolder;

    public CustomEmotionAdapter(Context context, CustomEmotion customEmotion, ViewPager viewPager) {
        this.mContext = context;
        this.customEmotion = customEmotion;
        this.emotionViewPager = viewPager;
        initData();
    }


    private void initData() {
        // custom from pref
        customList = new ArrayList<>();
//        customList.add();
        String addImageUrl = this.getResourceUriString(mContext, R.mipmap.ic_launcher);
        customList.add(addImageUrl);
        customList.add(addImageUrl);
        customList.add(addImageUrl);
        customList.add(addImageUrl);
        customList.add(addImageUrl);
        customList.add(addImageUrl);
        customList.add(addImageUrl);
        customList.add(addImageUrl);
        customList.add(addImageUrl);
        customList.add(addImageUrl);
        customList.add(addImageUrl);
        customList.add(addImageUrl);
        customList.add(addImageUrl);
        customList.add(addImageUrl);
        customList.add(addImageUrl);
        customList.add(addImageUrl);
        customList.add(addImageUrl);
        customList.add(addImageUrl);
        customList.add(addImageUrl);

        // add pref image path

        // calc page number
        if (customList.size() % P_COUNT > 0) {
            mCount = customList.size() / P_COUNT + 1;
        } else {
            mCount = customList.size() / P_COUNT;
        }


        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        size = dm.widthPixels / COLUMN_COUNT;
        rate = (dm.widthPixels / (COLUMN_COUNT * 10 + 3));
    }


    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        ViewHolder holder = (ViewHolder) object;
        return view == holder.gridView;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        GridView gridView;
        CustomEmotionListAdapter adapter;
        ViewHolder holder;

        if (mViewHolder == null) {
            gridView = (GridView) LayoutInflater.from(mContext).inflate(R.layout.bx_emotion, null);
            gridView.setScrollContainer(false);
//            gridView.setPadding(this.rate * 2, this.rate * 2, this.rate * 2, 0);
            // 横向间距
//            gridView.setHorizontalSpacing(this.rate);

            int viewPageHeight = View.MeasureSpec.getSize(emotionViewPager.getMeasuredHeight());
            int verticalSpacing = viewPageHeight / ROW_COUNT - size;
            if (verticalSpacing < 0)
                verticalSpacing = 0;
            // 纵向间距
            gridView.setVerticalSpacing(verticalSpacing);
            gridView.setMinimumHeight(viewPageHeight);

            // 设置列数
            gridView.setNumColumns(COLUMN_COUNT);
            adapter = new CustomEmotionListAdapter();
            holder = new ViewHolder();
            holder.gridView = gridView;
            holder.adapter = adapter;

        } else {
            gridView = this.mViewHolder.gridView;
            adapter = this.mViewHolder.adapter;
            holder = this.mViewHolder;
            this.mViewHolder = null;
        }

        int startP = position * 8;
        int endP = (position + 1) * 8 > customList.size() ? customList.size() : (position + 1) * 8;
        String[] list = customList.subList(startP, endP).toArray(new String[endP - startP]);
        adapter.setData(list);
        gridView.setAdapter(adapter);
        container.addView(gridView);
        return holder;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        this.mViewHolder = ((ViewHolder) object);
        container.removeView(this.mViewHolder.gridView);
    }

    @Override
    public void onClick(View v) {
        Object path = v.getTag();
        if (path != null && customEmotion != null) {
            String pathString = (String) path;
            if (pathString.equals(customList.get(0))) {
                customEmotion.OnAddCustomEmotions();
            } else {
                customEmotion.OnClickCustomEmotions(v, pathString);
            }
        }
    }

    public void setCustomEmotion(CustomEmotionAdapter.CustomEmotion customEmotion) {
        this.customEmotion = customEmotion;
    }

    public CustomEmotionAdapter.CustomEmotion getCustomEmotion() {
        return customEmotion;
    }


    private String getResourceUriString(Context context, int resource) {
        return ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + context.getResources().getResourcePackageName(resource) + "/"
                + context.getResources().getResourceTypeName(resource) + "/"
                + context.getResources().getResourceEntryName(resource);
    }

    private class CustomEmotionListAdapter extends BaseAdapter {

        private String[] pathList;

        public CustomEmotionListAdapter() {
        }

        public void setData(String[] pathList) {
            this.pathList = pathList;
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
                containerLayout.setLayoutParams(new GridView.LayoutParams(size, size));
                containerLayout.setGravity(Gravity.CENTER);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.setMargins(size / 9, size / 9, size / 9, size / 9);
                img.setLayoutParams(params);
                img.setOnClickListener(CustomEmotionAdapter.this);
                // 加载
                Glide.with(mContext).load(pathList[position]).centerCrop().into(img);
                img.setTag(pathList[position]);
                containerLayout.addView(img);
            }
            return containerLayout;
        }
    }

    class ViewHolder {
        GridView gridView;
        CustomEmotionAdapter.CustomEmotionListAdapter adapter;
    }
}
