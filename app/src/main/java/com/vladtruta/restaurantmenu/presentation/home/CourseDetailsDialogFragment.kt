package com.vladtruta.restaurantmenu.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.vladtruta.restaurantmenu.R
import com.vladtruta.restaurantmenu.data.model.local.MenuCourse
import com.vladtruta.restaurantmenu.databinding.DialogCourseDetailsBinding
import com.vladtruta.restaurantmenu.utils.ImageHelper
import com.vladtruta.restaurantmenu.utils.UIUtils

class CourseDetailsDialogFragment : DialogFragment(), ChooseQuantityDialogFragment.ChooseQuantityListener {
    companion object {
        const val TAG = "CourseDetailsDialogFragment"
        private const val ARG_MENU_COURSE = "ARG_MENU_COURSE"

        @JvmStatic
        fun newInstance(menuCourse: MenuCourse): CourseDetailsDialogFragment {
            return CourseDetailsDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_MENU_COURSE, menuCourse)
                }
            }
        }
    }

    private lateinit var binding: DialogCourseDetailsBinding

    private lateinit var menuCourse: MenuCourse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            menuCourse = it.getParcelable(ARG_MENU_COURSE)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCourseDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initActions()
    }

    private fun initViews() {
        ImageHelper.loadImage(this, binding.courseIv, menuCourse.photoUrl)
        binding.courseNameTv.text = menuCourse.name
        binding.coursePriceTv.text = UIUtils.getString(R.string.price_dollars, menuCourse.price)
        binding.coursePortionSizeTv.text = menuCourse.portionSize
        binding.courseDescriptionTv.text = menuCourse.description
    }

    private fun initActions() {
        binding.closeFab.setOnClickListener {
            dismissAllowingStateLoss()
        }

        binding.addToCardEfb.setOnClickListener {
            openQuantityPicker()
        }
    }

    override fun onQuantityChosen(quantity: Int) {
        // Make call
    }

    private fun openQuantityPicker() {
        ChooseQuantityDialogFragment().show(childFragmentManager, ChooseQuantityDialogFragment.TAG)
    }
}