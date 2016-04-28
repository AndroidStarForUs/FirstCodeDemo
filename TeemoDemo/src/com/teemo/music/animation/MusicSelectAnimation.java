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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.teemo.demo.MyApplication;
import com.teemo.demo.R;
import com.teemo.utils.LogMgr;

/**
 * @brief The animation like of KuGou music that select music.
 * 
 * @author Teemo
 *
 * @date 2016-2-17 下午3:36:22
 */
public class MusicSelectAnimation {
    private final String TAG = MusicSelectAnimation.class.getSimpleName();
    private final int TOTAL_TIMES = 2000; //ms
    private final int MAX_TIMES = 1600;// ms
    private final int MIN_TIME = 700;// ms

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

    public void createObjectAnimation(final View itemView, final int parentHeight, final FrameLayout grandView) {
        /*ImageView img = getAnimatorView();
        FrameLayout itemLayout = (FrameLayout) itemView;
        itemLayout.addView(img);*/
        int[] location = new int[2];
        itemView.getLocationOnScreen(location);
        final ImageView img = getAnimatorView(location);
        grandView.addView(img);
        int duration = (TOTAL_TIMES * (parentHeight-location[1]) / parentHeight);
        if (duration > MAX_TIMES) {
            duration = MAX_TIMES;
        } else if (duration < MIN_TIME) {
            duration = MIN_TIME;
        }
        ObjectAnimator animUp = ObjectAnimator.ofFloat(img, "y", location[1] - itemView.getHeight() / 2, location[1] - itemView.getHeight()).setDuration(400);
        animUp.setInterpolator(new DecelerateInterpolator());
        
        ObjectAnimator animDown = ObjectAnimator.ofFloat(img, "y", location[1] - itemView.getHeight(), (float)parentHeight).setDuration(duration);
        animDown.setInterpolator(new AccelerateInterpolator());
        animDown.addListener(new AnimatorListener() {
            
            @Override
            public void onAnimationStart(Animator arg0) {
                
            }
            
            @Override
            public void onAnimationRepeat(Animator arg0) {
                
            }
            
            @Override
            public void onAnimationEnd(Animator arg0) {
                grandView.removeView(img);
            }
            
            @Override
            public void onAnimationCancel(Animator arg0) {
                
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(animUp, animDown);
        set.start();
    }

    private ImageView getAnimatorView(int location[]) {
        ImageView img = new ImageView(MyApplication.s_Context);
        img.setImageResource(R.drawable.ic_launcher);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(30, 30);
        params.leftMargin = 10;
        params.topMargin = location[1];
        img.setLayoutParams(params);
        return img;
    }
}
