package com.vladtruta.restaurantmenu.presentation.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.vladtruta.restaurantmenu.R
import com.vladtruta.restaurantmenu.databinding.ActivityHomeBinding
import com.vladtruta.restaurantmenu.presentation.home.adapter.HomeFragmentPagerAdapter
import com.vladtruta.restaurantmenu.utils.UIUtils
import com.vladtruta.restaurantmenu.widgets.OnTabSelectedListenerImpl

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var homeFragmentPagerAdapter: FragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initObservers()
        initActions()

        updateTab(binding.homeTl.getTabAt(binding.homeTl.selectedTabPosition))
    }

    private fun initViews() {
        homeFragmentPagerAdapter = HomeFragmentPagerAdapter(supportFragmentManager)
        binding.homeVp.adapter = homeFragmentPagerAdapter

        binding.homeTl.setupWithViewPager(binding.homeVp)
        binding.homeTl.getTabAt(0)?.setIcon(R.drawable.ic_restaurant_menu)
        binding.homeTl.getTabAt(1)?.setIcon(R.drawable.ic_shopping_cart)
    }

    private fun initObservers() {

    }

    private fun initActions() {
        binding.homeTl.addOnTabSelectedListener(object: OnTabSelectedListenerImpl() {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                updateTab(tab)
            }
        })
    }

    private fun updateTab(tab: TabLayout.Tab?) {
        when (tab?.position) {
            0 -> {
                binding.cartFab.setImageDrawable(UIUtils.getDrawable(R.drawable.ic_shopping_cart))
                binding.cartFab.setOnClickListener { binding.homeTl.getTabAt(1)?.select() }
            }
            1 -> {
                binding.cartFab.setImageDrawable(UIUtils.getDrawable(R.drawable.ic_restaurant_menu))
                binding.cartFab.setOnClickListener { binding.homeTl.getTabAt(0)?.select() }
            }
        }
    }
}
