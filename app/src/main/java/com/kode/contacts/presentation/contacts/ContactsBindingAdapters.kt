package com.kode.contacts.presentation.contacts

import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.kode.contacts.R
import com.kode.domain.contacts.entity.PhoneNumberType
import com.kode.domain.validation.constraint.ValidationConstraint
import com.kode.domain.validation.exception.ValidationFailure

object ContactsBindingAdapters {
    @JvmStatic
    @BindingAdapter(value = ["phoneType"])
    fun TextView.setPhoneType(type: PhoneNumberType?) {
        if (type == null) return

        val resId = when (type) {
            PhoneNumberType.MOBILE -> R.string.phone_type_mobile
            PhoneNumberType.HOME -> R.string.phone_type_home
            PhoneNumberType.WORK -> R.string.phone_type_work
        }
        setText(resId)
    }

    @JvmStatic
    @BindingAdapter(value = ["constraint", "errorText"], requireAll = false)
    fun TextInputEditText.setErrorMessage(
        constraint: ValidationConstraint,
        errorText: String?
    ) {
        doOnTextChanged { text, _, _, _ ->
            try {
                constraint.validate(text.toString())
            } catch (failure: ValidationFailure) {
                error = errorText ?: context.getString(R.string.error_validation_field)
            }
        }
    }
}