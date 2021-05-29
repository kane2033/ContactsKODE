package com.kode.domain.contacts.usecase

import com.kode.domain.base.usecase.FlowUseCase
import com.kode.domain.contacts.datasource.ContactsDataSource
import com.kode.domain.contacts.entity.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class SearchContacts(private val contactsDataSource: ContactsDataSource) :
    FlowUseCase<String, List<Contact>>() {
    override fun run(param: String): Flow<List<Contact>> {
        return contactsDataSource.getAllContacts().map {
            doSearch(param, it)
        }
    }

    private fun doSearch(constraint: String, unfilteredContacts: List<Contact>): List<Contact> {
        return if (constraint.isBlank()) {
            // Не осуществляем поиск, если запрос пуст
            unfilteredContacts
        } else {
            // Осуществляем фильтрацию (поиск)
            val filterPattern = constraint.toLowerCase(Locale.getDefault()).trim()
            unfilteredContacts.filter {
                it.firstName.lowerCaseContains(filterPattern) ||
                        it.lastName.lowerCaseContains(filterPattern)
            }
        }
    }

    // Возвращает boolean, означающий наличие в данной строке
    // указанного filterPattern, не учитывая регистр.
    // Если строка == null, возвращается false (проверка == true в конце)
    private fun String?.lowerCaseContains(filterPattern: String) =
        this?.toLowerCase(Locale.getDefault())?.contains(filterPattern) == true
}