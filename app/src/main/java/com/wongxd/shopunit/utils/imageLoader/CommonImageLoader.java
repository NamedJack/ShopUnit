package com.wongxd.shopunit.utils.imageLoader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class CommonImageLoader {


    public void displayImage(Context ctx, String uri, ImageView imageView) {
        Glide.with(ctx)
                .load(uri)
                .into(imageView);
    }


}