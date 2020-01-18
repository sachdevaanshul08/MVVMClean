package com.clean.utils.extensions

import android.content.Context

fun Context.dpToPx(dp: Int): Float {
    val density = getResources().getDisplayMetrics().density
    return dp * density
}

fun Context.pxToDp(px: Int): Float {
    val density = getResources().getDisplayMetrics().density
    return px / density
}