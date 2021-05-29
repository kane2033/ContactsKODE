package com.kode.contacts.presentation.base

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.kode.contacts.R

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    // Всегда false, потому что реализация во фрагментах
    override fun onOptionsItemSelected(item: MenuItem) = false
}