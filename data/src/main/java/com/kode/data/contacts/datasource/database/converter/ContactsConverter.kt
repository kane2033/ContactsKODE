package com.kode.data.contacts.datasource.database.converter

import android.net.Uri
import com.kode.data.contacts.datasource.database.entity.ContactEntity
import com.kode.data.contacts.datasource.database.entity.ContactWithPhoneNumbersEntity
import com.kode.data.contacts.datasource.database.entity.PhoneNumberEntity
import com.kode.domain.contacts.entity.Contact
import com.kode.domain.contacts.entity.PhoneNumber

fun ContactWithPhoneNumbersEntity.toDomainEntity(): Contact {
    return Contact(
        id = contact.contactId,
        firstName = contact.firstName,
        lastName = contact.lastName,
        phoneNumbers = phoneNumbers.map {
            PhoneNumber(
                id = it.phoneNumberId,
                number = it.number,
                type = enumValueOf(it.type)
            )
        },
        avatarUri = contact.avatarFilePath?.let { Uri.parse(it) },
        toneUri = contact.toneFilePath?.let { Uri.parse(it) }
    )
}

fun List<ContactWithPhoneNumbersEntity>.toDomainEntityList(): List<Contact> {
    return map { it.toDomainEntity() }
}

fun Contact.toDbEntity(): ContactWithPhoneNumbersEntity {
    return ContactWithPhoneNumbersEntity(
        contact = ContactEntity(
            contactId = id,
            firstName = firstName,
            lastName = lastName,
            avatarFilePath = avatarUri?.path,
            toneFilePath = toneUri?.path
        ),
        phoneNumbers = phoneNumbers.map {
            PhoneNumberEntity(
                phoneNumberId = id,
                number = it.number,
                type = it.type.name
            )
        }
    )
}

fun List<Contact>.toDbEntityList(): List<ContactWithPhoneNumbersEntity> {
    return map { it.toDbEntity() }
}