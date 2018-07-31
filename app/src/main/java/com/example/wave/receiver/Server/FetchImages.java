package com.example.wave.receiver.Server;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yeswanth on 3/28/2018.
 */

public class FetchImages {


    public Boolean getLoaded()
    {

        return isLoaded;
    }

    public void setLoaded(Boolean loaded) {
        isLoaded = loaded;
    }

    private  Boolean isLoaded;


    @SerializedName("FileType")
    private String fileType;

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @SerializedName("Time")
    private String timestamp;
    @SerializedName("id")
    private String identity;
    @SerializedName("thumbnail")
    private String thumbnail;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @SerializedName("URL")
    private String Url;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }


    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public FetchImages(String image, String id, String url)
    {
        this.fileType = image;
        this.identity = id;
        Url = url;
    }


}
