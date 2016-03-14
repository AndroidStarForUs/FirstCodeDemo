/*
*****************************************************************************************
* @file MusicSelectAnimation.java
*
* @brief 
*
* Code History:
*       2016-2-17  下午3:36:22  Teemo , initial version
*
* Code Review:
*
********************************************************************************************
*/

package com.teemo.music.animation;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

/**
 * @brief The animation like of KuGou music that select music.
 * 
 * @author Teemo
 *
 * @date 2016-2-17 下午3:36:22
 */
public class MusicSelectAnimation {
    public static MusicSelectAnimation sInstance = null;

    public static MusicSelectAnimation getInstance() {
        if (sInstance == null) {
            synchronized (MusicSelectAnimation.class) {
                sInstance = new MusicSelectAnimation();
            }
        }
        return sInstance;
    }

    public MusicSelectAnimation() {
    }

    public void createAnimation(final View itemView, final float yItem, final float screenHeight) {
        TranslateAnimation translateAnimation = new TranslateAnimation(10, 10, 0, (0 - 10));

        translateAnimation.setDuration(200);
        translateAnimation.setAnimationListener(new AnimationListener() {
            
            @Override
            public void onAnimationStart(Animation animation) {
                
            }
            
            @Override
            public void onAnimationRepeat(Animation animation) {
                
            }
            
            @Override
            public void onAnimationEnd(Animation animation) {
                createDownAnimation(itemView, yItem, screenHeight);
            }
        });
        itemView.startAnimation(translateAnimation);
        Log.d("Location", "time>>>> start");
    }

    public void createDownAnimation(final View itemView, float yItem, float screenHeight) {
        float distance = screenHeight - yItem - 50;
        Log.e("Location", "distance = " + distance);
        TranslateAnimation translateAnimation = new TranslateAnimation(10, 10, (0 - 10), distance);

        long duration = (long) (distance / 0.5);
        Log.e("Location", "duration = " + duration);
        if (duration > 1500) {
            duration = 1500;
        } else if (duration < 200) {
            duration = 200;
        }
        Log.e("Location", "duration = " + duration);
        translateAnimation.setDuration((long) duration);
        translateAnimation.setAnimationListener(new AnimationListener() {
            
            @Override
            public void onAnimationStart(Animation animation) {
                
            }
            
            @Override
            public void onAnimationRepeat(Animation animation) {
                
            }
            
            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("Location", "time>>>> end\n\n");
            }
        });
        itemView.startAnimation(translateAnimation);
    }
}
