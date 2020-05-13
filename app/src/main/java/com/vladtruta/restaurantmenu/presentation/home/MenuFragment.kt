package com.vladtruta.restaurantmenu.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.vladtruta.restaurantmenu.data.model.local.Category
import com.vladtruta.restaurantmenu.data.model.local.MenuCourse
import com.vladtruta.restaurantmenu.databinding.FragmentMenuBinding
import com.vladtruta.restaurantmenu.presentation.home.adapter.CategoriesAdapter
import com.vladtruta.restaurantmenu.presentation.home.adapter.MenuCoursesAdapter

class MenuFragment: Fragment(),
    CategoriesAdapter.OnCategoryClickListener,
    MenuCoursesAdapter.OnMenuCourseClickListener {

    private lateinit var binding: FragmentMenuBinding
    private val viewModel by viewModels<MenuViewModel>()

    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var menuCoursesAdapter: MenuCoursesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObservers()
        initActions()
    }

    private fun initViews() {
        categoriesAdapter = CategoriesAdapter(this)
        binding.categoriesRv.adapter = categoriesAdapter

        menuCoursesAdapter = MenuCoursesAdapter(this)
        binding.menuCoursesRv.adapter = menuCoursesAdapter
    }

    private fun initObservers() {
        viewModel.categories.observe(viewLifecycleOwner, Observer {
            categoriesAdapter.submitList(it)
        })

        viewModel.filteredMenuCourses.observe(viewLifecycleOwner, Observer {
            menuCoursesAdapter.submitList(it)
        })
    }

    private fun initActions() {

    }

    override fun onCategoryClicked(category: Category) {
        viewModel.getMenuCoursesByCategory(category)
    }

    override fun onMenuCourseClicked(menuCourse: MenuCourse) {
        CourseDetailsDialogFragment.newInstance(menuCourse).show(
            childFragmentManager,
            CourseDetailsDialogFragment.TAG
        )
    }
}
