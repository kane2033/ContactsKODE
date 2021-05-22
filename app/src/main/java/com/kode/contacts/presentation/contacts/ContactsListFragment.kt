package com.kode.contacts.presentation.contacts

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kode.contacts.R
import com.kode.contacts.databinding.FragmentContactsListBinding
import com.kode.contacts.presentation.base.extension.makeSnackBar
import com.kode.contacts.presentation.base.extension.observeEvent
import com.kode.contacts.presentation.base.extension.observeFailure
import com.kode.contacts.presentation.base.extension.openFailureView
import com.kode.domain.base.UiState
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

        viewModel.uiState.observeFailure(viewLifecycleOwner, {
            openFailureView(it)
        })

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_contacts_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.searchButton -> makeSnackBar(
                R.string.feature_in_development,
                view = binding.addContactButton
            )
            R.id.settingsButton -> makeSnackBar(
                R.string.feature_in_development,
                view = binding.addContactButton
            )
        }
        return super.onOptionsItemSelected(item)
    }
}