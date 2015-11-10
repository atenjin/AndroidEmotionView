package com.king.chatview.utils;

import android.content.Context;

/**
 * Created by Administrator on 2015/11/10.
 */
public class ContextHolder {
    private static ContextHolder ourInstance = new ContextHolder();

    public static ContextHolder getInstance() {
        return ourInstance;
    }

    Context context;

    private ContextHolder() {}

    public void init(Context context) {
        this.context = context;
    }

    public Context get() {
        return context;
    }
}
