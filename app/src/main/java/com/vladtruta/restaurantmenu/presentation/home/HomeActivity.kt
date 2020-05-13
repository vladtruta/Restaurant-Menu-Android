package com.vladtruta.restaurantmenu.presentation.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import com.vladtruta.restaurantmenu.R
import com.vladtruta.restaurantmenu.databinding.ActivityHomeBinding
import com.vladtruta.restaurantmenu.presentation.home.adapter.HomeFragmentPagerAdapter

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

    }
}
