package com.vladtruta.restaurantmenu.widgets

import android.view.View
import android.widget.AdapterView

abstract class OnItemSelectedListenerImpl: AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
}