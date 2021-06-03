package com.kode.contacts.presentation.base.exception

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Контейнер текстовой информации об ошибке.
 * */
sealed class FailureInfo {

    /**
     * Используется с полноэкранным диалоговым окном
     * */
    @Parcelize
    data class FullScreen(
        val title: String,
        val text: String,
        val buttonText: String? = null
    ) : FailureInfo(), Parcelable

    /**
     * Используется со SnackBar
     * */
    data class Small(
        val retryClickedCallback: () -> Unit,
        val text: String,
        val buttonText: String? = null
    ) : FailureInfo()
}