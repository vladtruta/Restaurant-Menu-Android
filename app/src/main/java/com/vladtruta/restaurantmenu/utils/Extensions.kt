package com.vladtruta.restaurantmenu.utils

import android.util.TypedValue
import android.view.View
import com.vladtruta.restaurantmenu.R

fun View.setRipple() = with(TypedValue()) {
    context.theme.resolveAttribute(R.attr.selectableItemBackground, this, true)
    setBackgroundResource(resourceId)
}

fun View.setCircleRipple() = with(TypedValue()) {
    context.theme.resolveAttribute(R.attr.selectableItemBackgroundBorderless, this, true)
    setBackgroundResource(resourceId)
}