package com.kode.contacts.presentation.contacts.details

import androidx.recyclerview.widget.DiffUtil
import com.kode.contacts.R
import com.kode.contacts.presentation.base.adapter.BaseListAdapter
import com.kode.contacts.presentation.base.adapter.ItemClickedInterface
import com.kode.domain.contacts.entity.PhoneNumber

class PhoneNumberAdapter(clickListener: ItemClickedInterface<PhoneNumber>) :
    BaseListAdapter<PhoneNumber>(Companion, clickListener) {

    companion object : DiffUtil.ItemCallback<PhoneNumber>() {
        override fun areItemsTheSame(oldItem: PhoneNumber, newItem: PhoneNumber): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: PhoneNumber, newItem: PhoneNumber): Boolean =
            oldItem.id == newItem.id
    }

    override fun getItemViewType(position: Int) = R.layout.item_phone_number
}