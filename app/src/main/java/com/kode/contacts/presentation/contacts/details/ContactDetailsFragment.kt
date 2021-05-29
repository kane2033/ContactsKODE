package com.kode.contacts.presentation.contacts.details

import android.os.Bundle
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
import com.kode.contacts.presentation.base.extension.setupToolbarWithNavController
import com.kode.domain.contacts.entity.PhoneNumber
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ContactDetailsFragment : Fragment(R.layout.fragment_contact_details) {

    private val binding: FragmentContactDetailsBinding by viewBinding(FragmentContactDetailsBinding::bind)

    private val args: ContactDetailsFragmentArgs by navArgs()

    private val viewModel: ContactDetailsViewModel by viewModel { parametersOf(args.selectedContactId) }

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
                val action =
                    ContactDetailsFragmentDirections.actionContactDetailsFragmentToContactEditFragment(
                        contact = this@ContactDetailsFragment.viewModel.contact.value
                    )
                findNavController().navigate(action)
            }

            toolbar.apply {
                inflateMenu(R.menu.toolbar_contact_details)

                val snackBarInDevelopment = {
                    makeSnackBar(R.string.feature_in_development, view = binding.editContactButton)
                    true
                }

                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.favoriteButton -> snackBarInDevelopment()
                        R.id.inDevelopmentButton -> snackBarInDevelopment()
                        else -> false
                    }
                }

                setupToolbarWithNavController(toolbar)
            }
        }

        viewModel.contact.observe(viewLifecycleOwner, {
            adapter.submitList(it.phoneNumbers)
        })

        viewModel.uiState.observeFailure(viewLifecycleOwner, {
            openFailureView(it)
        })
    }
}