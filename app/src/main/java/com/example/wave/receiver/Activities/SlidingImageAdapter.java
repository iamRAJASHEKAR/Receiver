package com.example.wave.receiver.Activities;

/**
 * Created by  vedas android team
 */
/*
public class SlidingImageAdapter extends PagerAdapter {


    private LayoutInflater inflater;
    private Context context;
    ArrayList<FetchImages> urlList = new ArrayList<>();


    public SlidingImageAdapter(Context context, ArrayList<FetchImages> urlList) {
        this.context = context;
        this.urlList = urlList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return urlList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        FetchImages image = urlList.get(Myphotos.getPos);
        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
        //imageView.setImageResource(Integer.parseInt(image.getImagepath()));
        Glide.with(context).load(image.getFileType()).into(imageView);
        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}*/
