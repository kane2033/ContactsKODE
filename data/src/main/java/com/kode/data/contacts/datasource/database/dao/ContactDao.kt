package com.kode.data.contacts.datasource.database.dao

import androidx.room.*
import com.kode.data.contacts.datasource.database.entity.ContactEntity
import com.kode.data.contacts.datasource.database.entity.ContactWithPhoneNumbersEntity
import com.kode.data.contacts.datasource.database.entity.PhoneNumberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Transaction
    @Query("SELECT * FROM ContactEntity WHERE contactId=:id")
    fun getContactById(id: Long): Flow<ContactWithPhoneNumbersEntity>

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

    @Delete
    suspend fun deleteContactEntity(contact: ContactEntity): Int

    @Delete
    suspend fun deletePhoneNumbersEntities(phoneNumbers: List<PhoneNumberEntity>)

    @Transaction
    suspend fun insertContactsListWithPhones(contacts: List<ContactWithPhoneNumbersEntity>) {
        contacts.forEach { insertContactWithPhones(it) }
    }

    @Transaction
    suspend fun insertContactWithPhones(contact: ContactWithPhoneNumbersEntity) {
        contactWithPhoneNumbersTransaction(
            contact = contact,
            contactQuery = this::insertContactEntity,
            phoneNumbersQuery = this::insertPhoneNumbersEntities
        )
    }

    @Transaction
    suspend fun deleteContactWithPhones(contact: ContactWithPhoneNumbersEntity) {
        contactWithPhoneNumbersTransaction(
            contact = contact,
            contactQuery = { deleteContactEntity(contact.contact).toLong() },
            phoneNumbersQuery = this::deletePhoneNumbersEntities
        )
    }

    private suspend fun contactWithPhoneNumbersTransaction(
        contact: ContactWithPhoneNumbersEntity,
        contactQuery: suspend (contact: ContactEntity) -> Long,
        phoneNumbersQuery: suspend (phoneNumbers: List<PhoneNumberEntity>) -> Unit
    ) {
        val contactEntity = contact.contact
        val phoneEntities = contact.phoneNumbers

        val contactId = contactQuery(contactEntity)
        phoneEntities.forEach { it.phoneOwnerId = contactId }
        phoneNumbersQuery(phoneEntities)
    }
}