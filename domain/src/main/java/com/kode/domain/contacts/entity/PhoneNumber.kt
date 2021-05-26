package com.kode.domain.contacts.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhoneNumber(
    val id: Long = 0,
    val number: String,
    val type: PhoneNumberType
) : Parcelable