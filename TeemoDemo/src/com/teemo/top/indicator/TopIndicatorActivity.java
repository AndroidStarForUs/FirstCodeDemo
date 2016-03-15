/*
 *****************************************************************************************
 * @file TopIndicatorActivity.java
 *
 * @brief 
 *
 * Code History:
 *       2016-3-8  下午2:54:22  Teemo , initial version
 *
 * Code Review:
 *
 ********************************************************************************************
 */

package com.teemo.top.indicator;

import com.teemo.demo.R;
import com.teemo.top.indicator.TopIndicator.OnTopIndicatorListener;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * @brief
 * 
 * @author Teemo
 * 
 * @date 2016-3-8 下午2:54:22
 */
public class TopIndicatorActivity extends Activity implements OnTopIndicatorListener {
    private Context mContext;
    private TopIndicator mTopIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator);
        initView();
    }

    private void initView() {
        mContext = this;
        mTopIndicator = (TopIndicator) this.findViewById(R.id.top_indicator);
        mTopIndicator.setOnTopIndicatorListener(this);
    }

    @Override
    public void onIndicatorSelected(int index) {
        mTopIndicator.setTabsDisplay(mContext, index);
    }
}
