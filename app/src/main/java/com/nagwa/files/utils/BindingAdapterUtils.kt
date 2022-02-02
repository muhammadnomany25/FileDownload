package com.nagwa.files.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter


/**
 * Helper to contain all view binding functions
 *
 * @Author: Muhammad Noamany
 * @Email: muhammadnoamany@gmail.com
 */
object BindingAdapterUtils {

    /**
     * Load image request using binding function
     */
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, imageUrl: String) {
        ImageLoader().loadImage(view.context, view, imageUrl as String?)
    }

}