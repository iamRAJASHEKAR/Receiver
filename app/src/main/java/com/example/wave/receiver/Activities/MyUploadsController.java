package com.example.wave.receiver.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.wave.receiver.SerialNumber;
import com.example.wave.receiver.Server.FetchImages;
import com.example.wave.receiver.Server.FetchingImages;
import com.example.wave.receiver.Server.ServerAPIS;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.wave.receiver.Activities.MyAlbum.progressDialog;

/**
 * Created by yeswanth on 3/29/2018.
 */

public class MyUploadsController {
    private static MyUploadsController myUploadsController;
    ArrayList<FetchImages> uploadedimages;
    public ArrayList<FetchImages> uploadImages;
    public ArrayList<FetchImages> uploadvideos;

    public static MyUploadsController getInstance() {
        if (myUploadsController == null) {
            myUploadsController = new MyUploadsController();
            myUploadsController.uploadImages = new ArrayList<>();
            myUploadsController.uploadvideos = new ArrayList<>();
            myUploadsController.uploadedimages = new ArrayList<>();
        }

        return myUploadsController;
    }

    public ArrayList<FetchImages> fetchvideos(final Context context1) {
        String user = SerialNumber.message;
        //String email = DatabaseManager.getInstance().currentUser.getEmail();
        JSONObject username = new JSONObject();


        try {
            username.put("UserName", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAPIS.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerAPIS api = retrofit.create(ServerAPIS.class);
        Call<FetchingImages> fetchImages = api.Fetchimages(String.valueOf(username));
        fetchImages.enqueue(new retrofit2.Callback<FetchingImages>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<FetchingImages> call, Response<FetchingImages> response) {
//                progressDialog.dismiss();
                String status = response.body().getResponse();
                Log.e("videostatus", "" + status);
                if (status.equals("3")) {

                    uploadImages.clear();
                    uploadedimages = response.body().getFetchImages();
                    Collections.reverse(uploadedimages);

                    String filetype = uploadedimages.get(0).getTimestamp();
                    Log.e("atzerodude", filetype);
                    String id = uploadedimages.get(0).getIdentity();
                    Log.e("id", " " + id);

                    for (FetchImages objGallaryFile : uploadedimages) {
                        if (objGallaryFile.getFileType().equals("image")) {
                            uploadImages.add(objGallaryFile);
                        }
                    }
                    uploadvideos.clear();
                    ArrayList<FetchImages> uploadedVideos = response.body().getFetchImages();
                    Log.e("respi", "" + response.body().getFetchImages());

                    for (FetchImages objGallaryFile : uploadedVideos) {
                        if (objGallaryFile.getFileType().equals("video")) {
                            uploadvideos.add(objGallaryFile);
                            Log.e("thumbnail", "" + objGallaryFile.getThumbnail());
                            Log.e("urlvideo", "" + objGallaryFile.getUrl());
                        }

                    }


                    EventBus.getDefault().post(new Congratulations.MessageEvent("refreshImages"));
                    Log.e("Feed > ", new Gson().toJson(response));

                    Log.e("myvideossize", String.valueOf(uploadedVideos.size()));

                    // Toast.makeText(context1, uploadedVideos.size(), Toast.LENGTH_SHORT).show();

                } else
                    {
                    EventBus.getDefault().post(new Congratulations.MessageEvent("refreshemptyImages"));
                    //  Toast.makeText(context1, "No files found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchingImages> call, Throwable t) {
                Log.e("failed1", "failed to" + t);
                //   Toast.makeText(context1, "failedimages and video", Toast.LENGTH_SHORT).show();
            }
        });
        return uploadvideos;
    }
}
