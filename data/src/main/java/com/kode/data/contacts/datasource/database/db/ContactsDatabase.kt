package com.kode.data.contacts.datasource.database.db

import android.content.Context
import androidx.room.*
import com.kode.data.contacts.datasource.database.dao.ContactDao
import com.kode.data.contacts.datasource.database.entity.ContactEntity

@Database(entities = [ContactEntity::class], version = 1)
abstract class ContactsDatabase: RoomDatabase() {
    companion object {
        private const val NAME = "contacts_db"

        fun getInstance(context: Context) = Room.databaseBuilder(
            context,
            ContactsDatabase::class.java,
            NAME
        ).build()
    }

    abstract fun contactDao(): ContactDao
}