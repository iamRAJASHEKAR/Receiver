package com.example.wave.receiver.Activities;

/**
 * Created by yeswanth on 3/14/2018.
 */

/*
public class Myuploads_imagesadapter extends BaseAdapter {
    Activity activity;
    List<FetchImages> images = new ArrayList<FetchImages>();
    ArrayList<Integer> mSelected = new ArrayList<Integer>();


    public Myuploads_imagesadapter(Activity activity, List<FetchImages> imglist) {
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

        final GalleryAlbumViewHolder finalHolder = holder;
        Glide.with(activity)
                .load(images.get(position).getUrl())
                .asBitmap()
                .into(finalHolder.galleryImage);
*/
/*                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        //   imageView.setFileType(ImageSource.bitmap(bitmap));
                        finalHolder.galleryImage.setImageBitmap(bitmap);
                    }
                });*//*




        // Log.e("AdapterPos",""+position);
        Log.e("Pos1", "" + mSelected.size());


        if (mSelected.contains(position)) {

        }
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

        GalleryAlbumViewHolder holder = (GalleryAlbumViewHolder) view.getTag();
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

class GalleryAlbumViewHolder {
    ImageView galleryImage, circleimag;
}*/
