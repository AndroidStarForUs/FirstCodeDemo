/*
*****************************************************************************************
* @file AnimationDemo.java
*
* @brief 
*
* Code History:
*       2016-2-17  下午4:19:02  Teemo , initial version
*
* Code Review:
*
********************************************************************************************
*/

package com.teemo.music.animation;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.teemo.demo.R;

/**
 * @brief Demo for animation.
 * 
 * @author Teemo
 *
 * @date 2016-2-17 下午4:19:02
 */
public class AnimationDemo extends Activity implements OnItemClickListener {
    private ListView mListView = null;
    private Button mAnimationBtn = null;
    private Context mContext;
    private FrameLayout mParentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_demo);
        mContext = this;
        mListView = (ListView) this.findViewById(R.id.animation_demo_listview);
        mAnimationBtn = (Button) this.findViewById(R.id.animation_demo_btn);
        mParentFrame = (FrameLayout) this.findViewById(R.id.music_animator_parent);
        
       ArrayList<Object> data = new ArrayList<Object>();
        for (int i = 0; i < 30; i++) {
            data.add(null);
        }
        mListView.setAdapter(new AnimationAdapter(data));
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        MusicSelectAnimation.getInstance().createObjectAnimation(view, parent.getHeight(), mParentFrame);
    }

    class AnimationAdapter extends BaseAdapter {
        private ArrayList<Object> mData;

        public AnimationAdapter(ArrayList<Object> data) {
            this.mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.layout_music_list_item, null);
                holder.view = (TextView) convertView.findViewById(R.id.music_item_txv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.view.setText("小明>>>>>>");
            return convertView;
        }

        class ViewHolder {
            TextView view;
        }
    }

}
