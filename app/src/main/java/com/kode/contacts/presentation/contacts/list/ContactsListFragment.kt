package com.kode.contacts.presentation.contacts.list

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kode.contacts.R
import com.kode.contacts.databinding.FragmentContactsListBinding
import com.kode.contacts.presentation.base.adapter.ItemClickedInterface
import com.kode.contacts.presentation.base.extension.makeSnackBar
import com.kode.contacts.presentation.base.extension.observeFailure
import com.kode.contacts.presentation.base.extension.openFailureView
import com.kode.contacts.presentation.base.extension.setupToolbarAndDrawerWithNavController
import com.kode.domain.contacts.entity.Contact
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactsListFragment : Fragment(R.layout.fragment_contacts_list) {

    private val binding: FragmentContactsListBinding by viewBinding(FragmentContactsListBinding::bind)

    private val viewModel: ContactsListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onContactClicked = ItemClickedInterface<Contact> {
            val action =
                ContactsListFragmentDirections.actionContactsListFragmentToContactDetailsFragment(it.id)
            findNavController().navigate(action)
        }

        val adapter = ContactsAdapter(onContactClicked)
        val itemDecoration = StickyLetterDecoration(requireContext())

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ContactsListFragment.viewModel

            recyclerView.apply {
                this.adapter = adapter
                addItemDecoration(itemDecoration)
            }

            addContactButton.setOnClickListener {
                val action =
                    ContactsListFragmentDirections.actionContactsListFragmentToContactEditFragment()
                findNavController().navigate(action)
            }

            toolbar.apply {
                inflateMenu(R.menu.toolbar_contacts_list)

                val searchView = menu.findItem(R.id.searchButton).actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    // ???????????????????? ?????? ???????????? ?????????? ??????????????
                    override fun onQueryTextChange(newText: String?): Boolean {
                        newText?.let { this@ContactsListFragment.viewModel.searchContacts(newText) }
                        return false
                    }

                    override fun onQueryTextSubmit(query: String?) = false
                })

                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.settingsButton -> {
                            makeSnackBar(
                                R.string.feature_in_development,
                                view = binding.addContactButton
                            )
                            true
                        }
                        else -> false
                    }
                }

                setupToolbarAndDrawerWithNavController(this, drawerLayout, navigationView)
            }
        }

        viewModel.contacts.observe(viewLifecycleOwner, { list ->
            itemDecoration.setNewWords(list.map { it.firstName })
            adapter.submitList(list)
        })

        viewModel.uiState.observeFailure(viewLifecycleOwner, {
            openFailureView(it)
        })
    }
}