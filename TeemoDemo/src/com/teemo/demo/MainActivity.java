package com.teemo.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.teemo.dynamic.gridview.GridActivity;
import com.teemo.fly.text.SearchFlyActivity;
import com.teemo.image.loader.ImageActivity;
import com.teemo.music.animation.AnimationDemo;

public class MainActivity extends Activity implements OnClickListener{
    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.findViewById(R.id.main_animation).setOnClickListener(this);
        this.findViewById(R.id.main_image_loader).setOnClickListener(this);
        this.findViewById(R.id.main_dynamic_gridview).setOnClickListener(this);
        this.findViewById(R.id.main_fly_text).setOnClickListener(this);

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
        }
    }

}
