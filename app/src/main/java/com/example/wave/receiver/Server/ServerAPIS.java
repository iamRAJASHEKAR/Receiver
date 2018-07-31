package com.example.wave.receiver.Server;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by wave on 3/27/2018.
 */

public interface ServerAPIS
{

    //public static String BASE_URL = " http://192.168.43.207:8080/Aaro/";
    public static String BASE_URL = " http://34.227.104.90/Aaro/";
//117.247.13.135:8096

    @FormUrlEncoded
    @POST("receiverdeviceinsert")
    Call<Pairingserverobjects> sendingdata(@Field("receiverdevicedata") JSONObject userimages);

    @FormUrlEncoded
    @POST("fetchimages")
    Call<FetchingImagesServerObject> userimages(@Field("userimages") JSONObject userimages);

    @FormUrlEncoded
    @POST("fetch")
    Call<FetchingImages> Fetchimages(@Field("fetchdetails") String Fetchimages);

    @FormUrlEncoded
    @POST("otafetch1")
    Call<FetchingImagesServerObject> otafetch(@Field("otadetails") JSONObject userimages);

}
