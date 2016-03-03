/*
*****************************************************************************************
* @file ImageListActivity.java
*
* @brief 
*
* Code History:
*       2016-3-1  上午11:35:34  Teemo , initial version
*
* Code Review:
*
********************************************************************************************
*/

package com.teemo.image.loader;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.teemo.demo.R;
import com.teemo.xlistview.XListView;
import com.teemo.xlistview.XListView.IXListViewListener;

/**
 * @brief 
 * 
 * @author Teemo
 *
 * @date 2016-3-1 上午11:35:34
 */
public class ImageListActivity extends Activity implements IXListViewListener {
    private Context mContext;
    private XListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        initView();
    }

    private void initView() {
        mContext = this;
        IImageUtils.getInstance().initImageLoader(mContext);
        String imageUrl = "http://img6.cache.netease.com/photo/0001/2016-03-01/BH2LB43B00AP0001.jpg";

        ArrayList<ImageData> datas = new ArrayList<ImageData>();
        ImageData data = new ImageData();
        data.mImageName = "first";
        data.mImageUrl = "http://img6.cache.netease.com/photo/0001/2016-03-01/BH2LB43B00AP0001.jpg";
        datas.add(data);
        ImageData data1 = new ImageData();
        data1.mImageName = "two";
        data1.mImageUrl = "http://e.hiphotos.baidu.com/image/pic/item/e7cd7b899e510fb34395d1c3de33c895d0430cd1.jpg";
        datas.add(data1);
        mListView = (XListView) this.findViewById(R.id.image_list);
        
        mListView.setXListViewListener(this);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        ImageListAdapter adapter = new ImageListAdapter(datas);
        mListView.setAdapter(adapter);
    }

    class ImageListAdapter extends BaseAdapter {
        private ArrayList<ImageData> mImageData;

        public ImageListAdapter(ArrayList<ImageData> data) {
            setImageData(data);
        }

        private void setImageData(ArrayList<ImageData> data) {
            if (data == null) {
                mImageData = new ArrayList<ImageData>();
            } else {
                mImageData = new ArrayList<ImageData>(data);
            }
        }

        public void setData(ArrayList<ImageData> data) {
            setImageData(data);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.image_list_item, null);
                holder = new ViewHolder();
                holder.itemImg = (ImageView) convertView.findViewById(R.id.image_list_item_img);
                holder.itemTxv = (TextView) convertView.findViewById(R.id.image_list_item_txv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.itemTxv.setText(mImageData.get(position).mImageName);
            ImageLoader.getInstance().displayImage(mImageData.get(position).mImageUrl, holder.itemImg, IImageUtils.getInstance().getDisplayImageOptions());
            return convertView;
        }
        class ViewHolder {
            ImageView itemImg;
            TextView itemTxv;
        }

        @Override
        public int getCount() {
            return mImageData.size();
        }

        @Override
        public Object getItem(int position) {
            return mImageData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        
    }
}
