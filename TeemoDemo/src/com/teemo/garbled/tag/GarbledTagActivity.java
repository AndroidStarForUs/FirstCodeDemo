/*
*****************************************************************************************
* @file GarbledTagActivity.java
*
* @brief 
*
* Code History:
*       2016-3-4  下午5:34:20  Teemo , initial version
*
* Code Review:
*
********************************************************************************************
*/

package com.teemo.garbled.tag;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;

import com.teemo.demo.R;
import com.teemo.garbled.tag.GarbledTag.OnGarbledItemClickListener;

/**
 * @brief 
 * 
 * @author Teemo
 *
 * @date 2016-3-4 下午5:34:20
 */
public class GarbledTagActivity extends Activity implements OnGarbledItemClickListener{
    private GarbledTag garbledTag = null;
    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbled_tag);
        mContext = this;
        garbledTag = (GarbledTag) this.findViewById(R.id.garbled_tag);
        
        garbledTag.setOnGarbledItemClickListener(this);
        garbledTag.add(getTagData(), true);
        addText();
    }

    private void addText() {
        TextView view = new TextView(this);
        float size = getResources().getDimension(R.dimen.activity_vertical_margin);
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        view.setText("");
    }

    private ArrayList<String> getTagData() {
        ArrayList<String> data = new ArrayList<String>();
//        data.add("文章");
//        data.add("周杰伦");
//        data.add("庄心妍");
//        data.add("周传雄");
//        data.add("两只老虎");
        data.add("上海市");
        data.add("浦东新区");
        data.add("张江镇");
        data.add("玉兰香苑");
        data.add("盛夏路");
        data.add("爱迪生日记");
        data.add("爱迪日记");
        data.add("生日记");
        data.add("爱迪生日记");
        data.add("爱迪生记");
        data.add("爱迪生日记");
        data.add("爱迪记");
        data.add("爱迪记");
        data.add("爱迪生记");
        return data;
    }

    private ArrayList<String> getTagData2() {
        ArrayList<String> data = new ArrayList<String>();
        data.add("XXXXXX");
        data.add("AAAAA");
        data.add("DD");
        data.add("DDDDDDDDD");
        data.add("EFDFE");
        data.add("RRRRRR");
        data.add("RRRRRR");
        data.add("RRRRRR");
        data.add("RRRRRR");
        data.add("RRRRRR");
        return data;
    }

    @Override
    public void onGarbledItemClick(String itemContent) {
        Toast.makeText(mContext, itemContent, Toast.LENGTH_SHORT).show();
    }
}
