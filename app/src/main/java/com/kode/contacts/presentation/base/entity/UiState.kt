package com.kode.contacts.presentation.base.entity

import com.kode.domain.base.exception.Failure as ThrowableFailure

sealed class UiState {
    object Success : UiState()
    data class Failure(val failure: ThrowableFailure) : UiState()
    object Loading : UiState()
}