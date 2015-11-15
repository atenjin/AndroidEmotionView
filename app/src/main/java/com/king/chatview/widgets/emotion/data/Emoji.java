package com.king.chatview.widgets.emotion.data;

/**
 * Created by Administrator on 2015/11/15.
 */
public class Emoji {
    private int drawableResId;
    private int decInt;

    public Emoji(int drawableResId, int decInt) {
        this.drawableResId = drawableResId;
        this.decInt = decInt;
    }

    public int getDrawableResId() {
        return drawableResId;
    }

    public void setDrawableResId(int drawableResId) {
        this.drawableResId = drawableResId;
    }

    public int getDecInt() {
        return decInt;
    }

    public void setDecInt(int decInt) {
        this.decInt = decInt;
    }
}
