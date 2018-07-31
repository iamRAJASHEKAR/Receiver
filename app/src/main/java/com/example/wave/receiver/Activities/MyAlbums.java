package com.example.wave.receiver.Activities;

/*
public class MyAlbums extends AppCompatActivity {
    GridView mGridview;
    Button image;
    Button video;
    private ProgressBar mProgressBar;
    public static GridViewAdapter mGridAdapter;
    String username, imagepath;
    //public static ArrayList<ImageUrls> urlList;


    */
/* Integer[] imageIDs = {
           R.drawable.ic_frame, R.drawable.ic_frame, R.drawable.ic_frame,
           R.drawable.ic_frame, R.drawable.ic_frame, R.drawable.ic_frame,
           R.drawable.ic_frame, R.drawable.ic_frame, R.drawable.ic_frame,
           R.drawable.ic_frame, R.drawable.ic_frame, R.drawable.ic_frame,
           R.drawable.ic_frame, R.drawable.ic_frame, R.drawable.ic_frame,
           R.drawable.ic_frame, R.drawable.ic_frame, R.drawable.ic_frame,
           R.drawable.ic_frame, R.drawable.ic_frame, R.drawable.ic_frame,
           R.drawable.ic_frame, R.drawable.ic_frame, R.drawable.ic_frame,R.drawable.ic_frame,
   };*//*

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_albums);
        //fetchimages();
        mGridview = (GridView) findViewById(R.id.gridview_my_photos);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        image = (Button) findViewById(R.id.images);
        video = (Button) findViewById(R.id.videos);

        fetchimages();

        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, new ArrayList<ImageUrls>());
        mGridview.setAdapter(mGridAdapter);

       */
/* mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
             //   GridItem item = (GridItem) parent.getItemAtPosition(position);

                Intent intent = new Intent(MyAlbums.this,Myphotos.class);
                ImageView imageView = (ImageView) v.findViewById(R.id.grid_item_image);

                // Interesting data to pass across are the thumbnail size/location, the
                // resourceId of the source bitmap, the picture description, and the
                // orientation (to avoid returning back to an obsolete configuration if
                // the device rotates again in the meantime)

                int[] screenLocation = new int[2];
                imageView.getLocationOnScreen(screenLocation);

                //Pass the image title and url to DetailsFragment
                intent.putExtra("left", screenLocation[0]).
                        putExtra("top", screenLocation[1]).
                        putExtra("width", imageView.getWidth()).
                        putExtra("height", imageView.getHeight()).
                        // putExtra("title", item.getTitle()).
                                putExtra("image", imageap.getImagepath());

                //Start details activity
                startActivity(intent);
            }
        });
*//*

      */
/*  mGridview
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                                GridItem item = (GridItem) parent.getItemAtPosition(position);
                                                //Create intent
                                                Intent intent = new Intent(MyAlbums.this, Myphotos.class);
                                                intent.putExtra("image", item.getImageUriString());

                                                //Start details activity
                                                startActivity(intent);
                                            }
                                        });*//*

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGridview.setVisibility(View.VISIBLE);
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGridview.setVisibility(View.INVISIBLE);
            }
        });
        */
/*frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getApplicationContext(),Myphotos.class);
                startActivity(intent);
            }
        });
*//*

    }

    */
/*public void frame(){

        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                Toast.makeText(getBaseContext(), "Mahi " + (position + 1) + " Selected", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),Myphotos.class);
                startActivity(intent);
            }
        });
    }*//*


    */
/*public class ImageAdapterGridView extends BaseAdapter {
        private Context mContext;

        public ImageAdapterGridView(Context c) {
            mContext = c;
        }

        public int getCount() {
            return imageIDs.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView mImageView;

            if (convertView == null) {
                mImageView = new ImageView(mContext);
                mImageView.setLayoutParams(new GridView.LayoutParams(130, 130));
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mImageView.setPadding(16, 16, 16, 16);
            } else {
                mImageView = (ImageView) convertView;
            }
            //mImageView.setImageResource(imageIDs[position]);
            return mImageView;
        }
    }*//*

    private void fetchimages() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        JSONObject json = new JSONObject();

        try {
            json.put("username","vedas");

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

             */
/*   if (status.equals("3")) {
                    urlList = response.body().getUserurls();
                    // ArrayList<ImageUrls> urlList = response.body().getUserurls();
                    Log.e("URlList", "" + urlList.size());
                    if (urlList != null && urlList.size() > 0) {
                        Log.e("URlList1", "" + urlList.size());
                        mGridAdapter.mGridData = urlList;
                        mGridAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "fetching images failed", Toast.LENGTH_SHORT).show();
                }
*//*

            }


            @Override
            public void onFailure(Call<FetchingImagesServerObject> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failed", "failed to changepassword");
                Toast.makeText(MyAlbums.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class GridViewAdapter extends ArrayAdapter<ImageUrls> {

        //private final ColorMatrixColorFilter grayscaleFilter;
        private Context mContext;
        private int layoutResourceId;
        public ArrayList<ImageUrls> mGridData = new ArrayList<ImageUrls>();

        public GridViewAdapter(Context mContext, int layoutResourceId, ArrayList<ImageUrls> mGridData) {
            super(mContext, layoutResourceId, mGridData);
            this.layoutResourceId = layoutResourceId;
            this.mContext = mContext;
            this.mGridData = mGridData;
        }


        */
/**
         * Updates grid data and refresh grid items.
         *
         * @param mGridData
         *//*

        public void setGridData(ArrayList<ImageUrls> mGridData) {
            this.mGridData = mGridData;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            Log.e("URLlistCount", "" + mGridData.size());
            */
/*for (int i = 0; i < mGridData.size(); i++) {
                ImageUrls post = mGridData.get(i);*//*

            //Log.e("poster",""+post);

            return mGridData.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder;

            if (row == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new ViewHolder();
                // holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
                holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }


            // holder.titleTextView.setText(Html.fromHtml(item.getTitle()));

            mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int selectedPosition, long l) {
                   */
/* for ( i = 0; i < mGridData.size(); i++) {
                        ImageUrls post = mGridData.get(i);*//*

                    Intent intent = new Intent(MyAlbums.this, Myphotos.class);
                    ImageView imageView = (ImageView) view.findViewById(R.id.grid_item_image);
                    intent.putExtra("imagepath", mGridData.get(selectedPosition).getImagepath());
                    intent.putExtra("pos", selectedPosition);
                    startActivity(intent);

                   */
/* for(int i=0;i<mGridData.size();i++)
                        mGridData.get(i);*//*

                }
            });
            Glide.with(mContext)
                    .load(mGridData.get(position).getImagepath()) // Uri of the picture
                    .into(holder.imageView);
            //Picasso.with(mContext).load(item.getImageUriString()).into(holder.imageView);
            return row;
        }


        class ViewHolder {
            TextView titleTextView;
            ImageView imageView;
        }
    }

    @Override
    public void onBackPressed() {

    }
}
*/
