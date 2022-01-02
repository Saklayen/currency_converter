package com.saklayen.currencyconverter.ui.home

import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.saklayen.currencyconverter.utils.clearDecorations
import com.saklayen.currencyconverter.databinding.CurrencyItemBinding
import com.saklayen.currencyconverter.database.model.Wallet
import com.saklayen.currencyconverter.utils.layoutInflater
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class WalletListAdapter(val viewModel: CurrencyConvertViewModel) :
    ListAdapter<Wallet, WalletViewHolder>(
        DiffCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        return WalletViewHolder(
            CurrencyItemBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }
}


@ExperimentalCoroutinesApi
class WalletViewHolder(val binding: CurrencyItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(viewModel: CurrencyConvertViewModel, data: Wallet) {
        binding.viewModel = viewModel
        binding.item = data
        binding.executePendingBindings()
    }
}

private class DiffCallback : DiffUtil.ItemCallback<Wallet>() {
    override fun areItemsTheSame(oldItem: Wallet, newItem: Wallet) =
        oldItem.rowid == newItem.rowid

    override fun areContentsTheSame(oldItem: Wallet, newItem: Wallet) =
        oldItem == newItem
}

@ExperimentalCoroutinesApi
@BindingAdapter(value = ["currencyConvertViewModel", "walletList"], requireAll = true)
fun RecyclerView.bindWalletListAdapter(viewModel: CurrencyConvertViewModel, data: List<Wallet>?) {
    if (adapter == null) adapter = WalletListAdapter(viewModel)
    val value = data ?: emptyList()
    val walletListAdapter = adapter as? WalletListAdapter
    walletListAdapter?.submitList(value)
    clearDecorations()
    //if (value.isNotEmpty()) addDividerItemDecorator(RecyclerView.VERTICAL)
}
