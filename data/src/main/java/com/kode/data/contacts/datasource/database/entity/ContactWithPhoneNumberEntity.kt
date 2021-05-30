package com.kode.data.contacts.datasource.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ContactWithPhoneNumberEntity(
    @Embedded val contact: ContactEntity,
    @Relation(
        parentColumn = "contactId",
        entityColumn = "phoneOwnerId"
    )
    val phoneNumber: PhoneNumberEntity
)