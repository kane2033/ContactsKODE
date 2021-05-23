package com.kode.data.contacts.datasource.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactEntity(
    @PrimaryKey(autoGenerate = true) val contactId: Long = 0,
    val firstName: String,
    val lastName: String?,
    val avatarFilePath: String?,
    val toneFilePath: String?
)