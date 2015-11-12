package com.king.chatview.widgets.emotion.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.king.chatview.R;

/**
 * Created by Administrator on 2015/11/10.
 */
public class EmotionAdapter extends PagerAdapter implements View.OnClickListener {
    private static final int ROW_COUNT = 3;
    private static final int COLUMN_COUNT = 7;
    private static final int P_COUNT = ROW_COUNT * COLUMN_COUNT - 1;

    private Context context;

    private int[] mDrawableResId;
    private int[][] mDrawablePageId;
    private int[][] decId;

    private String[] defaultCodeArray;

    private int size = 0;

    private int rate = 0;
    private int item_length = 0;
    private int grid_height = 0;

    private int mCount; // the numbers of view pager

    private ViewHolder mViewHolder;

    private EditText editText;

    private ViewPager emotionViewPager;


    public EmotionAdapter(Context context, ViewPager viewPager) {
        this(context, viewPager, null);
    }

    public EmotionAdapter(Context context, ViewPager viewPager, EditText editText) {
        this.context = context;
        this.editText = editText;
        this.emotionViewPager = viewPager;
        initData();
    }

    private void initData() {
        this.defaultCodeArray = this.context.getResources().getStringArray(R.array.emotion_code);

        Resources res = context.getResources();
        TypedArray icons = res.obtainTypedArray(R.array.emotion_array);

        mDrawableResId = new int[icons.length()];
        for (int i = 0; i < mDrawableResId.length; ++i) {
            mDrawableResId[i] = icons.getResourceId(i, 0);
        }

        if (this.mDrawableResId.length % P_COUNT > 0)
            this.mCount = this.mDrawableResId.length / P_COUNT + 1;
        else {
            this.mCount = this.mDrawableResId.length / P_COUNT;
        }

        int[] intStringArray = context.getResources().getIntArray(R.array.emotion_dec_int);

        this.mDrawablePageId = new int[this.mCount][ROW_COUNT * COLUMN_COUNT];
        this.decId = new int[this.mCount][ROW_COUNT * COLUMN_COUNT];
        for (int i = 0; i < this.mCount; i++) {
            for (int j = 0; (j < P_COUNT) && (i * P_COUNT + j < this.mDrawableResId.length); j++) {
                this.mDrawablePageId[i][j] = this.mDrawableResId[(i * P_COUNT + j)];
                this.decId[i][j] = intStringArray[(i * P_COUNT + j)];
            }
        }

        DisplayMetrics dm = this.context.getResources().getDisplayMetrics();

        this.size = dm.widthPixels / COLUMN_COUNT ;

        this.rate = (dm.widthPixels / (COLUMN_COUNT * 10 + 3));
        this.item_length = (this.rate * 9);
        this.grid_height = (this.item_length * ROW_COUNT + this.rate * 6);
    }

    public int getMaxHeight() {
        return this.grid_height;
    }

    @Override
    public int getCount() {
        return this.mCount;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        ViewHolder holder = (ViewHolder) arg1;
        return arg0 == holder.gridView;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        GridView gridView;
        EmotionListAdapter adapter;
        ViewHolder holder;

        if (this.mViewHolder != null) {
            gridView = this.mViewHolder.gridView;
            adapter = this.mViewHolder.adapter;
            holder = this.mViewHolder;
            this.mViewHolder = null;
        } else {
            gridView = (GridView) LayoutInflater.from(this.context).inflate(R.layout.bx_emotion, null);
            gridView.setScrollContainer(false);
            gridView.setPadding(this.rate * 2, this.rate * 2, this.rate * 2, 0);
            gridView.setNumColumns(COLUMN_COUNT);
            // emotionViewPager在initData阶段是可能没有高度的(gone) 只有在emotionViewPager进行页面填充的时候才一定会有高度
            int viewPageHeight = View.MeasureSpec.getSize(emotionViewPager.getMeasuredHeight());
            int verticalSpacing = viewPageHeight / ROW_COUNT - size;
            if (verticalSpacing < 0)
                verticalSpacing = 0;

            gridView.setVerticalSpacing(verticalSpacing);
            gridView.setHorizontalSpacing(this.rate);
            gridView.setMinimumHeight(viewPageHeight);

            adapter = new EmotionListAdapter();
            holder = new ViewHolder();
            holder.gridView = gridView;
            holder.adapter = adapter;
        }
        adapter.setData(this.mDrawablePageId[position], this.decId[position]);
        gridView.setAdapter(adapter);
        container.addView(gridView);
        return holder;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        this.mViewHolder = ((ViewHolder) object);
        container.removeView(this.mViewHolder.gridView);
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
            Bitmap emojiExpression = BitmapFactory.decodeResource(context.getResources(),
                    resId, options);

            String emojiPhonyCode = v.getTag(R.id.emoji_tag_code).toString();

            ImageSpan imageSpan = new ImageSpan(context, emojiExpression);

            SpannableString spannableString = new SpannableString(emojiPhonyCode);
            spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            editText.append(spannableString);
        }
    }

    class EmotionListAdapter extends BaseAdapter {
        int[] resIds;
        int[] intStringArray;

        public EmotionListAdapter() {
        }

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
            ImageView img = new ImageView(context);

            img.setLayoutParams(new GridView.LayoutParams(EmotionAdapter.this.item_length, EmotionAdapter.this.item_length));
            img.setPadding(EmotionAdapter.this.rate, EmotionAdapter.this.rate, EmotionAdapter.this.rate, EmotionAdapter.this.rate);

            img.setTag(R.id.emoji_tag_id, this.resIds[position]);
            img.setTag(R.id.emoji_tag_code, new String(Character.toChars(intStringArray[position])));

            img.setVisibility(View.INVISIBLE);
            img.setOnClickListener(EmotionAdapter.this);
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

    class ViewHolder {
        GridView gridView;
        EmotionAdapter.EmotionListAdapter adapter;

        ViewHolder() {
        }
    }
}
