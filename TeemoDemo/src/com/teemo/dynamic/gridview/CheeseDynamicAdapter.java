/*
*****************************************************************************************
* @file CheeseDynamicAdapter.java
*
* @brief 
*
* Code History:
*       2016-3-2  上午11:13:35  Teemo , initial version
*
* Code Review:
*
********************************************************************************************
*/

package com.teemo.dynamic.gridview;

import java.util.List;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import com.teemo.demo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @brief 
 * 
 * @author Teemo
 *
 * @date 2016-3-2 上午11:13:35
 */
public class CheeseDynamicAdapter extends BaseDynamicGridAdapter {
    public CheeseDynamicAdapter(Context context, List<?> items, int columnCount) {
        super(context, items, columnCount);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheeseViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_grid, null);
            holder = new CheeseViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CheeseViewHolder) convertView.getTag();
        }
        holder.build(getItem(position).toString());
        return convertView;
    }

    private class CheeseViewHolder {
        private TextView titleText;
        private ImageView image;

        private CheeseViewHolder(View view) {
            titleText = (TextView) view.findViewById(R.id.item_title);
            image = (ImageView) view.findViewById(R.id.item_img);
        }

        void build(String title) {
            titleText.setText(title);
            image.setImageResource(R.drawable.ic_launcher);
        }
    }
}
