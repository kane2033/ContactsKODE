package com.kode.contacts.presentation.contacts

import androidx.recyclerview.widget.DiffUtil
import com.kode.contacts.R
import com.kode.contacts.presentation.base.adapter.BaseListAdapter
import com.kode.domain.entity.Contact

class ContactsAdapter: BaseListAdapter<Contact>(Companion) {

    companion object : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean =
            oldItem.name == newItem.name
    }

    override fun getItemViewType(position: Int) = R.layout.item_contact

    // Неотсортированный список хранит изначальный список
    private var unfilteredList = listOf<Contact>()

    // При добавлении списка рецептов,
    // сохраняется изначальный список
    override fun submitList(list: List<Contact>?) {
        list?.let { unfilteredList = it }
        super.submitList(list)
    }

    // Когда требуется показать отсортированный список,
    // не перезаписываем unfilteredList
    private fun submitListFiltered(list: List<Contact>?) {
        super.submitList(list)
    }
}