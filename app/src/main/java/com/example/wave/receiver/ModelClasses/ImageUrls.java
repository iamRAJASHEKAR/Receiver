package com.example.wave.receiver.ModelClasses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yeswanth on 3/23/2018.
 */

public class ImageUrls
{
    @SerializedName("imagepath")
    public  String imagepath;

    public  String getImagepath() {
        return imagepath;
    }

    public  void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
}
