/*
*****************************************************************************************
* @file ImageActivity.java
*
* @brief 
*
* Code History:
*       2016-3-1  上午9:41:37  Teemo , initial version
*
* Code Review:
*
********************************************************************************************
*/

package com.teemo.image.loader;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.teemo.demo.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * @brief 
 * 
 * @author Teemo
 *
 * @date 2016-3-1 上午9:41:37
 */
public class ImageActivity extends Activity implements OnClickListener {
    private ImageView mImageView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_loader);
        initView();
    }

    private void initView() {
        mContext = this;
        mImageView = (ImageView) this.findViewById(R.id.image_loader_img);
        mImageView.setOnClickListener(this);
        IImageUtils.getInstance().initImageLoader(mContext);
        String imageUrl = "http://img6.cache.netease.com/photo/0001/2016-03-01/BH2LB43B00AP0001.jpg";
        ImageLoader.getInstance().displayImage(imageUrl, mImageView, IImageUtils.getInstance().getDisplayImageOptions());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.image_loader_img:
            startActivity(new Intent(mContext, ImageListActivity.class));
            break;
        }
    }

    
}
