package com.kode.contacts.presentation.base.adapter

import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

object BindingAdapters {

    @JvmStatic
    @BindingAdapter(value = ["loading"])
    fun ContentLoadingProgressBar.setLoading(isLoading: Boolean) {
        if (isLoading) this.show() else this.hide()
    }

    @JvmStatic
    @BindingAdapter(value = ["recyclerAdapter"])
    fun RecyclerView.bindRecyclerViewAdapter(adapter: BaseListAdapter<*>) {
        //setHasFixedSize(true) // с осторожностью
        this.adapter = adapter
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    @BindingAdapter(value = ["items"])
    fun <T> RecyclerView.setItems(items: List<T>?) {
        (adapter as BaseListAdapter<T>).submitList(items)
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    @BindingAdapter(value = ["onClick"])
    fun <T> RecyclerView.setOnClickListener(onClick: ItemClickedInterface<T>?) {
        onClick?.let {
            (adapter as BaseListAdapter<T>).itemClickedInterface = it
        }
    }
}