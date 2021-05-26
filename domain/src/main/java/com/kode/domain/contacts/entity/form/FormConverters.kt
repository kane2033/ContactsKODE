package com.kode.domain.contacts.entity.form

import com.kode.domain.contacts.entity.Contact
import com.kode.domain.contacts.entity.PhoneNumber

fun Contact.toForm() = ContactForm(
    id = id,
    firstName = firstName,
    lastName = lastName,
    phoneNumber = phoneNumbers[0].toForm(),
    avatarFilePath = avatarFilePath,
    toneFilePath = toneFilePath
)

private fun PhoneNumber.toForm() = PhoneNumberForm(
    id = id,
    number = number,
    type = type.toString()
)