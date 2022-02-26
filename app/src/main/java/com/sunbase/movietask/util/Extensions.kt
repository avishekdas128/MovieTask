package com.sunbase.movietask.util

import android.widget.ImageView
import com.sunbase.movietask.R

fun ImageView.loadImage(url: String) {
    GlideApp.with(this.context)
        .load(url)
        .error(R.drawable.no_image)
        .into(this)
}