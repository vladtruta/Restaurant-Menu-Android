package com.vladtruta.restaurantmenu.utils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.*
import androidx.core.content.ContextCompat


object UIUtils {
    fun getDimension(@DimenRes resId: Int): Int {
        return RestaurantApp.instance.resources.getDimensionPixelSize(resId)
    }

    fun getDimensionFloat(@DimenRes resId: Int): Float {
        return RestaurantApp.instance.resources.getDimension(resId)
    }

    fun getString(@StringRes resId: Int, vararg args: Any?): String {
        return RestaurantApp.instance.resources.getString(resId, *args)
    }

    fun getDrawable(@DrawableRes resId: Int): Drawable? {
        return ContextCompat.getDrawable(RestaurantApp.instance, resId)
    }

    @ColorInt
    fun getColor(@ColorRes resId: Int): Int {
        return ContextCompat.getColor(RestaurantApp.instance, resId)
    }

    fun dpToPx(dp: Float): Int {
        return (dp * RestaurantApp.instance.resources.displayMetrics.density).toInt()
    }

    fun showSoftKeyboardFor(view: View) {
        if (view.requestFocus()) {
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun hideKeyboardFrom(view: View) {
        val inputMethodManager =
            view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

        view.clearFocus()
    }
}