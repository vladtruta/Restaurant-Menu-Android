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
import com.vladtruta.restaurantmenu.databinding.DialogChooseQuantityBinding

class ChooseQuantityDialogFragment : DialogFragment() {
    companion object {
        const val TAG = "ChooseQuantityDialogFragment"

        private const val MIN_VALUE = 1
        private const val MAX_VALUE = 5
    }

    private lateinit var binding: DialogChooseQuantityBinding
    private var listener: ChooseQuantityListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? ChooseQuantityListener ?: parentFragment as? ChooseQuantityListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogChooseQuantityBinding.inflate(layoutInflater, null, false)

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.choose_quantity)
            .setPositiveButton(R.string.ok) { _, _ ->
                listener?.onQuantityChosen(binding.quantityNp.value)
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
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    private fun initViews() {
        binding.quantityNp.minValue = MIN_VALUE
        binding.quantityNp.maxValue = MAX_VALUE
        binding.quantityNp.wrapSelectorWheel = true
    }

    interface ChooseQuantityListener {
        fun onQuantityChosen(quantity: Int)
    }
}