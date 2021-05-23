package com.kode.contacts.presentation.base.adapter

import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter

object BindingAdapters {

    @JvmStatic
    @BindingAdapter(value = ["loading"])
    fun ContentLoadingProgressBar.setLoading(isLoading: Boolean) {
        if (isLoading) this.show() else this.hide()
    }
}