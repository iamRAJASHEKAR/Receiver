package com.example.wave.receiver.Server;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by wave on 3/27/2018.
 */

public class Pairingserverobjects {
    @SerializedName("receiverdevicedata")
    private JSONObject receiverdevicedata;

    public JSONObject getReceiverdevicedata() {
        return receiverdevicedata;
    }

    public void setReceiverdevicedata(JSONObject receiverdevicedata) {
        this.receiverdevicedata = receiverdevicedata;
    }

    @SerializedName("response")
    private String response;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @SerializedName("username")

    private String username;
    @SerializedName("message")
    private String message;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
