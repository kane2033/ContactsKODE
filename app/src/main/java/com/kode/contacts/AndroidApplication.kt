package com.kode.contacts

import android.app.Application
import com.kode.contacts.di.ContactsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AndroidApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@AndroidApplication)
            modules(
                ContactsModule.module
            )
        }
    }
}