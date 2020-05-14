package com.vladtruta.restaurantmenu.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vladtruta.restaurantmenu.R
import com.vladtruta.restaurantmenu.data.model.local.Category
import com.vladtruta.restaurantmenu.databinding.ListItemCategoryBinding
import com.vladtruta.restaurantmenu.utils.UIUtils
import com.vladtruta.restaurantmenu.utils.setRipple

class CategoriesAdapter(private val listener: OnCategoryClickListener) :
    ListAdapter<Category, CategoriesAdapter.ViewHolder>(CategoryDiffCallback()) {

    var checkedPosition = 0
        set(value) {
            notifyItemChanged(field)
            field = value
            notifyItemChanged(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ListItemCategoryBinding) :
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

                checkedPosition = position
                listener.onCategoryClicked(getItem(position))
            }
        }

        fun bind(category: Category) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (position == checkedPosition) {
                    binding.root.setBackgroundColor(UIUtils.getColor(R.color.colorPrimaryVariant))
                } else {
                    binding.root.setRipple()
                }
            }
            binding.categoryTv.text = category.name
        }
    }

    interface OnCategoryClickListener {
        fun onCategoryClicked(category: Category)
    }
}

private class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}