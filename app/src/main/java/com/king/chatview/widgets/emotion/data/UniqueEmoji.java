package com.king.chatview.widgets.emotion.data;

import android.net.Uri;

/**
 * Created by Administrator on 2015/11/16.
 */
public class UniqueEmoji extends Emoticon{

    private int resourseId;
    private String path;
    public UniqueEmoji(int resourseId){
        this.resourseId = resourseId;
    }

    public UniqueEmoji(String path) {
        this.path = path;
    }

    @Override
    public int getResourceId() {
        return resourseId;
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
    public final String getDesc() {
        return Emoticon.UNIQUE_ITEM;
    }
}
