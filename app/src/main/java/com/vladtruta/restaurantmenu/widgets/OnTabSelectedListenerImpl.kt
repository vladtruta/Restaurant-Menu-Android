package com.vladtruta.restaurantmenu.widgets

import com.google.android.material.tabs.TabLayout

abstract class OnTabSelectedListenerImpl : TabLayout.OnTabSelectedListener {
    override fun onTabReselected(tab: TabLayout.Tab?) {}

    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabSelected(tab: TabLayout.Tab?) {}
}