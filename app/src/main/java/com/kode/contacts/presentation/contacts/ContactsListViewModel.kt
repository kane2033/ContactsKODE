package com.kode.contacts.presentation.contacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kode.contacts.presentation.base.adapter.ItemClickedInterface
import com.kode.domain.entity.Contact

class ContactsListViewModel: ViewModel() {

    val contacts = MutableLiveData<List<Contact>>()

    val onContactClicked = ItemClickedInterface<Contact> {

    }
}