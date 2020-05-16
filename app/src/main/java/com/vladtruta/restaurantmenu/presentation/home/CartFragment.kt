package com.vladtruta.restaurantmenu.presentation.home

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.vladtruta.restaurantmenu.R
import com.vladtruta.restaurantmenu.data.model.local.CartItem
import com.vladtruta.restaurantmenu.data.model.local.OrderedItem
import com.vladtruta.restaurantmenu.databinding.FragmentCartBinding
import com.vladtruta.restaurantmenu.presentation.home.adapter.CartOrderedAdapter
import com.vladtruta.restaurantmenu.presentation.home.adapter.CartPendingAdapter
import com.vladtruta.restaurantmenu.utils.UIUtils

class CartFragment : Fragment(),
    CartPendingAdapter.CartPendingListener,
    ChooseQuantityDialogFragment.ChooseQuantityListener, CartOrderedAdapter.CartOrderedListener {

    private lateinit var binding: FragmentCartBinding
    private val viewModel by viewModels<CartViewModel>()
    private val activityViewModel by activityViewModels<HomeViewModel>()

    private lateinit var cartPendingAdapter: CartPendingAdapter
    private lateinit var cartOrderedAdapter: CartOrderedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.home_items, menu)

        val splitPayItem = menu.findItem(R.id.menu_split_pay)
        val splitPayView = splitPayItem?.actionView as SwitchMaterial

        val customersItem = menu.findItem(R.id.menu_customers)
        val customersView = customersItem?.actionView as Spinner

        val scanQrCodeItem = menu.findItem(R.id.menu_qr_scan)

        customersItem.isEnabled = splitPayView.isChecked && customersView.adapter?.count ?: 0 > 0
        customersItem.isVisible = splitPayView.isChecked && customersView.adapter?.count ?: 0 > 0
        scanQrCodeItem.isEnabled = splitPayView.isChecked
        scanQrCodeItem.isVisible = splitPayView.isChecked

        splitPayView.setOnCheckedChangeListener { _, isChecked ->
            customersItem.isEnabled = isChecked && customersView.adapter?.count ?: 0 > 0
            customersItem.isVisible = isChecked && customersView.adapter?.count ?: 0 > 0
            scanQrCodeItem.isEnabled = isChecked
            scanQrCodeItem.isVisible = isChecked
        }

        viewModel.customers.observe(viewLifecycleOwner, Observer { customers ->
            customers ?: return@Observer

            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                customers.map { it.fullName })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            customersView.adapter = adapter
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_qr_scan -> {
                // Open QR Scan screen
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObservers()
        initActions()
    }

    override fun onResume() {
        super.onResume()

        activityViewModel.toolbarText.value = UIUtils.getString(R.string.cart)
    }

    private fun initViews() {
        cartPendingAdapter = CartPendingAdapter(this)
        binding.pendingRv.adapter = cartPendingAdapter

        cartOrderedAdapter = CartOrderedAdapter(this)
        binding.orderedRv.adapter = cartOrderedAdapter
    }

    private fun initObservers() {
        viewModel.cartItems.observe(viewLifecycleOwner, Observer {
            cartPendingAdapter.submitList(it)
            if (!it.isNullOrEmpty()) {
                binding.pendingEmptyTv.visibility = View.GONE
                binding.pendingRv.visibility = View.VISIBLE
                binding.confirmEfab.show()
            } else {
                binding.pendingEmptyTv.visibility = View.VISIBLE
                binding.pendingRv.visibility = View.GONE
                binding.confirmEfab.hide()
            }
        })

        viewModel.orderedItems.observe(viewLifecycleOwner, Observer {
            cartOrderedAdapter.submitList(it)
            if (!it.isNullOrEmpty()) {
                binding.orderedEmptyTv.visibility = View.GONE
                binding.orderedRv.visibility = View.VISIBLE
                binding.payEfab.show()
            } else {
                binding.orderedEmptyTv.visibility = View.VISIBLE
                binding.orderedRv.visibility = View.GONE
                binding.payEfab.hide()
            }
        })

        viewModel.cartItemsTotalPrice.observe(viewLifecycleOwner, Observer {
            if (it == null || it == 0) {
                binding.pendingTotalTv.visibility = View.GONE
            } else {
                binding.pendingTotalTv.text = UIUtils.getString(R.string.price_dollars_total, it)
                binding.pendingTotalTv.visibility = View.VISIBLE
            }
        })

        viewModel.orderedItemsTotalPrice.observe(viewLifecycleOwner, Observer {
            if (it == null || it == 0) {
                binding.orderedTotalTv.visibility = View.GONE
            } else {
                binding.orderedTotalTv.text = UIUtils.getString(R.string.price_dollars_total, it)
                binding.orderedTotalTv.visibility = View.VISIBLE
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            val errorMessage = UIUtils.getString(R.string.error_message)
            Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT).show()
        })
    }

    private fun initActions() {
        val swipeToDeleteItemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return
                }

                val itemId = cartPendingAdapter.currentList[position].id
                viewModel.deleteItemFromCart(itemId)
            }
        })
        swipeToDeleteItemTouchHelper.attachToRecyclerView(binding.pendingRv)

        binding.confirmEfab.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.are_you_sure)
                .setMessage(R.string.confirm_order_message)
                .setPositiveButton(R.string.yes) { _, _ ->
                    viewModel.insertOrderedItems()
                }
                .setNegativeButton(R.string.no) { _, _ -> }
                .show()
        }

        binding.payEfab.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.confirm_payment_title)
                .setMessage(R.string.confirm_payment_message)
                .setPositiveButton(R.string.ok) { _, _ ->
                    viewModel.payForOrder()
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .show()
        }
    }

    override fun onCartPendingItemClicked(cartItem: CartItem) {
        viewModel.selectedCartItemId = cartItem.id
        ChooseQuantityDialogFragment().show(childFragmentManager, ChooseQuantityDialogFragment.TAG)
    }

    override fun onCartOrderedItemClicked(orderedItem: OrderedItem) {

    }

    override fun onQuantityChosen(quantity: Int) {
        val position = viewModel.selectedCartItemId
        if (position == RecyclerView.NO_POSITION) {
            return
        }

        viewModel.updateQuantityInCart(position, quantity)
    }
}
