package com.kode.domain.contacts.entity

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val id: Long = 0,
    val firstName: String,
    val lastName: String?,
    val phoneNumber: PhoneNumber,
    val notes: String?,
    val avatarUri: Uri?,
    val toneUri: Uri?
) : Parcelable