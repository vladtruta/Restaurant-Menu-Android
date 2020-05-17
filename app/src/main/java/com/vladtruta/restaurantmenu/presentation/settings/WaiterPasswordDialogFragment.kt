package com.vladtruta.restaurantmenu.presentation.settings

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vladtruta.restaurantmenu.R
import com.vladtruta.restaurantmenu.databinding.DialogWaiterPasswordBinding
import com.vladtruta.restaurantmenu.utils.SessionUtils
import com.vladtruta.restaurantmenu.utils.UIUtils

class WaiterPasswordDialogFragment: DialogFragment() {
    companion object {
        const val TAG = "WaiterPasswordDialogFragment"
    }

    private lateinit var binding: DialogWaiterPasswordBinding
    private var listener: WaiterPasswordListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as? WaiterPasswordListener ?: context as? WaiterPasswordListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogWaiterPasswordBinding.inflate(layoutInflater, null, false)

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.password_required)
            .setMessage(R.string.call_the_waiter_to_proceed)
            .setPositiveButton(R.string.ok) { _, _ ->
                checkPasswordCorrect()
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

        initActions()
    }

    override fun onStart() {
        super.onStart()
        UIUtils.showSoftKeyboardFor(binding.waiterPasswordEt)
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    private fun initActions() {
        binding.waiterPasswordEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                checkPasswordCorrect()
                UIUtils.hideKeyboardFrom(binding.waiterPasswordEt)
                dismissAllowingStateLoss()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun checkPasswordCorrect() {
        val password = binding.waiterPasswordEt.text.toString()
        if (password == SessionUtils.getWaiterPassword() || password == SessionUtils.getEmergencyPassword()) {
            listener?.onWaiterPasswordCorrect()
        } else {
            Toast.makeText(requireContext(), R.string.waiter_password_incorrect, Toast.LENGTH_SHORT).show()
        }
    }

    interface WaiterPasswordListener{
        fun onWaiterPasswordCorrect()
    }
}