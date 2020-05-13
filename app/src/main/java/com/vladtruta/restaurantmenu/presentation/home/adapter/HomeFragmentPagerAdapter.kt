package com.vladtruta.restaurantmenu.presentation.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.vladtruta.restaurantmenu.R
import com.vladtruta.restaurantmenu.presentation.home.CartFragment
import com.vladtruta.restaurantmenu.presentation.home.MenuFragment
import com.vladtruta.restaurantmenu.utils.UIUtils

class HomeFragmentPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    companion object {
        private const val ITEM_COUNT = 2
        private val ITEM_TITLES = arrayOf(
            UIUtils.getString(R.string.menu),
            UIUtils.getString(R.string.cart)
        )
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                MenuFragment()
            }
            1 -> {
                CartFragment()
            }
            else -> {
                MenuFragment()
            }
        }
    }

    override fun getCount(): Int {
        return ITEM_COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ITEM_TITLES[position]
    }
}