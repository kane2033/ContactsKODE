package com.kode.data.contacts.datasource.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kode.data.contacts.datasource.database.dao.ContactDao
import com.kode.data.contacts.datasource.database.entity.ContactEntity
import com.kode.data.contacts.datasource.database.entity.PhoneNumberEntity

@Database(entities = [ContactEntity::class, PhoneNumberEntity::class], version = 1)
abstract class ContactsDatabase : RoomDatabase() {
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