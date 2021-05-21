package com.kode.contacts.presentation.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kode.contacts.BR

/**
 * Базовая имплементация [ListAdapter]
 * */
abstract class BaseListAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>,
    var itemClickedInterface: ItemClickedInterface<T> = ItemClickedInterface { }
) :
    ListAdapter<T, BaseListAdapter<T>.BaseViewHolder<T>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) =
        holder.bind(getItem(position))

    inner class BaseViewHolder<T>(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: T) {
            binding.setVariable(BR.item, item)
            binding.setVariable(BR.clickListener, itemClickedInterface)
            binding.executePendingBindings()
        }
    }
}