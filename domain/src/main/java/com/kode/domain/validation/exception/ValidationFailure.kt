package com.kode.domain.validation.exception

import com.kode.domain.base.exception.Failure

sealed class ValidationFailure : Failure.FeatureFailure() {
    object EmptyField : ValidationFailure()
}