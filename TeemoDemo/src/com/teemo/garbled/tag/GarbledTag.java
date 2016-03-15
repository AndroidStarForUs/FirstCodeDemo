/*
*****************************************************************************************
* @file MultiTag.java
*
* @brief 
*
* Code History:
*       2016-3-3  下午4:37:54  Teemo , initial version
*
* Code Review:
*
********************************************************************************************
*/

package com.teemo.garbled.tag;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teemo.demo.R;
import com.teemo.music.animation.Utils;

/**
 * @brief 
 * 
 * @author Teemo
 *
 * @date 2016-3-3 下午4:37:54
 */
public class GarbledTag extends LinearLayout implements Handler.Callback{
    
    private final String TAG = GarbledTag.class.getSimpleName();
    private Context mContext;
    private ArrayList<String> mTagList = null;
    private int marginTop = 0;
    private int itemTextSize = 16;
    private String itemTextColor = "#666666";
    private int itemMarginRight = 5;
    private int itemWidth = 0;

    private Handler mHandler = new Handler(this);
    public final int HANDLER_POST_ITEM_CLICK  = 0;

    private OnGarbledItemClickListener mGarbledItemClickListener;

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
        case HANDLER_POST_ITEM_CLICK:
            //Toast.makeText(mContext, (String)msg.obj, Toast.LENGTH_SHORT).show();
            mGarbledItemClickListener.onGarbledItemClick((String)msg.obj);
            break;
        }
        return false;
    }

    public GarbledTag(Context context) {
        super(context);
        //Log.e(TAG, "MultiTag :: >>> 1");
        init(context);
    }

    public GarbledTag(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Log.e(TAG, "MultiTag :: >>> 2");
        init(context);
        //TypedArray a = context.obtainStyle
    }

    public GarbledTag(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //Log.e(TAG, "MultiTag :: >>> 3");
      
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setOrientation(VERTICAL);
        /*LayoutParams params = (LayoutParams) getLayoutParams();
        int rightMargin = params.rightMargin;
        int leftMargin = params.leftMargin;
        Log.e(TAG, "init :: rightMargin = " + rightMargin + " >>>> leftMargin = " + leftMargin);*/
        itemWidth = Utils.getInstance().getScreenWidthPx(mContext);
        Log.e(TAG, "init :: start init");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e(TAG, "onLayout :: start onLayout");
    }

    /**
     * @brief 此步骤是必须的，在初始化的时候就需要执行.
     *
     * void
     */
    public void setDefaultConfig() {
        setMarginTop(20);
        setItemTextSize(28);
        setItemMarginRight(30);
    }

    /**
     * @brief 设置Item距离右边的margin.
     * 
     * @param marginRight 以px为单位
     */
    public void setItemMarginRight(int marginRight) {
        this.itemMarginRight =  Utils.getInstance().px2dip(mContext, marginRight);
    }

    /**
     * @brief 设置Item中TextSize的字体大小.
     * 
     * @param textSize 以px为单位
     */
    public void setItemTextSize(int textSize) {
        this.itemTextSize =  Utils.getInstance().px2dip(mContext, textSize);
    }

    /**
     * @brief 设置Item中TextVeiw的颜色
     * 
     * @param default color is #666666
     */
    public void setItemTextColor(String textColor) {
        this.itemTextColor =  textColor;
    }
    /**
     * @brief 设置每行距离上面的高度，marginTop
     *
     * @param marginTop marginTop 以px为单位.
     */
    public void setMarginTop(int marginTop) {
        this.marginTop = Utils.getInstance().px2dip(mContext, marginTop);
    }

    /**
     * @brief 更新MultiTag中的数据源
     *
     * @param data
     */
    public void add(ArrayList<String> data) {
        removeAllViews();
        if (data == null) {
            mTagList = new ArrayList<String>();
        } else {
            mTagList = new ArrayList<String>(data);
        }
        for (int i = 0; i < mTagList.size(); i++) {
            addItemTag(mContext, mTagList.get(i));
        }
    }

    private void addItemTag(Context context, String content) {
        Log.e(TAG, "addItemTag :: start addItemTag");
        LinearLayout itemLayout = null;
        TextView itemTxv = getItemTextView(context, content);
        if (getChildCount() == 0) {
            itemLayout = getItemLayout(context, false);
            addView(itemLayout);
            invalidate();
        } else {
            itemLayout = (LinearLayout) getChildAt(getChildCount() -1);
            if (isCreateNewItem(context, itemLayout, itemTxv)) {
                itemLayout = getItemLayout(context, true);
                addView(itemLayout);
            }
        }
        itemLayout.addView(itemTxv);
        invalidate();
    }

    /**
     * @brief 判断是否需要为Parent新建一个LinearLayout
     */
    private boolean isCreateNewItem(Context context, LinearLayout itemLayout, TextView itemTxv) {
        if (itemLayout == null) {
            return true;
        }
        if (itemLayout.getChildCount() == 0) {
            return false;
        }
        int sumWidth = 0;
        for (int i = 0; i < itemLayout.getChildCount(); i++) {
            View view = itemLayout.getChildAt(i);
            int width = getViewWidth(view);
            //Log.e(TAG, "The item content = " + itemTxv.getText() + " >>> and width = " + width);
            /*LayoutParams params = (LayoutParams) view.getLayoutParams();
            int width = params.width;
            Log.e(TAG, "The item content = " + itemTxv.getText() + " >>> and width = " + width);*/
            sumWidth = sumWidth + width + itemMarginRight;
        }
        int itemTxvWidth = getViewWidth(itemTxv);
        LinearLayout.LayoutParams params = (LayoutParams) getLayoutParams();
        int parentMarginRight = params.rightMargin;
        int parentMarginLeft = params.leftMargin;
        //Log.e(TAG, "The parent margin right = " + parentMarginRight);
        //Log.e(TAG, "The item layout sun width = " + sumWidth + " >>> textView width = " + itemTxvWidth + " >>> total width = " + itemWidth);
        if (sumWidth + itemTxvWidth >= (itemWidth - parentMarginRight - parentMarginLeft)) {
            return true;
        }
        return false;
    }

    private LinearLayout getItemLayout(Context context, boolean isNeedMarginTop) {
        LinearLayout itemLayout = new LinearLayout(context);
        itemLayout.setOrientation(HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        if (isNeedMarginTop) {
            params.topMargin = marginTop;
        }
        itemLayout.setLayoutParams(params);
        return itemLayout;
    }

    @SuppressLint("NewApi")
    private TextView getItemTextView(Context context, String content) {
        TextView itemTxv = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.rightMargin = itemMarginRight;
        itemTxv.setLayoutParams(params);

        itemTxv.setGravity(Gravity.CENTER);
        itemTxv.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                mHandler.sendMessage(mHandler.obtainMessage(HANDLER_POST_ITEM_CLICK, ((TextView)v).getText().toString()));
            }
        });
        itemTxv.setText(content);
        itemTxv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, Utils.getInstance().px2dip(mContext, itemTextSize));
        itemTxv.setTextColor(Color.parseColor(itemTextColor));
        itemTxv.setBackground(context.getResources().getDrawable(R.drawable.music_search_record));
        return itemTxv;
    }

    private int getViewWidth(View itemTxv) {
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        itemTxv.measure(w, h);
        int width =itemTxv.getMeasuredWidth();
        return width;
    }

    public interface OnGarbledItemClickListener {
        void onGarbledItemClick(String itemContent);
    }

    public void setOnGarbledItemClickListener(OnGarbledItemClickListener garbledItemClickListener) {
        this.mGarbledItemClickListener = garbledItemClickListener;
    }
}
