package com.saklayen.currencyconverter.base.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filter
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.saklayen.currencyconverter.utils.EMPTY
import com.saklayen.currencyconverter.R
import com.saklayen.currencyconverter.database.model.Wallet
import com.saklayen.currencyconverter.utils.cleanAndAdd

class DropdownItemArrayAdapter<T>(
    context: Context,
    resource: Int = R.layout.simple_dropdown_item_1line,
    textViewResourceId: Int = android.R.id.text1,
    objects: List<T> = mutableListOf(),
    private val lambda: (data: T) -> String
) : ArrayAdapter<T>(context, resource, textViewResourceId, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        getItem(position)?.let { view.text = lambda(it) }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        getItem(position)?.let { view.text = lambda(it) }
        return view
    }

    override fun getFilter(): Filter {
        return ArrayFilter()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    private inner class ArrayFilter : Filter() {

        override fun performFiltering(prefix: CharSequence?) = FilterResults()
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {}

        @Suppress("UNCHECKED_CAST")
        override fun convertResultToString(resultValue: Any?): CharSequence {
            val data = resultValue as? T
            return data?.let { lambda(it) } ?: ""
        }
    }
}

@BindingAdapter("bindWallet")
fun AutoCompleteTextView.bindWallet(data: List<Wallet>?) {
    var dataList = ArrayList<String>()
    data?.forEach {
        dataList.add(it.currencyName)
    }
    setText(String.EMPTY)
    dataList?.let {
        setText(it[0])
    }
    val objects = dataList?: mutableListOf()
    if (adapter == null) {
        val dropdownItemArrayAdapter = DropdownItemArrayAdapter(
            context = context,
            objects = objects,
            lambda = { it })
        setAdapter(dropdownItemArrayAdapter)
    } else {
        @Suppress("UNCHECKED_CAST")
        val dropdownItemArrayAdapter = adapter as? DropdownItemArrayAdapter<String>
        dropdownItemArrayAdapter?.cleanAndAdd(objects)
    }
}

/*@BindingAdapter("bindWallet")
fun AutoCompleteTextView.bindWallet(data: List<Wallet>?) {
    var dataList = ArrayList<String>()
    data?.forEach {
        dataList.add(it.currencyName)
    }
    setText(String.EMPTY)
    dataList?.let {
        setText(it[0])
    }
    val objects = data?: mutableListOf()
    if (adapter == null) {
        val dropdownItemArrayAdapter = DropdownItemArrayAdapter(
            context = context,
            objects = objects,
            lambda = { it.currencyName })
        setAdapter(dropdownItemArrayAdapter)
    } else {
        @Suppress("UNCHECKED_CAST")
        val dropdownItemArrayAdapter = adapter as? DropdownItemArrayAdapter<Wallet>
        dropdownItemArrayAdapter?.cleanAndAdd(objects)
   }
}
*/



