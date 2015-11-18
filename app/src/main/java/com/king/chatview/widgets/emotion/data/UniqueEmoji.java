package com.king.chatview.widgets.emotion.data;

import android.net.Uri;

/**
 * Created by Administrator on 2015/11/16.
 */
public class UniqueEmoji implements Emoticon {

    private int resourceId;
    private String path;

    public UniqueEmoji(int resourseId) {
        this.resourceId = resourseId;
    }

    public UniqueEmoji(String path) {
        this.path = path;
    }

    @Override
    public int getResourceId() {
        return resourceId;
    }

    @Override
    public String getImagePath() {
        return path;
    }

    @Override
    public Uri getUri() {
        return null;
    }

    @Override
    public String getDesc() {
        return path;
    }

    @Override
    public EmoticonType getEmoticonType() {
        return EmoticonType.UNIQUE;
    }
}
