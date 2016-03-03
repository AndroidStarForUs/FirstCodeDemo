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

import com.teemo.demo.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @brief Demo for animation.
 * 
 * @author Teemo
 *
 * @date 2016-2-17 下午4:19:02
 */
public class AnimationDemo extends Activity implements OnItemClickListener {
    private ListView mListView = null;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_demo);
        mContext = this;
        mListView = (ListView) this.findViewById(R.id.animation_demo_listview);
        ArrayList<Object> data = new ArrayList<Object>();
        for (int i = 0; i < 30; i++) {
            data.add(null);
        }
        mListView.setAdapter(new AnimationAdapter(data));
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        //Log.e("Location", "x: = " + location[0]);
        Log.e("Location", "y: = " + location[1]);
        int screenHeight = Utils.getInstance().getScreenHeightPx(mContext);
        Log.e("Location", "screenHeight: = " + screenHeight);
        MusicSelectAnimation.getInstance().createAnimation(view, location[1], screenHeight);
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
                TextView view = new TextView(mContext);
                view.setMinHeight(40);
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                holder.view = view;
                convertView = view;
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
