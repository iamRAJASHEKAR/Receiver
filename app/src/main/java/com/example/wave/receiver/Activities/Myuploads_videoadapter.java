package com.example.wave.receiver.Activities;

/**
 * Created by yeswanth on 3/14/2018.
 */

/*
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

       */
/* Glide.with(activity)
                .load(new File(images.get(position))) // Uri of the picture
                .into(holder.galleryImage);*//*

        final AlbumViewHolder finalHolder = holder;
        Glide.with(activity)
                .load(images.get(position).getUrl())
                .thumbnail(0.5f)
                //.asBitmap()
                .into(finalHolder.galleryImage);

               */
/* .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        //   imageView.setFileType(ImageSource.bitmap(bitmap));
                        bitmap = ThumbnailUtils.createVideoThumbnail(images.get(position).getUrl(), MediaStore.Video.Thumbnails.MINI_KIND);
                        finalHolder.galleryImage.setImageBitmap(bitmap);
                        //  bitmap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);

                    }
                });*//*




        // Log.e("AdapterPos",""+position);
        Log.e("Pos1", "" + mSelected.size());



       */
/* previewimages.clear();
        for(int i= 0;i<images_selected;i++)
        {
            previewimages.add(images.get(i));
            Log.e("prevvv"," "+previewimages+" "+previewimages.size());
        }
        *//*


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
*/
