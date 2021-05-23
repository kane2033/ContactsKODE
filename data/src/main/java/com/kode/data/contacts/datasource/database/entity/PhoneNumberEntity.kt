package com.kode.data.contacts.datasource.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhoneNumberEntity(
    @PrimaryKey(autoGenerate = true) val phoneNumberId: Long = 0,
    val number: String,
    val type: String,
    var phoneOwnerId: Long = 0,
)