package com.kode.data.contacts.datasource.database.dao

import androidx.room.*
import com.kode.data.contacts.datasource.database.entity.ContactEntity
import com.kode.data.contacts.datasource.database.entity.ContactWithPhoneNumbersEntity
import com.kode.data.contacts.datasource.database.entity.PhoneNumberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Transaction
    @Query("SELECT * FROM ContactEntity")
    fun getAll(): Flow<List<ContactWithPhoneNumbersEntity>>

    @Transaction
    @Query("SELECT * FROM ContactEntity ORDER BY firstName")
    fun getAllByFirstName(): Flow<List<ContactWithPhoneNumbersEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContactEntity(contact: ContactEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoneNumbersEntities(phoneNumbers: List<PhoneNumberEntity>)

    @Transaction
    suspend fun insertContactsListWithPhones(contacts: List<ContactWithPhoneNumbersEntity>) {
        contacts.forEach { insertContactWithPhones(it) }
    }

    @Transaction
    suspend fun insertContactWithPhones(contact: ContactWithPhoneNumbersEntity) {
        val contactEntity = contact.contact
        val phoneEntities = contact.phoneNumbers

        val contactId = insertContactEntity(contactEntity)
        phoneEntities.forEach { it.phoneOwnerId = contactId }
        insertPhoneNumbersEntities(phoneEntities)
    }
}