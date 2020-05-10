package com.vladtruta.restaurantmenu.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vladtruta.restaurantmenu.data.model.local.MenuCourse
import com.vladtruta.restaurantmenu.databinding.ListItemMenuCourseBinding

class MenuCoursesAdapter(private val listener: OnMenuCourseClickListener) :
    ListAdapter<MenuCourse, MenuCoursesAdapter.ViewHolder>(MenuCourseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemMenuCourseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ListItemMenuCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            initActions()
        }

        private fun initActions() {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }

                listener.onMenuCourseClicked(getItem(position))
            }
        }

        fun bind(menuCourse: MenuCourse) {
            binding.menuEntryNameTv.text = menuCourse.name
            binding.menuEntryPortionSizeTv.text = menuCourse.portionSize
            binding.menuEntryPriceTv.text = menuCourse.price.toString()
            binding.menuEntryIv
        }
    }

    interface OnMenuCourseClickListener {
        fun onMenuCourseClicked(menuCourse: MenuCourse)
    }
}

class MenuCourseDiffCallback : DiffUtil.ItemCallback<MenuCourse>() {
    override fun areItemsTheSame(oldItem: MenuCourse, newItem: MenuCourse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MenuCourse, newItem: MenuCourse): Boolean {
        return oldItem == newItem
    }
}