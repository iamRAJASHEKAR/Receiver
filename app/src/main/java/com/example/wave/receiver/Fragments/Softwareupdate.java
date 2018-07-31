package com.example.wave.receiver.Fragments;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.wave.receiver.R;
import com.example.wave.receiver.Server.FetchingImagesServerObject;
import com.example.wave.receiver.Server.ServerAPIS;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Softwareupdate extends Fragment {
    Button button;
    String version;
    ProgressDialog bar;
    public static String linkforupdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_softwareupdate, container, false);
        button = (Button) view.findViewById(R.id.btn_updates);

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                fetchimages(getActivity());
                //  UriUpdate();           //Toasty.error(getActivity(), "OTA not implemented", Toast.LENGTH_LONG, true).show();
            }

        });
        return view;
    }


    public void UriUpdate(String url) {
        //get destination to update file and set Uri
        //TODO: First I wanted to store my update .apk file on internal storage for my app but apparently android does not allow you to open and install
        //aplication with existing package from there. So for me, alternative solution is Download directory in external storage. If there is better
        //solution, please inform us in comment
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String fileName = "Aaro.apk";
        destination += fileName;
        final Uri uri = Uri.parse("file://" + destination);

        //Delete update file if exists
        File file = new File(destination);
        if (file.exists())
            //file.delete() - test this, I think sometimes it doesnt work
            file.delete();

        //get url of app on server
        //  String url = "http://54.234.239.245/image/app.apk";

        //set downloadmanager
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Update Avaialable");
        request.setTitle("Aaro");

        //set destination
        request.setDestinationUri(uri);

        // get download service and enqueue file
        final DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);

        //set BroadcastReceiver to install app when .apk is downloaded
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                install.setDataAndType(uri, manager.getMimeTypeForDownloadedFile(downloadId));
                startActivity(install);

                getActivity().unregisterReceiver(this);

            }
        };
        //register receiver for when .apk download is compete
        getActivity().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void fetchimages(final Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = String.valueOf(pInfo.versionCode);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading ...");
        //       progressDialog.show();

        JSONObject json = new JSONObject();

        try {
            json.put("version", version);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("jhbjdfbjn", "nk" + version);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAPIS.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerAPIS api = retrofit.create(ServerAPIS.class);
        Call<FetchingImagesServerObject> changepsd = api.otafetch(json);
        changepsd.enqueue(new retrofit2.Callback<FetchingImagesServerObject>()
        {
            @Override
            public void onResponse(Call<FetchingImagesServerObject> call, Response<FetchingImagesServerObject> response) {
                Log.e("Feedvideos", new Gson().toJson(response));
                progressDialog.dismiss();
                String status = response.body().getResponse();
                String statuus = response.body().getURL();

                if (status.equals("3"))
                {
                    //String link = "http://54.234.239.245/image/app.apk";
                    String link = response.body().getURL();
                    linkforupdate = link;
                    new Newversion().execute();
                    Log.e("linkforupdate", linkforupdate);
                } else {
                    Toast.makeText(context, "No updates Available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchingImagesServerObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "failed" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    class Newversion extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bar = new ProgressDialog(getActivity());
            bar.setCancelable(false);

            bar.setMessage("Downloading...");

            bar.setIndeterminate(true);
            bar.setCanceledOnTouchOutside(false);
            bar.show();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

            bar.setIndeterminate(false);
            bar.setMax(100);
            bar.setProgress(progress[0]);
            String msg = "";
            if (progress[0] > 99) {

                msg = "Finishing... ";

            } else {

                msg = "Downloading... " + progress[0] + "%";
            }
            bar.setMessage(msg);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            bar.dismiss();

            if (result) {

                Toast.makeText(getActivity(), "Update Done",
                        Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(getActivity(), "Error: Try Again",
                        Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean flag = false;

            try {


                URL url = new URL(linkforupdate);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();


                String PATH = Environment.getExternalStorageDirectory() + "/Download/";
                File file = new File(PATH);
                file.mkdirs();

                File outputFile = new File(file, "app-debug.apk");

                if (outputFile.exists()) {
                    outputFile.delete();
                }

                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();

                int total_size = 1431692;//size of apk

                byte[] buffer = new byte[1024];
                int len1 = 0;
                int per = 0;
                int downloaded = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                    downloaded += len1;
                    per = (int) (downloaded * 100 / total_size);
                    publishProgress(per);
                }
                fos.close();
                is.close();

                OpenNewVersion(PATH);

                flag = true;
            } catch (Exception e) {
                Log.e("updateerror", "Update Error: " + e.getMessage());
                flag = false;
            }
            return flag;

        }

    }

    void OpenNewVersion(String location) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(location + "app-debug.apk")),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}

