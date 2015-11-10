package com.king.chatview.widgets;

/**
 * Created by yqd on 14/11/20.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.king.chatview.R;

import java.util.ArrayList;
import java.util.List;

public  class CustomIndicator extends LinearLayout {

    private Context mContext;
    private int width;
    private int height;
    private int margin;
    private int normalId, selectedId;
    private int count = 0;
    private int currentCount = 0;
    private List<ImageView> views = new ArrayList<ImageView>();

    public CustomIndicator(Context context) {
        this(context, null);
    }

    public CustomIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initParams();
    }

    public void setDotMargin(int margin){
        this.margin = margin;
    }
    public void setDotWidth(int width){
        this.width = width;
    }
    public void setDotHeight(int height){
        this.height = height;
    }
    public void setDotCount(int count){
        this.count = count;
    }
    public void setNormalId(int id){
        this.normalId = id;
    }
    public void setSelectedId(int id){
        this.selectedId = id;
    }

    public void setCurrentPosition(int pos) {
        if (count == 0){
            return;
        }
        pos = pos % count;
        if(pos < 0) {
            pos += count;
        }
        if(null != views && views.size() > currentCount) {
            views.get(currentCount).setBackgroundResource(normalId);
            currentCount = pos;
            views.get(currentCount).setBackgroundResource(selectedId);
        }
    }

    public void next() {
        setCurrentPosition(currentCount + 1);
    }

    public void previous() {
        setCurrentPosition(currentCount-1);
    }

    private void initParams(){
        margin = 10;
        width = 40;
        height = 40;
        count = 0;
        normalId = R.drawable.icon_indicator_0;
        selectedId = R.drawable.reddot;
    }
    public void show(){
        initViews();
    }
    private void initViews() {
        views.clear();
        this.removeAllViews();
        for(int i = 0; i < count; i++) {
            ImageView view = new ImageView(mContext);
            views.add(view);
            LayoutParams params = new LayoutParams(width == 0 ? LayoutParams.WRAP_CONTENT : width,
                    height == 0 ? LayoutParams.WRAP_CONTENT : height);
            if(i != count-1) {
                params.rightMargin = margin;
            }
            view.setLayoutParams(params);
            view.setBackgroundResource(normalId);
            this.addView(view);
        }
        setCurrentPosition(0);
    }

    public void removeMargins() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)getLayoutParams();
        params.topMargin = 0;
    }
}
