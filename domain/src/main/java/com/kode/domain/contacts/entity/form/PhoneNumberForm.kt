package com.kode.domain.contacts.entity.form

import com.kode.domain.contacts.entity.PhoneNumber
import com.kode.domain.contacts.entity.PhoneNumberType
import com.kode.domain.validation.constraint.ValidationConstraint

data class PhoneNumberForm(
    val id: Long = 0,
    var number: String? = null,
    var type: PhoneNumberType? = null
) {
    fun isEmpty(): Boolean {
        return number.isNullOrBlank() and (type == null)
    }

    fun toPhoneNumber() = PhoneNumber(
        id = id,
        number = ValidationConstraint.NotEmpty.validate(number),
        type = enumValueOf(ValidationConstraint.NotEmpty.validate(type.toString()))
    )
}