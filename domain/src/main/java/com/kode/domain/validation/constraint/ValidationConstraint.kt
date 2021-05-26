package com.kode.domain.validation.constraint

import com.kode.domain.validation.exception.ValidationFailure

sealed class ValidationConstraint(val validate: (String?) -> String) {
    object NotEmpty : ValidationConstraint(
        validate = { if (it.isNullOrBlank()) throw ValidationFailure.EmptyField else it }
    )
}