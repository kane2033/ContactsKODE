package com.kode.contacts.presentation.contacts.edit

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kode.contacts.R
import com.kode.contacts.databinding.FragmentContactEditBinding
import com.kode.contacts.presentation.base.extension.makeAlertDialog
import com.kode.contacts.presentation.base.extension.makeSnackBar
import com.kode.contacts.presentation.base.extension.observeFailure
import com.kode.contacts.presentation.base.extension.openFailureView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ContactEditFragment : Fragment(R.layout.fragment_contact_edit) {

    private val binding: FragmentContactEditBinding by viewBinding(FragmentContactEditBinding::bind)

    private val args: ContactEditFragmentArgs by navArgs()

    private val viewModel: ContactEditViewModel by viewModel { parametersOf(args.contact) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ContactEditFragment.viewModel

            val changeAvatar = View.OnClickListener {
                // viewModel.changeAvatar()
            }
            changeAvatarButton.setOnClickListener(changeAvatar)
            avatarView.setOnClickListener(changeAvatar)
        }

        viewModel.uiState.observeFailure(viewLifecycleOwner, {
            openFailureView(it)
        })

        // Установка заголовка и левой кнопки (на галочку) тулбара
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            val title = when (viewModel.mode) {
                ContactEditViewModel.Mode.CREATE -> R.string.contact_create_title
                ContactEditViewModel.Mode.EDIT -> R.string.contact_edit_title
            }
            setTitle(title)

            setHomeAsUpIndicator(R.drawable.ic_done)
        }

        // При нажатии "Назад"
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Предупреждаем о несохраненных изменениях через alertDialog
            makeAlertDialog(
                title = R.string.contact_unsaved_title,
                message = R.string.contact_unsaved,
                positiveCallback = { findNavController().popBackStack() },
                negativeCallback = { }
            )
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_contact_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // viewModel.save()
            }
            R.id.deleteButton -> {
                // viewModel.delete()
                val action =
                    ContactEditFragmentDirections.actionContactEditFragmentToContactsListFragment()
                findNavController().navigate(action)
            }
            R.id.inDevelopmentButton -> makeSnackBar(R.string.feature_in_development)
        }
        return super.onOptionsItemSelected(item)
    }
}