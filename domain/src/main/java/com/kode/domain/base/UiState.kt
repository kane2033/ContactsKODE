package com.kode.domain.base

import com.kode.domain.base.exception.Failure as ThrowableFailure

sealed class UiState {
    object Success : UiState()
    data class Failure(val failure: ThrowableFailure) : UiState()
    object Loading : UiState()
}