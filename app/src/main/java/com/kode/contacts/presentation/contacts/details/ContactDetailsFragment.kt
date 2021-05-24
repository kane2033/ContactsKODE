package com.kode.contacts.presentation.contacts.details

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kode.contacts.R
import com.kode.contacts.databinding.FragmentContactDetailsBinding
import com.kode.contacts.presentation.base.adapter.ItemClickedInterface
import com.kode.contacts.presentation.base.extension.makeSnackBar
import com.kode.contacts.presentation.base.extension.observeFailure
import com.kode.contacts.presentation.base.extension.openFailureView
import com.kode.domain.contacts.entity.PhoneNumber
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ContactDetailsFragment : Fragment(R.layout.fragment_contact_details) {

    private val binding: FragmentContactDetailsBinding by viewBinding(FragmentContactDetailsBinding::bind)

    private val args: ContactDetailsFragmentArgs by navArgs()

    private val viewModel: ContactDetailsViewModel by viewModel { parametersOf(args.selectedContact) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onPhoneClicked = ItemClickedInterface<PhoneNumber> {
            makeSnackBar(R.string.feature_in_development, view = binding.editContactButton)
        }

        val adapter = PhoneNumberAdapter(onPhoneClicked)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ContactDetailsFragment.viewModel
            phoneNumbersRecyclerView.adapter = adapter

            editContactButton.setOnClickListener {
                //val title = getString(ContactEditFragment.getTitle(ContactEditFragment.Mode.EDIT))
                val action =
                    ContactDetailsFragmentDirections.actionContactDetailsFragmentToContactEditFragment(
                        contact = this@ContactDetailsFragment.viewModel.contact.value
                    )
                findNavController().navigate(action)
            }
        }

        viewModel.contact.observe(viewLifecycleOwner, {
            adapter.submitList(it.phoneNumbers)
        })

        viewModel.uiState.observeFailure(viewLifecycleOwner, {
            openFailureView(it)
        })

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_contact_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val snackBarInDevelopment =
            { makeSnackBar(R.string.feature_in_development, view = binding.editContactButton) }
        when (item.itemId) {
            R.id.favoriteButton -> snackBarInDevelopment()
            R.id.inDevelopmentButton -> snackBarInDevelopment()
        }
        return super.onOptionsItemSelected(item)
    }
}