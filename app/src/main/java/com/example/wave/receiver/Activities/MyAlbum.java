package com.example.wave.receiver.Activities;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wave.receiver.R;
import com.example.wave.receiver.Server.FetchImages;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAlbum extends Fragment {

    GridView gridView;
    View view;
    Myuploads_imagesadapter myuploadsImagesadapter;
    RelativeLayout relativeLayout;
    public static ProgressDialog progressDialog;

    public MyAlbum() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_albums, container, false);

        relativeLayout = view.findViewById(R.id.relative_settings);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading.....");
        progressDialog.show();


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

        //  fetchimages(emailid);
        if (MyUploadsController.getInstance().uploadImages != null) {
            final ArrayList<FetchImages> arrayList = MyUploadsController.getInstance().uploadImages;
            Log.e("sssssres", " " + arrayList.size());
            gridView = (GridView) view.findViewById(R.id.galleryGridView_myuploads);
            myuploadsImagesadapter = new Myuploads_imagesadapter(getActivity(), arrayList);
            gridView.setAdapter(myuploadsImagesadapter);
            myuploadsImagesadapter.notifyDataSetChanged();
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  String selectedItem = parent.getItemAtPosition(position).toString();
                myuploadsImagesadapter.onItemSelected(parent, view, position, id);

            }
        });

        Log.e("onresumecheck", "called");
        EventBus.getDefault().register(this);
    /*    for (int i = 0; i < MyUploadsController.getInstance().uploadedimages.size(); i++) {
            if (i == 0) {
                if (MyUploadsController.getInstance().uploadedimages.get(i).getFileType().equals("video")) {
                    Toast.makeText(getActivity(), "video", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), "image", Toast.LENGTH_SHORT).show();
                }
            }
        }
*/
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
            final ArrayList<FetchImages> arrayList = MyUploadsController.getInstance().uploadImages;
            Log.e("sssss", " " + arrayList.size());
            gridView = (GridView) view.findViewById(R.id.galleryGridView_myuploads);
            myuploadsImagesadapter = new Myuploads_imagesadapter(getActivity(), arrayList);
            gridView.setAdapter(myuploadsImagesadapter);
            myuploadsImagesadapter.notifyDataSetChanged();
        }

    }

    public class Myuploads_imagesadapter extends BaseAdapter {
        Activity activity;

        List<FetchImages> images = new ArrayList<FetchImages>();
        ArrayList<Integer> mSelected = new ArrayList<Integer>();


        public Myuploads_imagesadapter(Activity activity, List<FetchImages> images) {
            this.activity = activity;
            this.images = images;

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
        public View getView(int position, View convertView, ViewGroup parent) {
            GalleryAlbumViewHolder holder = null;
            if (convertView == null) {
                holder = new GalleryAlbumViewHolder();
                convertView = LayoutInflater.from(activity).inflate(
                        R.layout.gallery_list_myuploads, parent, false);

                holder.galleryImage = (ImageView) convertView.findViewById(R.id.imageview_myuploads);
                convertView.setTag(holder);
            } else {
                holder = (GalleryAlbumViewHolder) convertView.getTag();
            }

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int selectedPosition, long l) {
                   /* for ( i = 0; i < mGridData.size(); i++) {
                        ImageUrls post = mGridData.get(i);*/
                    Intent intent = new Intent(getActivity(), Myphotos.class);
                    ImageView imageView = (ImageView) view.findViewById(R.id.grid_item_image);
                    intent.putExtra("imagepath", images.get(selectedPosition).getFileType());
                    intent.putExtra("pos", "0");
                    startActivity(intent);

                   /* for(int i=0;i<mGridData.size();i++)
                        mGridData.get(i);*/
                }
            });

            final GalleryAlbumViewHolder finalHolder = holder;
            Glide.with(activity)
                    .load(images.get(position).getUrl())
                    .asBitmap()
                    .into(finalHolder.galleryImage);
/*                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        //   imageView.setFileType(ImageSource.bitmap(bitmap));
                        finalHolder.galleryImage.setImageBitmap(bitmap);
                    }
                });*/


            // Log.e("AdapterPos",""+position);
            Log.e("Pos1", "" + mSelected.size());


            if (mSelected.contains(position)) {

            }


            return convertView;
        }


        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            GalleryAlbumViewHolder holder = (GalleryAlbumViewHolder) view.getTag();
            Integer position = new Integer(pos);
            Log.e("selctedItems", " " + pos);

            if (mSelected.contains(position)) {
                mSelected.remove(position);

                notifyDataSetChanged();

            } else {
                mSelected.add(position);
                notifyDataSetChanged();
            }

        }
    }

    class GalleryAlbumViewHolder {
        ImageView galleryImage;
    }

}
