package com.kode.contacts.presentation.base.adapter

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter
import com.kode.domain.base.Event
import com.kode.domain.base.UiState

object BindingAdapters {

    @JvmStatic
    @BindingAdapter(value = ["loading"])
    fun ContentLoadingProgressBar.setLoading(uiState: Event<UiState>?) {
        if (uiState?.peekContent() is UiState.Loading) this.show() else this.hide()
    }

    @JvmStatic
    @BindingAdapter(value = ["imageFilePath", "defaultSrc"], requireAll = false)
    fun ImageView.setImageFromUri(uri: Uri?, defaultSrc: Drawable?) {
        if (uri != null) {
            setImageURI(uri)
        } else {
            setImageDrawable(defaultSrc)
        }
    }
}