package com.example.mymovieapp.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadImage(url: String?) {
    if (url.isNullOrEmpty()) return
    Glide.with(this.context)
        .load(url)
        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop())
        .into(this)
}