package com.kode.contacts.presentation.contacts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kode.contacts.R
import com.kode.contacts.databinding.FragmentContactsListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactsListFragment : Fragment(R.layout.fragment_contacts_list) {

    private val binding: FragmentContactsListBinding by viewBinding(FragmentContactsListBinding::bind)

    private val viewModel: ContactsListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = this@ContactsListFragment.viewModel
            adapter = ContactsAdapter()
        }
    }
}