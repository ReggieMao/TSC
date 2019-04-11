package com.ebei.library.util;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

/**
 * Created by MaoLJ on 2018/7/18.
 * 加载图片工具类
 */

public class ImageLoadUtil {

    /**
     * 加载放在服务器上的图片
     */
    public static void loadServiceImg(ImageView v, String url, int defaultID) {
        RequestOptions options = new RequestOptions().placeholder(defaultID).diskCacheStrategy(DiskCacheStrategy.ALL);
        options.placeholder(defaultID);
        Glide.with(v.getContext()).load(url).apply(options).into(v);
    }

    /**
     * 加载放在服务器上的图片并处理成圆形
     */
    public static void loadServiceRoundImg(ImageView v, String url, int defaultID) {
        RequestOptions options = new RequestOptions().placeholder(defaultID).diskCacheStrategy(DiskCacheStrategy.ALL);
        options.placeholder(defaultID);
        options.transform(new CircleCrop());
        Glide.with(v.getContext()).load(url).apply(options).into(v);
    }

}
