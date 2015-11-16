package com.king.chatview.widgets.emotion.data;

import android.net.Uri;

/**
 * Created by Administrator on 2015/11/16.
 */
public abstract class Emoticon {
    public final static String UNIQUE_ITEM = "uniqueItem";

    public abstract int getResourceId();
    public abstract String getImagePath();
    public abstract Uri getUri();
    public abstract String getDesc();
}
