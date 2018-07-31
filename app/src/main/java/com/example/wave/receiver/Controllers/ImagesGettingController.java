package com.example.wave.receiver.Controllers;

//import com.example.wave.receiver.Models.ImageUrls;

/**
 * Created by Mahi on 3/26/2018.
 */

/*
public class ImagesGettingController {

    private static ImagesGettingController obj;
    private ProgressBar mProgressBar;
    private MyAlbums.GridViewAdapter mGridAdapter;
    //public static ImageUrls imagedisplay;
    public ArrayList<ImageUrls> urlList;

    public static ImagesGettingController getInstance() {

        if (obj == null) {
            obj = new ImagesGettingController();
            obj.urlList = new ArrayList<>();

        }
        return obj;
    }


    public void fetchimages(final Context context) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading ...");
        //       progressDialog.show();

        JSONObject json = new JSONObject();

        try {
            json.put("username", "vedas");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAPIS.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerAPIS api = retrofit.create(ServerAPIS.class);
        Call<FetchingImagesServerObject> changepsd = api.userimages(json);
        changepsd.enqueue(new retrofit2.Callback<FetchingImagesServerObject>() {
            @Override
            public void onResponse(Call<FetchingImagesServerObject> call, Response<FetchingImagesServerObject> response) {

                progressDialog.dismiss();
                String status = response.body().getResponse();

                if (status.equals("3")) {
                    ArrayList<ImageUrls> urlList = response.body().getUserurls();
                    Log.e("URlList", "" + urlList.size());
                    if (urlList != null && urlList.size() > 0) {
                        Log.e("URlList1", "" + urlList.size());
                        mGridAdapter.mGridData = urlList;
                        mGridAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(context, "fetching images failed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<FetchingImagesServerObject> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failed", "failed to changepassword");
                Toast.makeText(context, "failed"+t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
*/
