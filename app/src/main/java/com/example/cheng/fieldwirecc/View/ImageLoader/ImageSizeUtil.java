package com.example.cheng.fieldwirecc.View.ImageLoader;

import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageSizeUtil {

    public static class ImageSize {
        int width;
        int height;
    }

    public static ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();
        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();

        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        int width = imageView.getWidth();
        if(width <= 0) width = layoutParams.width;
        // if(width <= 0) width = imageView.getMaxWidth();
        if(width <= 0) width = displayMetrics.widthPixels;

        int height = imageView.getHeight();
        if(height <= 0) height = layoutParams.height;
        // if(height <= 0) height = imageView.getMaxHeight();
        if(height <= 0) height = displayMetrics.heightPixels;

        imageSize.width = width;
        imageSize.height = height;
        return imageSize;
    }

    public static int caculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int inSampleSize = 1;
        int width = options.outWidth, height=options.outHeight;

        if(width > reqWidth || height > reqHeight) {
            int widthRadio = Math.round(width/reqWidth);
            int heightRadio = Math.round(height/reqHeight);
            inSampleSize = Math.max(widthRadio, heightRadio);
        }

        return inSampleSize;
    }

}
