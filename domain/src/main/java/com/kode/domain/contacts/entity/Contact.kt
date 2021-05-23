package com.kode.domain.contacts.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val firstName: String,
    val lastName: String?,
    val phoneNumbers: List<PhoneNumber>,
    val avatarFilePath: String?, // Как хранить аватарку??
    val toneFilePath: String?
) : Parcelable