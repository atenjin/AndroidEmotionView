package com.king.chatview.widgets.emotion.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.king.chatview.R;

/**
 * Created by Administrator on 2015/11/12.
 */
public class EmotionAdapter2 extends BaseEmotionAdapter<EmotionAdapter2.EmotionListAdapter> {
    private static final int ROW_COUNT = 3;
    private static final int COLUMN_COUNT = 7;
    private static final int P_COUNT = ROW_COUNT * COLUMN_COUNT - 1;

    private int[][] mDrawablePageId;
    private int[][] decId;

    public int rate = 0;
    private int item_length = 0;

    private EditText editText;

    public EmotionAdapter2(Context context, ViewPager viewPager) {
        this(context, viewPager, null);
    }

    public EmotionAdapter2(Context context, ViewPager viewPager, EditText editText) {
        super(context, viewPager);
        this.editText = editText;
    }

    @Override
    protected void initData() {
        Resources res = mContext.getResources();
        TypedArray icons = res.obtainTypedArray(R.array.emotion_array);

        int[] mDrawableResId = new int[icons.length()];
        for (int i = 0; i < mDrawableResId.length; ++i) {
            mDrawableResId[i] = icons.getResourceId(i, 0);
        }

        if (mDrawableResId.length % P_COUNT > 0)
            this.mCount = mDrawableResId.length / P_COUNT + 1;
        else {
            this.mCount = mDrawableResId.length / P_COUNT;
        }

        int[] intStringArray = mContext.getResources().getIntArray(R.array.emotion_dec_int);

        this.mDrawablePageId = new int[this.mCount][ROW_COUNT * COLUMN_COUNT];
        this.decId = new int[this.mCount][ROW_COUNT * COLUMN_COUNT];
        for (int i = 0; i < this.mCount; i++) {
            for (int j = 0; (j < P_COUNT) && (i * P_COUNT + j < mDrawableResId.length); j++) {
                this.mDrawablePageId[i][j] = mDrawableResId[(i * P_COUNT + j)];
                this.decId[i][j] = intStringArray[(i * P_COUNT + j)];
            }
        }
        mSize = viewPageWidth / COLUMN_COUNT;
        rate = (viewPageWidth / (COLUMN_COUNT * 10 + 3));
        item_length = (this.rate * 9);
    }

    @Override
    public GridView instantiateGridView() {
        GridView gridView = (GridView) LayoutInflater.from(mContext).inflate(R.layout.bx_emotion, null);
        gridView.setScrollContainer(false);
        gridView.setPadding(this.rate * 2, this.rate * 2, this.rate * 2, 0);
        gridView.setNumColumns(COLUMN_COUNT);
        return gridView;
    }

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
    protected GridView setGridViewSpacing(GridView gridView, int viewPageHeight, int viewPageWeight) {
        int verticalSpacing = viewPageHeight / ROW_COUNT - mSize;
        if (verticalSpacing < 0)
            verticalSpacing = 0;
        gridView.setVerticalSpacing(verticalSpacing);
        gridView.setHorizontalSpacing(rate);
        return gridView;
    }

    @Override
    public void onClick(View v) {
        int resId = (Integer) v.getTag(R.id.emoji_tag_id);

        // 暂时这样
        if (editText == null)
            return;
        if (resId == -1) {
            //点击delete,执行删除操作
            KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
            editText.dispatchKeyEvent(event);
        } else if (resId != 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.outHeight = 5;
            options.outWidth = 5;
            Bitmap emojiExpression = BitmapFactory.decodeResource(mContext.getResources(), resId, options);
            String emojiPhonyCode = v.getTag(R.id.emoji_tag_code).toString();
            ImageSpan imageSpan = new ImageSpan(mContext, emojiExpression);
            SpannableString spannableString = new SpannableString(emojiPhonyCode);
            spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            editText.append(spannableString);
        }
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

            EmotionAdapter2 adapter2 = EmotionAdapter2.this;
            //?????
            img.setLayoutParams(new GridView.LayoutParams(item_length, item_length));
            img.setPadding(rate, rate, rate, rate);

            img.setTag(R.id.emoji_tag_id, this.resIds[position]);
            img.setTag(R.id.emoji_tag_code, new String(Character.toChars(intStringArray[position])));

            img.setVisibility(View.INVISIBLE);
            img.setOnClickListener(EmotionAdapter2.this);
            if (this.resIds[position] != 0) {
                img.setImageResource(this.resIds[position]);
                img.setVisibility(View.VISIBLE);
            } else if (position == this.resIds.length - 1) {
                img.setTag(R.id.emoji_tag_id, -1);
                img.setTag(R.id.emoji_tag_code, "delete");
                img.setImageResource(R.drawable.bx_emotion_delete);
                img.setVisibility(View.VISIBLE);
            }
            return img;
        }
    }
}
