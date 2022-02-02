package com.nagwa.files.utils

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide


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
            .centerCrop()
            .override(256)
            .placeholder(buildCircularProgress(activity))
            .into(imageView)
    }

    /**
     * Build Circular Progress to be showing when image is loading
     */
    private fun buildCircularProgress(activity: Context?): CircularProgressDrawable {
        val circularProgressDrawable = CircularProgressDrawable(activity!!)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        return circularProgressDrawable
    }
}