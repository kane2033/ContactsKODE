package com.kode.data.contacts.datasource.database.converter

import com.kode.data.contacts.datasource.database.entity.ContactEntity
import com.kode.domain.contacts.entity.Contact

fun ContactEntity.toDomainEntity(): Contact {
    return Contact("$firstName $lastName")
}

fun List<ContactEntity>.toDomainEntityList(): List<Contact> {
    return map { it.toDomainEntity() }
}

fun Contact.toDbEntity(): ContactEntity {
    val (firstName, lastName) = name.split(" ", limit = 2)
    return ContactEntity(
        firstName = firstName,
        lastName = lastName,
        phone = "",
        phoneType = "mobile"
    )
}

fun List<Contact>.toDbEntityList(): List<ContactEntity> {
    return map { it.toDbEntity() }
}