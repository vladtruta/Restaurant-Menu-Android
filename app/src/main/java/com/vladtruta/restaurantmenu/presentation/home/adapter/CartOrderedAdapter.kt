package com.vladtruta.restaurantmenu.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vladtruta.restaurantmenu.R
import com.vladtruta.restaurantmenu.data.model.local.OrderedItem
import com.vladtruta.restaurantmenu.databinding.ListItemCartOrderedBinding
import com.vladtruta.restaurantmenu.utils.ImageHelper
import com.vladtruta.restaurantmenu.utils.UIUtils

class CartOrderedAdapter(private val listener: CartOrderedListener) :
    ListAdapter<OrderedItem, CartOrderedAdapter.ViewHolder>(CardOrderedDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemCartOrderedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ListItemCartOrderedBinding) :
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

                listener.onCartOrderedItemClicked(getItem(position))
            }
        }

        fun bind(orderedItem: OrderedItem) {
            ImageHelper.loadImage(
                itemView.context,
                binding.courseIv,
                orderedItem.cartItem.menuCourse.photoUrl
            )
            binding.nameTv.text = orderedItem.cartItem.menuCourse.name
            binding.quantityTv.text =
                UIUtils.getString(R.string.quantity_placeholder, orderedItem.cartItem.quantity)
            binding.priceTv.text =
                UIUtils.getString(
                    R.string.price_dollars,
                    orderedItem.cartItem.menuCourse.price * orderedItem.cartItem.quantity
                )

            orderedItem.payingCustomer?.let {
                binding.payingCustomerTv.text = it.fullName
                binding.payingCustomerTv.visibility = View.VISIBLE
            } ?: run {
                binding.payingCustomerTv.visibility = View.GONE
            }
        }
    }

    interface CartOrderedListener {
        fun onCartOrderedItemClicked(orderedItem: OrderedItem)
    }
}

private class CardOrderedDiffCallback : DiffUtil.ItemCallback<OrderedItem>() {
    override fun areItemsTheSame(oldItem: OrderedItem, newItem: OrderedItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: OrderedItem, newItem: OrderedItem): Boolean {
        return oldItem == newItem
    }
}