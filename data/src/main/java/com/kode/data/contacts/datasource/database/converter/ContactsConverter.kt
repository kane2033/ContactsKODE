package com.kode.data.contacts.datasource.database.converter

import com.kode.data.contacts.datasource.database.entity.ContactEntity
import com.kode.data.contacts.datasource.database.entity.ContactWithPhoneNumbersEntity
import com.kode.data.contacts.datasource.database.entity.PhoneNumberEntity
import com.kode.domain.contacts.entity.Contact
import com.kode.domain.contacts.entity.PhoneNumber

fun ContactWithPhoneNumbersEntity.toDomainEntity(): Contact {
    return Contact(
        firstName = contact.firstName,
        lastName = contact.lastName,
        phoneNumbers = phoneNumbers.map {
            PhoneNumber(
                number = it.number,
                type = enumValueOf(it.type)
            )
        },
        avatarFilePath = contact.avatarFilePath,
        toneFilePath = contact.toneFilePath
    )
}

fun List<ContactWithPhoneNumbersEntity>.toDomainEntityList(): List<Contact> {
    return map { it.toDomainEntity() }
}

fun Contact.toDbEntity(): ContactWithPhoneNumbersEntity {
    return ContactWithPhoneNumbersEntity(
        contact = ContactEntity(
            firstName = firstName,
            lastName = lastName,
            avatarFilePath = avatarFilePath,
            toneFilePath = toneFilePath
        ),
        phoneNumbers = phoneNumbers.map {
            PhoneNumberEntity(
                number = it.number,
                type = it.type.name
            )
        }
    )
}

fun List<Contact>.toDbEntityList(): List<ContactWithPhoneNumbersEntity> {
    return map { it.toDbEntity() }
}