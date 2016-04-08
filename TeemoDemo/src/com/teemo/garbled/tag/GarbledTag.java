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
import com.teemo.utils.LogMgr;

/**
 * @brief 
 * 
 * @author Teemo
 *
 * @date 2016-3-3 下午4:37:54
 */
public class GarbledTag extends LinearLayout implements Handler.Callback{
    private final String MOUDLE = GarbledTag.class.getSimpleName();
    private final String TAG = GarbledTag.class.getSimpleName();

    private Context mContext;
    private ArrayList<String> mTagList = null;
    private int marginTop = 0;
    private int itemTextSize = 16;
    private int itemMarginRight = 5;
    private int itemWidth = 0;
    private String itemTextColor = "#000000";

    private final int LIMIT_ROW_NUM = 3;

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
        init(context);
    }

    public GarbledTag(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        //TypedArray a = context.obtainStyle
    }

    public GarbledTag(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
      
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setDefaultConfig();
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
     */
    public void setDefaultConfig() {
        setMarginTop(10);
        setItemTextSize(14);
        setItemTextColor("#666666");
        setItemMarginRight(15);
    }

    /**
     * @brief 设置Item距离右边的margin.
     * 
     * @param marginRight 以px为单位
     */
    public void setItemMarginRight(int marginRight) {
        this.itemMarginRight =  (int) (marginRight * Utils.getInstance().getScreenDensity(mContext));
        //this.itemMarginRight =  Utils.getInstance().px2dip(mContext, marginRight);
    }

    /**
     * @brief 设置Item中TextSize的字体大小.
     * 
     * @param textSize 以1.0屏幕密度下的dp为单位
     */
    public void setItemTextSize(int textSize) {
        float screenDensity = Utils.getInstance().getScreenDensity(mContext);
        LogMgr.e(MOUDLE, TAG, "[setItemTextSize] screenDensity = " + screenDensity);
        this.itemTextSize =  (int) (textSize * screenDensity);
        LogMgr.e(MOUDLE, TAG, "[setItemTextSize] item text size = " + itemTextSize);
    }

    /**
     * @brief 设置Item中TextView的字体颜色.
     * 
     * @param color 字体颜色，default "#000000"
     */
    public void setItemTextColor(String color) {
        this.itemTextColor = color;
    }

    /**
     * @brief 设置每行距离上面的高度，marginTop
     *
     * @param marginTop marginTop 以px为单位.
     */
    public void setMarginTop(int marginTop) {
        this.marginTop =  (int) (marginTop * Utils.getInstance().getScreenDensity(mContext));
        //this.marginTop = Utils.getInstance().px2dip(mContext, marginTop);
    }

    /**
     * @brief 更新MultiTag中的数据源
     *
     * @param data
     * @param isNormalSort true为正常排序，false为倒序.
     */
    public void add(ArrayList<String> data, boolean isNormalSort) {
        removeAllViews();
        if (data == null) {
            mTagList = new ArrayList<String>();
        } else {
            mTagList = new ArrayList<String>(data);
        }
        if (isNormalSort) {
            for (int i = 0; i < mTagList.size(); i++) {
                boolean result = addItemTag(mContext, mTagList.get(i));
                if (!result)
                    break;
            }
        } else {
            for (int i = mTagList.size() - 1; i >= 0; i--) {
                boolean result = addItemTag(mContext, mTagList.get(i));
                if (!result)
                    break;
            } 
        }
    }

    private boolean addItemTag(Context context, String content) {
        Log.e(TAG, "addItemTag :: start addItemTag");
        LinearLayout itemLayout = null;
        boolean isNeedCreateNewItem = false;
        TextView itemTxv = getItemTextView(context, content);
        if (getChildCount() == 0) {
            itemLayout = getItemLayout(context, false);
            addView(itemLayout);
            invalidate();
        } else {
            itemLayout = (LinearLayout) getChildAt(getChildCount() -1);
            isNeedCreateNewItem = isCreateNewItem(context, itemLayout, itemTxv);
            if (isNeedCreateNewItem && getChildCount() < LIMIT_ROW_NUM) {
                isNeedCreateNewItem = false;
                itemLayout = getItemLayout(context, true);
                addView(itemLayout);
            }
        }
        if (getChildCount() < LIMIT_ROW_NUM || (getChildCount() == LIMIT_ROW_NUM && !isNeedCreateNewItem)) {
            itemLayout.addView(itemTxv);
            invalidate();
            return true;
        }
        return false;
    }

    /**
     * @brief 清除Item标签
     *
     * @return
     */
    public boolean clearItemTag() {
        removeAllViews();
        if (mTagList != null) {
            mTagList.clear();
        }
        return true;
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
        int valide = (itemWidth - parentMarginRight - parentMarginLeft) - (sumWidth + itemTxvWidth);
        if (valide >= 0) {
            if (valide <= itemMarginRight) {
                cancelItemMarginRight(itemTxv);
            }
            return false;
        }
        return true;
    }

    private void cancelItemMarginRight(TextView itemTxv) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.rightMargin = 0;
        itemTxv.setLayoutParams(params);
    }

    /**
     * @brief Create item layout for garbled.
     *
     * @param context
     * @param isNeedMarginTop
     * @return 
     */
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

    /**
     * @brief 创建Item中的TextView，用于显示Tag的内容
     *
     * @return
     * TextView The item view of garbled tag.
     */
    @SuppressLint("NewApi")
    private TextView getItemTextView(Context context, final String content) {
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
        itemTxv.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemTextSize);
        itemTxv.setTextColor(Color.parseColor(itemTextColor));
        itemTxv.setBackgroundResource(R.drawable.music_search_record);
        return itemTxv;
    }

    /**
     * @brief 获取控件的宽度
     *
     * @param view 
     * @return The width value of view.
     */
    private int getViewWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int width = view.getMeasuredWidth();
        return width;
    }

    public interface OnGarbledItemClickListener {
        void onGarbledItemClick(String itemContent);
    }

    public void setOnGarbledItemClickListener(OnGarbledItemClickListener garbledItemClickListener) {
        this.mGarbledItemClickListener = garbledItemClickListener;
    }
}
