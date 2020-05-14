package com.vladtruta.restaurantmenu.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vladtruta.restaurantmenu.R
import com.vladtruta.restaurantmenu.data.model.local.CartItem
import com.vladtruta.restaurantmenu.databinding.ListItemCartPendingBinding
import com.vladtruta.restaurantmenu.utils.ImageHelper
import com.vladtruta.restaurantmenu.utils.UIUtils

class CartPendingAdapter(private val listener: CartPendingListener) :
    ListAdapter<CartItem, CartPendingAdapter.ViewHolder>(CartPendingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemCartPendingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ListItemCartPendingBinding) :
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

                listener.onCartPendingItemClicked(getItem(position))
            }
        }

        fun bind(cartItem: CartItem) {
            ImageHelper.loadImage(
                itemView.context,
                binding.menuCourseIv,
                cartItem.menuCourse.photoUrl
            )
            binding.nameTv.text = cartItem.menuCourse.name
            binding.portionSizeTv.text = cartItem.menuCourse.portionSize
            binding.priceTv.text =
                UIUtils.getString(R.string.price_dollars, cartItem.menuCourse.price)
            binding.quantityTv.text =
                UIUtils.getString(R.string.quantity_placeholder, cartItem.quantity)
            binding.priceTotalTv.text = UIUtils.getString(
                R.string.price_dollars,
                cartItem.menuCourse.price * cartItem.quantity
            )
        }
    }

    interface CartPendingListener {
        fun onCartPendingItemClicked(cartItem: CartItem)
    }
}

private class CartPendingDiffCallback : DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem == newItem
    }
}