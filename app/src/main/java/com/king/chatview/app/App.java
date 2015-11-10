package com.king.chatview.app;

import android.app.Application;

import com.king.chatview.utils.ContextHolder;

/**
 * Created by Administrator on 2015/11/10.
 */
public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        ContextHolder.getInstance().init(this);
    }
}
