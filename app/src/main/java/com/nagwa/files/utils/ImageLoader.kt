package com.nagwa.files.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.nagwa.files.R


/**
 * Helper to handle image loading using Glide lib.
 *
 * @Author: Muhammad Noamany
 * @Email: muhammadnoamany@gmail.com
 */
class ImageLoader {
    /**
     * Load image by loading thumbnail url of pdf or video
     */
    fun loadImage(activity: Context?, imageView: ImageView?, url: String?) {
        if (url == null || url.isEmpty() || imageView == null) return
        Glide.with(activity!!).load(url)
            .placeholder(R.drawable.ic_placeholder)
            .into(imageView)
    }
}