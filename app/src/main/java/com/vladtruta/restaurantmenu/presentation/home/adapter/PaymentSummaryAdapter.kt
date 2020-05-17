package com.vladtruta.restaurantmenu.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vladtruta.restaurantmenu.R
import com.vladtruta.restaurantmenu.data.model.local.PaymentSummary
import com.vladtruta.restaurantmenu.databinding.ListItemPaymentSummaryBinding
import com.vladtruta.restaurantmenu.utils.UIUtils

class PaymentSummaryAdapter :
    ListAdapter<PaymentSummary, PaymentSummaryAdapter.ViewHolder>(PaymentSummaryDiffCallback()) {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemPaymentSummaryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ListItemPaymentSummaryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(paymentSummaryItem: PaymentSummary) {
            binding.customerTv.text = paymentSummaryItem.payingCustomer?.fullName
                ?: UIUtils.getString(R.string.everybody)
            binding.totalTv.text =
                UIUtils.getString(R.string.price_dollars, paymentSummaryItem.totalPrice)

            val adapter = SummaryItemsAdapter()
            binding.orderedItemsRv.adapter = adapter
            adapter.submitList(paymentSummaryItem.cartItems)

            binding.orderedItemsRv.setRecycledViewPool(viewPool)
        }
    }
}

private class PaymentSummaryDiffCallback : DiffUtil.ItemCallback<PaymentSummary>() {
    override fun areItemsTheSame(oldItem: PaymentSummary, newItem: PaymentSummary): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PaymentSummary, newItem: PaymentSummary): Boolean {
        return oldItem == newItem
    }
}