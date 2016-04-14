/*
*****************************************************************************************
* @file RecyclerViewActivity.java
*
* @brief 
*
* Code History:
*       2016-4-14  下午3:57:13  Teemo , initial version
*
* Code Review:
*
********************************************************************************************
*/

package com.teemo.rv;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teemo.demo.R;

/**
 * @brief 
 * 
 * @author Teemo
 *
 * @date 2016-4-14 下午3:57:13
 */
public class RVActivity extends Activity {
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
       mRecyclerView = (RecyclerView) this.findViewById(R.id.rv_recyclerview);
       mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
       mRecyclerView.setItemAnimator(new DefaultItemAnimator());
       mRecyclerView.setHasFixedSize(true);
       mAdapter = new MyAdapter(null); 
       mRecyclerView.setAdapter(mAdapter);
       initData();
    }

    private void initData() {
        ArrayList<String> data = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            data.add("第" + i + "项");
        }
        mAdapter.setData(data);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<String> mItemData;

        public MyAdapter(ArrayList<String> data) {
            setListData(data);
        }

        private void setListData(ArrayList<String> data) {
            if (data == null) {
                mItemData = new ArrayList<String>();
            } else {
                mItemData = new ArrayList<String>(data);
            }
        }

        public void setData(ArrayList<String> data) {
            setListData(data);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mItemData.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.mImageView.setImageResource(R.drawable.xlistview_arrow);
            holder.mTextView.setText(mItemData.get(position).toString());
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView;

        public MyViewHolder(View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.card_view_item_img);
            mTextView = (TextView) v.findViewById(R.id.card_view_item_name);
        }
        
    }
}
