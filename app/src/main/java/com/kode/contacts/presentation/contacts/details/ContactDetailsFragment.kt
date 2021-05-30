package com.kode.contacts.presentation.contacts.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kode.contacts.R
import com.kode.contacts.databinding.FragmentContactDetailsBinding
import com.kode.contacts.presentation.base.extension.makeSnackBar
import com.kode.contacts.presentation.base.extension.observeFailure
import com.kode.contacts.presentation.base.extension.openFailureView
import com.kode.contacts.presentation.base.extension.setupToolbarWithNavController
import com.kode.domain.base.exception.info.SmallFailureInfo
import com.kode.domain.contacts.exception.ContactDetailsFailure
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ContactDetailsFragment : Fragment(R.layout.fragment_contact_details) {

    private val binding: FragmentContactDetailsBinding by viewBinding(FragmentContactDetailsBinding::bind)

    private val args: ContactDetailsFragmentArgs by navArgs()

    private val viewModel: ContactDetailsViewModel by viewModel { parametersOf(args.selectedContactId) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ContactDetailsFragment.viewModel

            phoneIconButton.setOnClickListener {
                this@ContactDetailsFragment.viewModel.getPhoneNumber()?.let { dial(it) }
            }
            chatIconButton.setOnClickListener {
                this@ContactDetailsFragment.viewModel.getPhoneNumber()?.let { sendSms(it) }
            }
            videoIconButton.setOnClickListener {
                makeSnackBar(R.string.feature_in_development)
            }

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

        viewModel.uiState.observeFailure(viewLifecycleOwner, {
            openFailureView(it) { failure ->
                when (failure) {
                    ContactDetailsFailure.PhoneIncorrect -> SmallFailureInfo(
                        retryClickedCallback = {},
                        text = getString(R.string.error_phone_incorrect)
                    )
                    else -> null
                }
            }
        })
    }

    private fun dial(phoneNumber: String) {
        try {
            val intentDial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            context?.startActivity(intentDial)
        } catch (e: Throwable) {
            viewModel.setPhoneIncorrectFailure()
        }
    }

    private fun sendSms(phoneNumber: String) {
        try {
            val intentDial = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$phoneNumber"))
            context?.startActivity(intentDial)
        } catch (e: Throwable) {
            viewModel.setPhoneIncorrectFailure()
        }
    }
}