package com.king.chatview.widgets.emotion.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.king.chatview.R;
import com.king.chatview.widgets.emotion.EmotionView;
import com.king.chatview.widgets.emotion.data.Emoji;
import com.king.chatview.widgets.emotion.data.EmotionData;

import java.util.List;

/**
 * Created by Administrator on 2015/11/12.
 */
public class EmotionAdapter2 extends BaseEmotionAdapter<EmotionAdapter2.EmotionListAdapter> {
    private int[][] mDrawablePageId;
    private int[][] decId;

    public int rate;
    private int item_length;

    public EmotionAdapter2(Context context, ViewPager viewPager, EmotionData<Emoji> data, EmotionView.EmotionClickListener clickListener) {
        super(context, viewPager, data, clickListener);
        initData(data);
    }

    @Override
    protected int calcPageCount(EmotionData emotionData) {
        // 判断unique item 是否存在 存在就是-1 不存在就不处理
        int count = super.calcPageCount(emotionData);
        if (emotionData.getUniqueItem() != null)
            count = count - 1;
        return count;
    }

    private void initData(EmotionData<Emoji> data) {
        List<Emoji> emojiList = data.getEmotionList();

        this.mDrawablePageId = new int[this.mCount][mRow * mColumn];

        this.decId = new int[this.mCount][mRow * mColumn];
        for (int i = 0; i < this.mCount; i++) {
            for (int j = 0; (j < mPageCount) && (i * mPageCount + j < emojiList.size()); j++) {
                Emoji emoji = emojiList.get(i * mPageCount + j);
                this.mDrawablePageId[i][j] = emoji.getDrawableResId();
                this.decId[i][j] = emoji.getDecInt();
            }
        }
        rate = (viewPageWidth / (mColumn * 10 + 3));
        item_length = (this.rate * 9);
    }

    @NonNull
    @Override
    public GridView instantiateGridView() {
        GridView gridView = (GridView) LayoutInflater.from(mContext).inflate(R.layout.bx_emotion, null);
        gridView.setScrollContainer(false);
        gridView.setPadding(this.rate * 2, this.rate * 2, this.rate * 2, 0);
        return gridView;
    }

    @NonNull
    @Override
    public EmotionListAdapter createListAdapter() {
        return new EmotionListAdapter();
    }

    @Override
    public EmotionListAdapter bingData(EmotionListAdapter listAdapter, int position) {
        listAdapter.setData(this.mDrawablePageId[position], this.decId[position]);
        return listAdapter;
    }

    @Override
    protected void setGridViewSpacing(GridView gridView, int viewPageHeight, int viewPageWeight) {
        super.setGridViewSpacing(gridView, viewPageHeight, viewPageWeight);
        gridView.setHorizontalSpacing(rate);
    }

    @Override
    public void onClick(View v) {
        int resId = (Integer) v.getTag(R.id.emoji_tag_id);
        // 暂时这样
//        if (editText == null)
//            return;
//        if (resId == -1) {
//            //点击delete,执行删除操作
//            KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
//            editText.dispatchKeyEvent(event);
//        } else if (resId != 0) {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.outHeight = 5;
//            options.outWidth = 5;
//            Bitmap emojiExpression = BitmapFactory.decodeResource(mContext.getResources(), resId, options);
//            String emojiPhonyCode = v.getTag(R.id.emoji_tag_code).toString();
//            ImageSpan imageSpan = new ImageSpan(mContext, emojiExpression);
//            SpannableString spannableString = new SpannableString(emojiPhonyCode);
//            spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            editText.append(spannableString);
//        }
    }

    class EmotionListAdapter extends BaseEmotionAdapter.BaseListAdapter {
        int[] resIds;
        int[] intStringArray;

        public void setData(int[] resIds, int[] intStringArray) {
            this.resIds = resIds;
            this.intStringArray = intStringArray;
            notifyDataSetChanged();
        }

        public int getCount() {
            if (this.resIds != null) {
                return this.resIds.length;
            }
            return 0;
        }

        public Object getItem(int position) {
            return this.resIds[position];
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView img = new ImageView(mContext);

            img.setLayoutParams(new GridView.LayoutParams(item_length, item_length));
            img.setPadding(rate, rate, rate, rate);

            img.setTag(R.id.emoji_tag_id, this.resIds[position]);
            img.setTag(R.id.emoji_tag_code, new String(Character.toChars(intStringArray[position])));

            img.setVisibility(View.INVISIBLE);
            img.setOnClickListener(EmotionAdapter2.this);

            if (mEmotionData.getUniqueItem() == null) {
                img.setImageResource(this.resIds[position]);
                img.setVisibility(View.VISIBLE);
            } else {
                if (this.resIds[position] != 0) {
                    img.setImageResource(this.resIds[position]);
                    img.setVisibility(View.VISIBLE);
                } else if (position == this.resIds.length - 1) {
                    img.setTag(R.id.emoji_tag_id, -1);
                    img.setTag(R.id.emoji_tag_code, "delete");
                    img.setImageResource(((Emoji) mEmotionData.getUniqueItem()).getDrawableResId());
                    img.setVisibility(View.VISIBLE);
                }
            }
            return img;
        }
    }
}
