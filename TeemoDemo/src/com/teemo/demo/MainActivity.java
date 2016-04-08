package com.teemo.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.teemo.dynamic.gridview.GridActivity;
import com.teemo.fly.text.SearchFlyActivity;
import com.teemo.garbled.tag.GarbledTagActivity;
import com.teemo.image.loader.ImageActivity;
import com.teemo.music.animation.AnimationDemo;
import com.teemo.music.animation.Utils;
import com.teemo.top.indicator.TopIndicatorActivity;
import com.teemo.utils.LogMgr;

public class MainActivity extends Activity implements OnClickListener{
    private final String TAG = MainActivity.class.getSimpleName();
    private TextView mTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);// 初始化view
        mTextView = (TextView) this.findViewById(R.id.main_text);
        this.findViewById(R.id.main_animation).setOnClickListener(this);
        this.findViewById(R.id.main_image_loader).setOnClickListener(this);
        this.findViewById(R.id.main_dynamic_gridview).setOnClickListener(this);
        this.findViewById(R.id.main_fly_text).setOnClickListener(this);
        this.findViewById(R.id.main_garbled_tag).setOnClickListener(this);
        this.findViewById(R.id.main_top_indicator).setOnClickListener(this);
        this.findViewById(R.id.main_ap_conn).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.main_animation:
            startActivity(new Intent(this, AnimationDemo.class));
            break;
        case R.id.main_image_loader:
            startActivity(new Intent(this, ImageActivity.class));
            break;
        case R.id.main_dynamic_gridview:
            startActivity(new Intent(this, GridActivity.class));
            break;
        case R.id.main_fly_text:
            startActivity(new Intent(this, SearchFlyActivity.class));
            break;
        case R.id.main_garbled_tag:
            startActivity(new Intent(this, GarbledTagActivity.class));
            break;
        case R.id.main_top_indicator:
            startActivity(new Intent(this, TopIndicatorActivity.class));
            break;
        case R.id.main_ap_conn:
            //startActivity(new Intent(this, ApReceverDemo.class));
            //String time = "*,*,*,10,00,00,*,0*1";
            //String time = "*,*,01,10,00,00,*,0*1 ";
            //String time = "*,*,01|03,10,00,00,*,0*1";
            //String time = "*,*,01-03,10,00,00,*,0*1";
            //String time = "*,01,01,10,00,00,*,0*1";
            //String time = "*,01|03,01,10,00,00,*,0*1";
            //String time = "*,01-03,01,10,00,00,*,0*1";
            //String time = "2016,*,01,10,00,00,*,0*1";
            //String time = "2016|2017,*,01,10,00,00,*,0*1";
            //String time = "2016-2018,*,01,10,00,00,*,0*1";

            //String time = "*,*,*,10|13,00,00,*,0*1";
            //String time = "*,*,*,10-13,00,00,*,0*1";

            //String time = "*,*,*,10,00,00,Mon,0*1";
            //String time = "*,*,*,10,00,00,Mon|Fri,0*1 ";
            //String time = "*,*,*,10,00,00,Mon-Fri,0*1";

            String time = "*,*,*,06,00,00,Mon-Fri,300*3";

            
            String result = Utils.getInstance().formatTimeStr(time);
            LogMgr.d(TAG, TAG, "result = " + result);
            mTextView.setText(result);
            break;
        }
    }

}
