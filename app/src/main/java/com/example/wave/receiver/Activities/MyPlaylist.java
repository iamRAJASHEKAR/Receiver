package com.example.wave.receiver.Activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wave.receiver.R;
import com.example.wave.receiver.Server.FetchImages;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyPlaylist extends Fragment {
    TextView textView1;
    View view;
    RelativeLayout relativeLayout;
    GridView gridViewvideos;
    public static final String EXTRA_VIDEOS = "video";
    ArrayList<FetchImages> videosArrayList = new ArrayList<>();
    Myuploads_videoadapter myuploadsVideoadapter;


    public MyPlaylist() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_playlist, container, false);
        relativeLayout = view.findViewById(R.id.relative_settings);
        if (MyUploadsController.getInstance().uploadvideos != null) {

            videosArrayList = MyUploadsController.getInstance().uploadvideos;
            Log.e("vvsize", " " + videosArrayList.size());
            gridViewvideos = (GridView) view.findViewById(R.id.galleryVideosGridView);
            myuploadsVideoadapter = new Myuploads_videoadapter(getActivity(), videosArrayList);
            gridViewvideos.setAdapter(myuploadsVideoadapter);
        }
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Settings.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Congratulations.MessageEvent event) {
        Log.e("filesevent", "" + event.message);
        String resultData = event.message.trim();
        if (resultData.equals("refreshImages")) {
            Log.e("filesevent1", "call" + event.message);
            final ArrayList<FetchImages> arrayList = MyUploadsController.getInstance().uploadvideos;
            Log.e("sssss", " " + arrayList.size());
            gridViewvideos = (GridView) view.findViewById(R.id.galleryVideosGridView);
            myuploadsVideoadapter = new Myuploads_videoadapter(getActivity(), videosArrayList);
            gridViewvideos.setAdapter(myuploadsVideoadapter);
            myuploadsVideoadapter.notifyDataSetChanged();

        }
    }

    public class Myuploads_videoadapter extends BaseAdapter {
        File file;
        Activity activity;
        List<FetchImages> images;
        ArrayList<Integer> mSelected = new ArrayList<Integer>();


        public Myuploads_videoadapter(Activity activity, ArrayList<FetchImages> imglist) {
            this.activity = activity;
            images = imglist;

        }


        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            AlbumViewHolder holder = null;
            if (convertView == null) {
                holder = new AlbumViewHolder();
                convertView = LayoutInflater.from(activity).inflate(
                        R.layout.videos_list_myuploads, parent, false);

                holder.galleryImage = (ImageView) convertView.findViewById(R.id.videoview_myuploads);
                convertView.setTag(holder);
            } else {
                holder = (AlbumViewHolder) convertView.getTag();
            }

            gridViewvideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int selectedPosition, long l) {
                    myuploadsVideoadapter.onItemSelected(adapterView, view, selectedPosition, l);
                    Intent intent = new Intent(getActivity(), Activity_display_myvideos.class);
                    ImageView imageView = (ImageView) view.findViewById(R.id.grid_item_videos);
                    intent.putExtra("videopath", images.get(selectedPosition).getFileType());
                    intent.putExtra("pos", selectedPosition);
                    startActivity(intent);
                }
            });


        /*Glide.with(activity)
                .load(new File(images.get(position).getThumbnail())) // Uri of the picture
                .into(holder.galleryImage);
            final AlbumViewHolder finalHolder = holder;
            */
            Glide.with(activity)
                    .load(images.get(position).getThumbnail())
                    .thumbnail(0.5f)
                    //.asBitmap()
                    .into(holder.galleryImage);
            Log.e("Pos1", "" + mSelected.size());
            return convertView;
        }


        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            AlbumViewHolder holder = (AlbumViewHolder) view.getTag();
            Integer position = new Integer(pos);
            Log.e("selctedItems", " " + pos);

            if (mSelected.contains(position)) {
                mSelected.remove(position);

                //  holder.circleimag.setVisibility(View.INVISIBLE);
                notifyDataSetChanged();

            } else {
                mSelected.add(position);
                notifyDataSetChanged();
            }

        }
    }

    class AlbumViewHolder {
        ImageView galleryImage, circleimag;
    }

}
