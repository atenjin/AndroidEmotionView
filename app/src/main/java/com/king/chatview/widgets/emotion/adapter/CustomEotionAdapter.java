package com.king.chatview.widgets.emotion.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/11.
 */
public class CustomEotionAdapter extends PagerAdapter implements View.OnClickListener {


    public interface AddCustomEmotion {
        void OnAddCustomEmotions();
    }

    private AddCustomEmotion addCustomEmotion;
    private Context mContext;
    private List<String> customList;

    public CustomEotionAdapter(Context context, CustomEotionAdapter.AddCustomEmotion addCustomEmotion) {
        this.mContext = context;
        this.addCustomEmotion = addCustomEmotion;
        initData();
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }

    private void initData() {
        // custom from pref
        customList = new ArrayList<>();
        customList.add();
    }


    private class CustomEmotionListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }

    class ViewHolder {
        GridView gridView;
        CustomEotionAdapter.CustomEmotionListAdapter adapter;
    }
}
