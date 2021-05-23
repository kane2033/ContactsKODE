package com.kode.contacts.presentation.contacts.list

import androidx.recyclerview.widget.DiffUtil
import com.kode.contacts.R
import com.kode.contacts.presentation.base.adapter.BaseListAdapter
import com.kode.contacts.presentation.base.adapter.ItemClickedInterface
import com.kode.domain.contacts.entity.Contact

class ContactsAdapter(clickListener: ItemClickedInterface<Contact>) :
    BaseListAdapter<Contact>(Companion, clickListener) {

    companion object : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean =
            "${oldItem.firstName} + ${oldItem.lastName}" == "${newItem.firstName} + ${newItem.lastName}"
    }

    override fun getItemViewType(position: Int) = R.layout.item_contact

    // Неотсортированный список хранит изначальный список
    private var unfilteredList = listOf<Contact>()

    // При добавлении списка,
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