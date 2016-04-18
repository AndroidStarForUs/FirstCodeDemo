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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
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
public class RVActivity extends Activity implements OnRefreshListener {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager; 
    private MyAdapter mAdapter;
    private ArrayList<String> mData = null;
    private SwipeRefreshLayout mSwipeRefresh;
    private int lastVisibleItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        configSwipeRefreshWidget();
        configRecyclerView();
        mAdapter = new MyAdapter(null); 
        mRecyclerView.setAdapter(mAdapter);
        initData();
    }

    private void configSwipeRefreshWidget() {
        mSwipeRefresh = (SwipeRefreshLayout) this.findViewById(R.id.rv_swipe_refresh);
        mSwipeRefresh.setColorScheme(R.color.color_1, R.color.color_2, R.color.color_3, R.color.color_4);
        mSwipeRefresh.setOnRefreshListener(this);
        //mSwipeRefresh.set
    }

    private void configRecyclerView() {
        mRecyclerView = (RecyclerView) this.findViewById(R.id.rv_recyclerview);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
    
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            
            @Override
            public void onScrolled(int dx, int dy) {
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
            
            @Override
            public void onScrollStateChanged(int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    // to refresh
                    mSwipeRefresh.setRefreshing(true);
                    loadMore();
                }
            }
        });
    }

    private void initData() {
        mData= new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            mData.add("第" + i + "项");
        }
        mAdapter.setData(mData);
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

    @Override
    public void onRefresh() {
        if (mData == null) {
            return;
        }
        mData.clear();
        for (int i = 0; i < 10; i++) {
            mData.add("第" + i + "项");
        }
        mSwipeRefresh.setRefreshing(false);
    }

    private void loadMore() {
        if (mData == null) {
            return;
        }
        for (int i = 0; i < 10; i++) {
            mData.add(i + "LoadMore");
        }
        mAdapter.setData(mData);
    }
}
