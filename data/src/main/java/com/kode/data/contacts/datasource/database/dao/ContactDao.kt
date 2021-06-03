package com.kode.data.contacts.datasource.database.dao

import androidx.room.*
import com.kode.data.contacts.datasource.database.entity.ContactEntity
import com.kode.data.contacts.datasource.database.entity.ContactWithPhoneNumberEntity
import com.kode.data.contacts.datasource.database.entity.PhoneNumberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Transaction
    @Query("SELECT * FROM ContactEntity WHERE contactId=:id")
    fun getContactById(id: Long): Flow<ContactWithPhoneNumberEntity>

    @Transaction
    @Query("SELECT * FROM ContactEntity ORDER BY firstName")
    fun getAllByFirstName(): Flow<List<ContactWithPhoneNumberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContactEntity(contact: ContactEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoneNumberEntity(phoneNumber: PhoneNumberEntity)

    @Delete
    suspend fun deleteContactEntity(contact: ContactEntity): Int

    @Delete
    suspend fun deletePhoneNumberEntity(phoneNumber: PhoneNumberEntity)

    @Transaction
    suspend fun insertContactWithPhone(contact: ContactWithPhoneNumberEntity) {
        contactWithPhoneNumberTransaction(
            contact = contact,
            contactQuery = this::insertContactEntity,
            phoneNumberQuery = this::insertPhoneNumberEntity
        )
    }

    @Transaction
    suspend fun deleteContactWithPhones(contact: ContactWithPhoneNumberEntity) {
        contactWithPhoneNumberTransaction(
            contact = contact,
            contactQuery = { deleteContactEntity(contact.contact).toLong() },
            phoneNumberQuery = this::deletePhoneNumberEntity
        )
    }

    private suspend fun contactWithPhoneNumberTransaction(
        contact: ContactWithPhoneNumberEntity,
        contactQuery: suspend (contact: ContactEntity) -> Long,
        phoneNumberQuery: suspend (phoneNumber: PhoneNumberEntity) -> Unit
    ) {
        val contactEntity = contact.contact
        val phoneEntity = contact.phoneNumber

        val contactId = contactQuery(contactEntity)
        phoneEntity.phoneOwnerId = contactId
        phoneNumberQuery(phoneEntity)
    }
}