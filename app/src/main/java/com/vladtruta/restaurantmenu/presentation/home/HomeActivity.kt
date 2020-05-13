package com.vladtruta.restaurantmenu.presentation.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.vladtruta.restaurantmenu.data.model.local.Category
import com.vladtruta.restaurantmenu.data.model.local.MenuCourse
import com.vladtruta.restaurantmenu.databinding.ActivityHomeBinding
import com.vladtruta.restaurantmenu.presentation.home.adapter.CategoriesAdapter
import com.vladtruta.restaurantmenu.presentation.home.adapter.MenuCoursesAdapter

class HomeActivity : AppCompatActivity(), CategoriesAdapter.OnCategoryClickListener,
    MenuCoursesAdapter.OnMenuCourseClickListener {

    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel by viewModels<HomeViewModel>()

    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var menuCoursesAdapter: MenuCoursesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerViews()
        initObservers()
        initActions()
    }

    private fun initObservers() {
        homeViewModel.categories.observe(this, Observer {
            categoriesAdapter.submitList(it)
        })

        homeViewModel.filteredMenuCourses.observe(this, Observer {
            menuCoursesAdapter.submitList(it)
        })
    }

    private fun initRecyclerViews() {
        categoriesAdapter = CategoriesAdapter(this)
        binding.categoriesRv.adapter = categoriesAdapter

        menuCoursesAdapter = MenuCoursesAdapter(this)
        binding.menuCoursesRv.adapter = menuCoursesAdapter
    }

    private fun initActions() {

    }

    override fun onCategoryClicked(category: Category) {
        homeViewModel.getMenuCoursesByCategory(category)
    }

    override fun onMenuCourseClicked(menuCourse: MenuCourse) {
        CourseDetailsDialogFragment.newInstance(menuCourse).show(
            supportFragmentManager,
            CourseDetailsDialogFragment.TAG
        )
    }
}
