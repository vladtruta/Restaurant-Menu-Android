package com.vladtruta.restaurantmenu.presentation.home

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vladtruta.restaurantmenu.R
import com.vladtruta.restaurantmenu.data.model.local.PaymentSummary
import com.vladtruta.restaurantmenu.databinding.DialogPaymentSummaryBinding
import com.vladtruta.restaurantmenu.presentation.home.adapter.PaymentSummaryAdapter

class PaymentSummaryDialogFragment : DialogFragment() {
    companion object {
        const val TAG = "PaymentSummaryDialogFragment"

        const val ARG_PAYMENT_SUMMARY = "ARG_PAYMENT_SUMMARY"

        @JvmStatic
        fun newInstance(paymentSummary: ArrayList<PaymentSummary>): PaymentSummaryDialogFragment {
            return PaymentSummaryDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_PAYMENT_SUMMARY, paymentSummary)
                }
            }
        }
    }

    private lateinit var binding: DialogPaymentSummaryBinding
    private lateinit var adapter: PaymentSummaryAdapter

    private var listener: PaymentSummaryListener? = null
    private lateinit var paymentSummary: ArrayList<PaymentSummary>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as? PaymentSummaryListener ?: context as? PaymentSummaryListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentSummary = arguments?.getParcelableArrayList(ARG_PAYMENT_SUMMARY) ?: ArrayList()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogPaymentSummaryBinding.inflate(layoutInflater, null, false)

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.order_summary)
            .setPositiveButton(R.string.ok) { _, _ ->
                listener?.onPaymentConfirmed()
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .setView(binding.root)
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObservers()
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    private fun initViews() {
        adapter = PaymentSummaryAdapter()
        binding.summaryRv.adapter = adapter
        adapter.submitList(paymentSummary)
    }

    private fun initObservers() {

    }

    interface PaymentSummaryListener {
        fun onPaymentConfirmed()
    }
}