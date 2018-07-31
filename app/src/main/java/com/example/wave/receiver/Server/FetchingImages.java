package com.example.wave.receiver.Server;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by yeswanth on 3/28/2018.
 */

public class FetchingImages {

    @SerializedName("Files")
    private ArrayList<FetchImages> fetchImages;

    public ArrayList<FetchImages> getFetchImages() {
        return fetchImages;
    }

    public void setFetchImages(ArrayList<FetchImages> fetchImages) {
        this.fetchImages = fetchImages;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @SerializedName("response")

    private String response;


}
