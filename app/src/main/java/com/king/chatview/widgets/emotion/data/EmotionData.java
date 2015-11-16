package com.king.chatview.widgets.emotion.data;

import java.util.List;

/**
 * Created by Administrator on 2015/11/14.
 */
public class EmotionData<T> {

    public String getStickerIcon() {
        return stickerIcon;
    }

    public enum EmotionCategory {
        emoji, image
    }

    private List<T> emotionList;
    private String stickerIcon;
    private EmotionCategory category;
    private int row;
    private int column;
    private T uniqueItem;

    public EmotionData(List<T> emotionList, String stickerIcon, EmotionCategory category, int row, int column) {
        this.emotionList = emotionList;
        this.stickerIcon = stickerIcon;
        this.category = category;
        this.row = row;
        this.column = column;
    }

    public EmotionData(List<T> emotionList, String stickerIcon, EmotionCategory category, T uniqueItem, int row, int column) {
        this(emotionList, stickerIcon, category, row, column);
        this.uniqueItem = uniqueItem;
    }

    public void setUniqueItem(T uniqueItem) {
        this.uniqueItem = uniqueItem;
//        if (emotionList != null)
//            emotionList.add(0, uniqueItem);
    }

    public T getUniqueItem() {
        return uniqueItem;
    }

    public List<T> getEmotionList() {
        return emotionList;
    }

    public void setEmotionList(List<T> emotionList) {
        this.emotionList = emotionList;
    }

    public EmotionCategory getCategory() {
        return category;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
