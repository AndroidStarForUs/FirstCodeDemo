/*
*****************************************************************************************
* @file ImageData.java
*
* @brief 
*
* Code History:
*       2016-3-1  下午2:26:15  Teemo , initial version
*
* Code Review:
*
********************************************************************************************
*/

package com.teemo.image.loader;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @brief 
 * 
 * @author Teemo
 *
 * @date 2016-3-1 下午2:26:15
 */
public class ImageData implements Parcelable {
    public String mImageUrl;
    public String mImageName;

    public ImageData() {
    }

    private ImageData(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {
        mImageName = in.readString();
        mImageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mImageName);
        dest.writeString(mImageUrl);
    }

    public static final Parcelable.Creator<ImageData> CREATOR = new Creator<ImageData>() {
        
        @Override
        public ImageData[] newArray(int size) {
            return new ImageData[size];
        }
        
        @Override
        public ImageData createFromParcel(Parcel source) {
            return new ImageData(source);
        }
    };
}
