package com.kode.contacts.presentation.base.adapter

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter
import com.kode.contacts.presentation.base.entity.Event
import com.kode.contacts.presentation.base.entity.UiState
import com.kode.data.contacts.datasource.database.extension.getFileName
import java.io.File

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

    @JvmStatic
    @BindingAdapter(value = ["fileUriToName"])
    fun TextView.setFileNameFromUri(fileUri: Uri?) {
        if (fileUri == null) return
        val fileName = context.contentResolver.getFileName(fileUri) ?: File(
            fileUri.path ?: return
        ).nameWithoutExtension
        text = fileName
    }
}