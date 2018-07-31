package com.example.wave.receiver;

/**
 * Created by wave on 3/27/2018.
 */

/*
public class Displayimage extends AppCompatActivity {

    ImageView imageView;
    //Timer task;
    int count;
    int i = count;
    Timer timer = new Timer();
    // List<Userimages> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        imageView = findViewById(R.id.myZoomageView);

        Toast.makeText(this, "jj" + MyAlbums.urlList.size(), Toast.LENGTH_SHORT).show();

*/
/*
        for (int i = 0; i < MainActivity.images.size(); i++) {
            Toast.makeText(DisplayImage.this, "" + MainActivity.images.get(i).getImagepath(), Toast.LENGTH_SHORT).show();
            break;

        }*//*

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("name");
            Glide.with(getApplicationContext())
                    .load(value) // Uri of the picture
                    .crossFade()
                    .thumbnail(0.5f)
                    .into(imageView);

            //The key argument here must match that used in the other activity
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ma();
    }

    public void ma() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(Displayimage.this, "" + i, Toast.LENGTH_SHORT).show();
                        Glide.with(getApplicationContext())
                                .load(MyAlbums.urlList.get(i).getImagepath()) // Uri of the picture
                                .crossFade()
                                .thumbnail(0.5f)
                                .into(imageView);
                        count = count + 1;
                        i = count;
                        if (count > MyAlbums.urlList.size()) {
                            i = 0;
                            count = 0;
                        }


                        //  Toast.makeText(DisplayImage.this, "" + i, Toast.LENGTH_SHORT).show();


                        // Toast.makeText(DisplayImage.this, "hello akka"+, Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }, 0, 10000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
*/
