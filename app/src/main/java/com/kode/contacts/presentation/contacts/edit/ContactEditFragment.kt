package com.kode.contacts.presentation.contacts.edit

import android.Manifest
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kode.contacts.R
import com.kode.contacts.databinding.FragmentContactEditBinding
import com.kode.contacts.presentation.base.extension.*
import com.kode.contacts.presentation.contacts.ContactsBindingAdapters.getPhoneTypesTranslation
import com.kode.contacts.presentation.contacts.photo.GetPictureClickListener
import com.kode.data.contacts.datasource.database.extension.getFileName
import com.kode.domain.base.exception.info.SmallFailureInfo
import com.kode.domain.validation.constraint.ValidationConstraint
import com.kode.domain.validation.exception.ValidationFailure
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ContactEditFragment : Fragment(R.layout.fragment_contact_edit), GetPictureClickListener {

    private val binding: FragmentContactEditBinding by viewBinding(FragmentContactEditBinding::bind)

    private val args: ContactEditFragmentArgs by navArgs()

    private val viewModel: ContactEditViewModel by viewModel { parametersOf(args.contact) }

    companion object {
        private const val IMAGE_MIME = "image/*"
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    }

    private val getTone = registerForActivityResult(PickRingtoneContract()) { uri: Uri? ->
        viewModel.contactForm.value?.toneUri = uri
        binding.ringtoneEditText.setText(uri?.let { context?.contentResolver?.getFileName(it) })
    }

    private val getPicture =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                viewModel.setAvatarPathFromGallery(it)
                binding.avatarView.setImageURI(it)
            }
        }

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { pictureTaken ->
            if (pictureTaken) {
                viewModel.setAvatarPathFromCamera()
                binding.avatarView.setImageURI(viewModel.newPicImageUri)
            } else {
                viewModel.deleteImageFile()
            }
        }

    private val requestCameraPermission = permissionActivityResultContract(
        onGranted = { onCameraPermissionGranted() },
        onRejected = { makeSnackBar(R.string.take_picture_permission_required) }
    )

    private fun onCameraPermissionGranted() {
        viewModel.createImageFile { uri -> takePicture.launch(uri) }
    }

    override fun onTakePicture() {
        if (context.hasPermission(CAMERA_PERMISSION)) {
            onCameraPermissionGranted()
        } else {
            requestCameraPermission.launch(CAMERA_PERMISSION)
        }
    }

    override fun onChoosePicture() {
        getPicture.launch(IMAGE_MIME)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ContactEditFragment.viewModel
            notEmpty = ValidationConstraint.NotEmpty

            val openGetPictureDialog = View.OnClickListener {
                val action =
                    ContactEditFragmentDirections.actionContactEditFragmentToGetPictureBottomSheetDialog()
                findNavController().navigate(action)
            }
            changeAvatarButton.setOnClickListener(openGetPictureDialog)
            avatarView.setOnClickListener(openGetPictureDialog)

            val array = resources.getPhoneTypesTranslation().values.toTypedArray()
            // Адаптер для типа телефона (выбираем enum)
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                array
            )
            phoneTypeAutoComplete.setAdapter(adapter)

            ringtoneEditText.setOnClickListener {
                getTone.launch(RingtoneManager.TYPE_RINGTONE)
            }
            ringtoneEditText.keyListener = null
        }

        viewModel.uiState.observeFailure(viewLifecycleOwner, {
            openFailureView(it) { failure ->
                when (failure) {
                    is ValidationFailure -> SmallFailureInfo(
                        retryClickedCallback = {},
                        text = getString(R.string.error_validation),
                        buttonText = getString(R.string.ok)
                    )
                    else -> null
                }
            }
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
            // Если есть заполненные поля
            if (viewModel.hasUnsavedChanges()) {
                // Предупреждаем о несохраненных изменениях через alertDialog
                makeAlertDialog(
                    title = R.string.contact_unsaved_title,
                    message = R.string.contact_unsaved,
                    positiveCallback = { findNavController().popBackStack() },
                    negativeCallback = { },
                    positiveText = R.string.yes,
                    negativeText = R.string.no
                )
            } else {
                findNavController().popBackStack()
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_contact_edit, menu)

        // Прячем кнопку удаления, если контакт еще не создан
        val deleteVisibility = viewModel.mode != ContactEditViewModel.Mode.CREATE
        menu.findItem(R.id.deleteButton).isVisible = deleteVisibility

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                viewModel.createContact {
                    findNavController().popBackStack()
                    hideKeyboard()
                }
                return true // Отключение кнопки "назад"
            }
            R.id.deleteButton -> {
                deleteContact()
            }
            R.id.inDevelopmentButton -> makeSnackBar(R.string.feature_in_development)
        }
        return super.onOptionsItemSelected(item)
    }

    // Удаление контакта
    // с вызовом уточняющего диалога
    private fun deleteContact() {
        val doContactDeletion = {
            viewModel.deleteContact {
                val action =
                    ContactEditFragmentDirections.actionContactEditFragmentToContactsListFragment()
                findNavController().navigate(action)
            }
        }
        makeAlertDialog(
            title = R.string.contact_deletion_title,
            message = R.string.contact_deletion,
            positiveCallback = { doContactDeletion() },
            negativeCallback = { },
            positiveText = R.string.yes,
            negativeText = R.string.no
        )
    }
}