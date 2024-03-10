package com.tnt.commonlibrary.wrapper

import android.content.Context
import androidx.core.content.ContextCompat

class ContextWrapper(private val context: Context) {

    fun getString(resId: Int) = context.getString(resId)

    fun getColor(color: Int) = ContextCompat.getColor(context, color)

    fun getDrawable(drawable: Int) = ContextCompat.getDrawable(context, drawable)
}