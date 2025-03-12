package com.common.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.zcbl.manager.MyApplication;

/**
 * Created by rendy on 17/1/6.
 * <p>
 * <p>
 * 加载网络图片工具
 */

public class ImageUrlUtils {



    /**
     * @param imageUrl  网址
     * @param img
     * @param defaultId 默认本地图片ID
     */
    public static void setImageUrl(String imageUrl, ImageView img, int defaultId) {

        Glide.with(MyApplication.getApplication()).load(imageUrl)
                .placeholder(defaultId).into(img);

    }

    public static void setLocalImgData(ImageView img, int defImgId) {

        setImageResource(img, defImgId);


    }




    public static void setImageResource(ImageView img, int drawableId) {
        try {
            if (img == null) {
                return;
            }
            if (img.getContext() instanceof Activity) {
                Activity activity = (Activity) img.getContext();
                if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
                    return;
                }
            }
            img.setBackgroundResource(drawableId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void setImageUrlCommmon(String imageUrl, ImageView img, int defaultId) {
        Glide.with(MyApplication.getApplication()).load(imageUrl).placeholder(defaultId).into(img);

    }


    public static void setRoundImageUrl(String imageUrl, ImageView imgView, int defImgId) {
        RoundedCorners roundedCorners = new RoundedCorners((int) AppUtils.dip2px(imgView.getContext(), 10));
        MultiTransformation multiTransformation = new MultiTransformation<>(new CenterCrop(), roundedCorners);
        new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA);
        RequestOptions options = RequestOptions
                .bitmapTransform(multiTransformation);
        Glide.with(MyApplication.application)
                .load(imageUrl)
                .apply(options)
                .placeholder(defImgId)
                .into(imgView);
    }


    public static void setGlideImageUrl(String imageUrl, ImageView img) {
        Glide.with(MyApplication.getApplication()).load(imageUrl).centerCrop().into(img);
    }


    public static void showBase64Img(ImageView imageView, String base64) {
        if (TextUtils.isEmpty(base64) || base64.length() < 10) {
            return;
        }
        try {
            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
