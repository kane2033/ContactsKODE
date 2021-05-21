package com.kode.contacts.presentation.base.adapter

fun interface ItemClickedInterface<T> {
    fun onClick(item: T)
}