package com.king.chatview.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by yqd on 14/12/4.
 */
public class OnlyView extends RelativeLayout {
    private View nowView = null;
    public OnlyView(Context context) {
        this(context, null);
    }

    public OnlyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OnlyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    // we will select the first visible view as now target view to show, if none we will choose the first view.
    private void init() {
        int cout = this.getChildCount();
        for (int i = 0; i < cout; i ++){
                View v = this.getChildAt(i);
               if (v.getVisibility() == View.VISIBLE){
                     nowView = v;
                     return;
                   }
           }
        if (null == nowView){
            nowView = this.getChildAt(0);
        }
    }
    public void setChildView(View view){
       if (nowView == view){
           return;
       }
       int cout = this.getChildCount();
       for (int i = 0; i < cout; i ++){
           View v = this.getChildAt(i);
           if (v == view){
               v.setVisibility(View.VISIBLE);
           }else{
               v.setVisibility(View.GONE);
           };
       }
    }
    public View getChildView(){
        return nowView;
    }
}
