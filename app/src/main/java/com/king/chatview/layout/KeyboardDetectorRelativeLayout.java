package com.king.chatview.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2015/11/10.
 */
public class KeyboardDetectorRelativeLayout extends RelativeLayout {
    private OnSoftKeyboardListener listener;

    public KeyboardDetectorRelativeLayout(Context context) {
        this(context, null);
    }

    public KeyboardDetectorRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardDetectorRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int proposedheight = MeasureSpec.getSize(heightMeasureSpec);
        final int actualHeight = getHeight();

        int diff = actualHeight - proposedheight;
        Log.d("hehe", "diff:"+diff);
        if (diff > 100) {
            if (null != listener) {
                listener.onShown(diff);
            }

        } else if (diff < -100) {
            if (null != listener) {
                listener.onHidden();
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (null != listener) {
            listener.onMeasureFinished();
        }
    }

    public void setOnSoftKeyboardListener(OnSoftKeyboardListener listener) {
        this.listener = listener;
    }

    public interface OnSoftKeyboardListener {
        void onShown(int keyboardHeight);

        void onHidden();

        void onMeasureFinished();
    }
}
