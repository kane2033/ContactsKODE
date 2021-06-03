package com.kode.data.contacts.datasource.database.converter

import android.net.Uri
import com.kode.data.contacts.datasource.database.entity.ContactEntity
import com.kode.data.contacts.datasource.database.entity.ContactWithPhoneNumberEntity
import com.kode.data.contacts.datasource.database.entity.PhoneNumberEntity
import com.kode.domain.contacts.entity.Contact
import com.kode.domain.contacts.entity.PhoneNumber

fun ContactWithPhoneNumberEntity.toDomainEntity(): Contact {
    return Contact(
        id = contact.contactId,
        firstName = contact.firstName,
        lastName = contact.lastName,
        phoneNumber = phoneNumber.toDomainEntity(),
        notes = contact.notes,
        avatarUri = contact.avatarFilePath?.let { Uri.parse(it) },
        toneUri = contact.toneFilePath?.let { Uri.parse(it) }
    )
}

fun Contact.toDbEntity(): ContactWithPhoneNumberEntity {
    return ContactWithPhoneNumberEntity(
        contact = ContactEntity(
            contactId = id,
            firstName = firstName,
            lastName = lastName,
            notes = notes,
            avatarFilePath = avatarUri?.path,
            toneFilePath = toneUri?.path
        ),
        phoneNumber = phoneNumber.toDbEntity()
    )
}

fun PhoneNumberEntity.toDomainEntity() = PhoneNumber(
    id = phoneNumberId,
    number = number,
    type = enumValueOf(type)
)

fun PhoneNumber.toDbEntity() = PhoneNumberEntity(
    phoneNumberId = id,
    number = number,
    type = type.toString()
)