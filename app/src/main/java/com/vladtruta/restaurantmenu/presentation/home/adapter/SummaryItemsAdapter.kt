package com.vladtruta.restaurantmenu.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vladtruta.restaurantmenu.R
import com.vladtruta.restaurantmenu.data.model.local.CartItem
import com.vladtruta.restaurantmenu.databinding.ListItemSummaryItemsBinding
import com.vladtruta.restaurantmenu.utils.UIUtils

class SummaryItemsAdapter :
    ListAdapter<CartItem, SummaryItemsAdapter.ViewHolder>(SummaryItemsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemSummaryItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ListItemSummaryItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItem) {
            binding.quantityTv.text =
                UIUtils.getString(R.string.quantity_placeholder, cartItem.quantity)
            binding.itemNameTv.text = cartItem.menuCourse.name
            binding.priceTv.text = UIUtils.getString(
                R.string.price_dollars,
                cartItem.menuCourse.price * cartItem.quantity
            )
        }
    }
}

private class SummaryItemsDiffCallback : DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem == newItem
    }
}