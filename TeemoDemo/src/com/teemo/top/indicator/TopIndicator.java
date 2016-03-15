/*
*****************************************************************************************
* @file TopIndicator.java
*
* @brief 
*
* Code History:
*       2016-3-8  上午10:53:22  Teemo , initial version
*
* Code Review:
*
********************************************************************************************
*/

package com.teemo.top.indicator;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.teemo.demo.R;

/**
 * @brief 
 * 
 * @author Teemo
 *
 * @date 2016-3-8 上午10:53:22
 */
public class TopIndicator extends LinearLayout {

    private static final String TAG = "TopIndicator";

    private String mBackgroundColor = "#F7F7F7";
    private String mIndicatorTextSelectColor = "#FF6868";
    private String mIndicatorTextNormalColor = "#999999";
    
    private List<CheckedTextView> mCheckedList = new ArrayList<CheckedTextView>();
    private List<View> mViewList = new ArrayList<View>();

    // The indicator item name.
    private CharSequence[] mLabels = new CharSequence[] {"单曲", "专辑"};
    private int mScreenWidth;
    private int mUnderLineWidth;
    private View mUnderLine;

    private int mUnderLineFromX = 0;

    public TopIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public TopIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopIndicator(Context context) {
        super(context);
        init(context);
    }

    private void init(final Context context) {
        setOrientation(LinearLayout.VERTICAL);
        this.setBackgroundColor(Color.parseColor(mBackgroundColor));

        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        mUnderLineWidth = mScreenWidth / mLabels.length;

        mUnderLine = new View(context);
        mUnderLine.setBackgroundColor(Color.parseColor(mIndicatorTextSelectColor));
        LinearLayout.LayoutParams underLineParams = new LinearLayout.LayoutParams(mUnderLineWidth, 2);

        LinearLayout topLayout = new LinearLayout(context);
        LinearLayout.LayoutParams topLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        topLayout.setOrientation(LinearLayout.HORIZONTAL);

        LayoutInflater inflater = LayoutInflater.from(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;

        int size = mLabels.length;
        for (int i = 0; i < size; i++) {

            final int index = i;

            final View view = inflater.inflate(R.layout.top_indicator_item, null);

            CheckedTextView itemName = (CheckedTextView) view.findViewById(R.id.indicator_item_name);
            /*itemName.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(mDrawableIds[i]), null, null, null);*/
            itemName.setCompoundDrawablePadding(10);
            itemName.setText(mLabels[i]);

            topLayout.addView(view, params);

            itemName.setTag(index);

            mCheckedList.add(itemName);
            mViewList.add(view);

            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (null != mTabListener) {
                        mTabListener.onIndicatorSelected(index);
                    }
                }
            });

            if (i == 0) {
                itemName.setChecked(true);
                itemName.setTextColor(Color.parseColor(mIndicatorTextSelectColor));
            } else {
                itemName.setChecked(false);
                itemName.setTextColor(Color.parseColor(mIndicatorTextNormalColor));
            }

        }
        this.addView(topLayout, topLayoutParams);
        this.addView(mUnderLine, underLineParams);
    }

    /**
     * @brief Set the index is select.
     *
     * @param context
     * @param index The selected index.
     */
    public void setTabsDisplay(Context context, int index) {
        int size = mCheckedList.size();
        for (int i = 0; i < size; i++) {
            CheckedTextView checkedTextView = mCheckedList.get(i);
            if ((Integer) (checkedTextView.getTag()) == index) {
                /*LogUtils.i(TAG, mLabels[index] + " is selected...");*/
                checkedTextView.setChecked(true);
                checkedTextView.setTextColor(Color.parseColor("#ff6868"));
            } else {
                checkedTextView.setChecked(false);
                checkedTextView.setTextColor(Color.parseColor("#999999"));
            }
        }

        doUnderLineAnimation(index);
    }

    /**
     * @brief The animatoin for under line.
     *
     * @param index 
     */
    private void doUnderLineAnimation(int index) {
        TranslateAnimation animation = new TranslateAnimation(mUnderLineFromX, index * mUnderLineWidth, 0, 0);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setFillAfter(true);
        animation.setDuration(150);
        mUnderLine.startAnimation(animation);
        mUnderLineFromX = index * mUnderLineWidth;
    }

    /*private CheckedTextView createCheckedTextView(Context context) {
        CheckedTextView view = new CheckedTextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        view.setPadding(0, 8, 0, 8);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        return view;
    }*/

    private OnTopIndicatorListener mTabListener;

    public interface OnTopIndicatorListener {
        void onIndicatorSelected(int index);
    }

    public void setOnTopIndicatorListener(OnTopIndicatorListener listener) {
        this.mTabListener = listener;
    }



}
