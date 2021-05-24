package com.kode.contacts.presentation.contacts

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.kode.contacts.R
import com.kode.domain.contacts.entity.PhoneNumberType

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
}