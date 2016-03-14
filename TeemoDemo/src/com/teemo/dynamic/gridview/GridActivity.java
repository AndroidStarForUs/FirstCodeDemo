/*
 *****************************************************************************************
 * @file GridAcvitity.java
 *
 * @brief 
 *
 * Code History:
 *       2016-3-2  上午10:54:27  Teemo , initial version
 *
 * Code Review:
 *
 ********************************************************************************************
 */

package com.teemo.dynamic.gridview;

import java.util.ArrayList;
import java.util.Arrays;

import org.askerov.dynamicgrid.DynamicGridView;

import com.teemo.demo.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * @brief
 * 
 * @author Teemo
 * 
 * @date 2016-3-2 上午10:54:27
 */
public class GridActivity extends Activity {
    private static final String TAG = GridActivity.class.getName();

    private DynamicGridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_gridview);

        gridView = (DynamicGridView) findViewById(R.id.dynamic_grid);
        gridView.setAdapter(new CheeseDynamicAdapter(this, new ArrayList<String>(Arrays.asList(Cheeses.sCheeseStrings)), 3));

        gridView.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                Log.d(TAG, "drag started at position " + position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {
                Log.d(TAG, String.format("drag item position changed from %d to %d", oldPosition, newPosition));
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                gridView.startEditMode(position);
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GridActivity.this, parent.getAdapter().getItem(position).toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (gridView.isEditMode()) {
            gridView.stopEditMode();
        } else {
            super.onBackPressed();
        }
    }
}
