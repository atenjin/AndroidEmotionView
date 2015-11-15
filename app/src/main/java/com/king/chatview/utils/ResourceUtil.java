package com.king.chatview.utils;

import android.content.ContentResolver;
import android.content.Context;

/**
 * Created by Administrator on 2015/11/14.
 */
public class ResourceUtil {
    public static String getResourceUriString(Context context, int resource) {
        return ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + context.getResources().getResourcePackageName(resource) + "/"
                + context.getResources().getResourceTypeName(resource) + "/"
                + context.getResources().getResourceEntryName(resource);
    }
}
