/*
*****************************************************************************************
* @file IImageUtils.java
*
* @brief 
*
* Code History:
*       2016-3-1  上午10:36:46  Teemo , initial version
*
* Code Review:
*
********************************************************************************************
*/

package com.teemo.image.loader;

import android.content.Context;
import android.graphics.Bitmap.Config;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;

/**
 * @brief 
 * 
 * @author Teemo
 *
 * @date 2016-3-1 上午10:36:46
 */
public class IImageUtils {

    private static IImageUtils mInstance;

    public static IImageUtils getInstance () {
        if (mInstance == null) {
            synchronized (IImageUtils.class) {
                mInstance = new IImageUtils();
            }
        }
        return mInstance;
    }

    public final boolean initImageLoader(Context context) {
        if (ImageLoader.getInstance().isInited()) {
            return true;
        }
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(configuration);
        return true;
    }

    public final ImageSize getImageSize(int width, int height) {
        ImageSize mImageSize = new ImageSize(width, height);
        return mImageSize;
    }

    public final DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .bitmapConfig(Config.RGB_565)
        .build();
        return options;
    }
}
