package com.vladtruta.restaurantmenu.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.vladtruta.restaurantmenu.R
import com.vladtruta.restaurantmenu.databinding.ActivityHomeBinding
import com.vladtruta.restaurantmenu.databinding.NavigationHeaderBinding
import com.vladtruta.restaurantmenu.presentation.home.adapter.HomeFragmentPagerAdapter
import com.vladtruta.restaurantmenu.presentation.home.adapter.HomeFragmentPagerAdapter.Companion.HOME_TABS
import com.vladtruta.restaurantmenu.presentation.settings.SettingsActivity
import com.vladtruta.restaurantmenu.utils.SessionUtils
import com.vladtruta.restaurantmenu.widgets.OnTabSelectedListenerImpl

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navigationHeaderBinding: NavigationHeaderBinding
    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var homeFragmentPagerAdapter: FragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        navigationHeaderBinding = NavigationHeaderBinding.bind(binding.homeNv.getHeaderView(0))
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
        binding.homeTl.getTabAt(HOME_TABS.MENU.ordinal)?.setIcon(R.drawable.ic_restaurant_menu)
        binding.homeTl.getTabAt(HOME_TABS.CART.ordinal)?.setIcon(R.drawable.ic_shopping_cart)

        navigationHeaderBinding.tableNameTv.text = SessionUtils.getTableName()
    }

    private fun initObservers() {
        viewModel.toolbarText.observe(this, Observer {
            binding.homeMtb.title = it
        })
    }

    private fun initActions() {
        binding.homeNv.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> {
                    false
                }
            }
        }

        binding.homeTl.addOnTabSelectedListener(object : OnTabSelectedListenerImpl() {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                updateTab(tab)
            }
        })

        binding.menuEfab.setOnClickListener {
            binding.homeTl.getTabAt(HOME_TABS.MENU.ordinal)?.select()
        }

        binding.cartEfab.setOnClickListener {
            //binding.homeTl.getTabAt(HOME_TABS.CART.ordinal)?.select()

            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun updateTab(tab: TabLayout.Tab?) {
        when (tab?.position) {
            HOME_TABS.MENU.ordinal -> {
                binding.menuEfab.hide()
                binding.cartEfab.show()
            }
            HOME_TABS.CART.ordinal -> {
                binding.menuEfab.show()
                binding.cartEfab.hide()
            }
        }
    }
}
