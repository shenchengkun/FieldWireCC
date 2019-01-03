package com.example.cheng.fieldwirecc.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.example.cheng.fieldwirecc.Model.Beans.SearchResponseData;
import com.example.cheng.fieldwirecc.R;
import com.example.cheng.fieldwirecc.View.CumsomViews.ZoomImageView;
import com.example.cheng.fieldwirecc.View.ImageLoader.MyImageLoader;

import java.util.ArrayList;

public class ImageZoomActivity extends Activity {
    private int index;
    private ArrayList<SearchResponseData> list;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);

        Intent intent = getIntent();
        if(intent.getExtras() != null){
            index = intent.getIntExtra("index", 0);
            //list = intent.getParcelableArrayListExtra("imageList");
            //list = MainActivity.list;
            list = (ArrayList<SearchResponseData>) intent.getSerializableExtra("list");
        }

        viewPager = findViewById(R.id.imageBrowseViewPager);
        viewPager.setAdapter(new ImageZoomAdapter(this));
        viewPager.setCurrentItem(index);
    }

    class ImageZoomAdapter extends PagerAdapter{
        Context context;

        public ImageZoomAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view==o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            final ZoomImageView image = new ZoomImageView(context);
            //image.enable();
            //image.setScaleType(ImageView.ScaleType.CENTER_INSIDE) ;
            //image.setMaxScale(4.0f);
            /*
            Glide.with(context)
                    .load(list.get(position).getLink())
                    .into(image);
                    */
            MyImageLoader.getInstance().loadImage(list.get(position).getLink(),image,true);
            /*
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //image.disenable();
                    finish();
                }
            });*/
            container.addView(image);
            return image;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
