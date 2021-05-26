package com.kode.contacts.presentation.contacts

import android.content.res.Resources
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.textfield.TextInputEditText
import com.kode.contacts.R
import com.kode.domain.contacts.entity.PhoneNumberType
import com.kode.domain.validation.constraint.ValidationConstraint
import com.kode.domain.validation.exception.ValidationFailure

object ContactsBindingAdapters {
    private val phoneNumberTypeTranslation =
        mapOf(
            PhoneNumberType.MOBILE to R.string.phone_type_mobile,
            PhoneNumberType.HOME to R.string.phone_type_home,
            PhoneNumberType.WORK to R.string.phone_type_work
        )

    fun Resources.getPhoneTypesTranslation(): Map<PhoneNumberType, String> {
        return phoneNumberTypeTranslation.mapValues { getString(it.value) }
    }

    @JvmStatic
    @BindingAdapter(value = ["phoneType"])
    fun TextView.setPhoneType(type: PhoneNumberType?) {
        val resId = phoneNumberTypeTranslation[type] ?: return
        setText(resId)
    }

    @JvmStatic
    @BindingAdapter(value = ["phoneType"])
    fun AutoCompleteTextView.setPhoneType(type: PhoneNumberType?) {
        val text = context.resources.getPhoneTypesTranslation()[type] ?: return
        setText(text, false)
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "phoneType")
    fun AutoCompleteTextView.getPhoneType(): PhoneNumberType {
        val translation = context.resources.getPhoneTypesTranslation()
        return translation.filterValues { text.toString() == it }.keys.first()
    }

    @JvmStatic
    @BindingAdapter(value = ["phoneTypeAttrChanged"])
    fun AutoCompleteTextView.setPhoneTypeListener(attrChange: InverseBindingListener) {
        if (onItemClickListener == null) {
            setOnItemClickListener { _, _, _, _ ->
                attrChange.onChange()
            }
        }
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