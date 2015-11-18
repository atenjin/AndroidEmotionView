package com.king.chatview.widgets.emotion.data;

import android.net.Uri;

/**
 * Created by Administrator on 2015/11/16.
 */
public interface Emoticon {

    int getResourceId();

    String getImagePath();

    Uri getUri();

    String getDesc();

    EmoticonType getEmoticonType();

    enum EmoticonType {
        NORMAL, UNIQUE
    }
}
