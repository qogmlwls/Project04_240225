package com.example.project04_240225.Utility;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageLoader {
    public static void loadProfileImage(Context context, String imageUrl, ImageView imageView, Drawable placeholder) {
        Glide.with(context)
                .load(imageUrl)
                .placeholder(placeholder) // 로드 중 표시할 이미지
                .error(placeholder) // 로드 실패 시 표시할 이미지
                .into(imageView);
    }
}
