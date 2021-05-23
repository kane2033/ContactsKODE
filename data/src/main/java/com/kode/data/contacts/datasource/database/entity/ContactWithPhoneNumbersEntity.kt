package com.kode.data.contacts.datasource.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ContactWithPhoneNumbersEntity(
    @Embedded val contact: ContactEntity,
    @Relation(
        parentColumn = "contactId",
        entityColumn = "phoneOwnerId"
    )
    val phoneNumbers: List<PhoneNumberEntity>
)