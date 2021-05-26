package com.kode.contacts.presentation.contacts.photo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kode.contacts.R
import com.kode.contacts.databinding.FragmentDialogGetPictureBinding

class GetPictureBottomSheetDialog : BottomSheetDialogFragment() {

    private val binding: FragmentDialogGetPictureBinding by viewBinding(
        FragmentDialogGetPictureBinding::bind
    )

    private var clickListener: GetPictureClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            clickListener =
                parentFragmentManager.primaryNavigationFragment as GetPictureClickListener
        } catch (e: ClassCastException) {
            throw ClassCastException("Calling fragment must implement Callback interface")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_dialog_get_picture, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner

            takePhotoButton.setOnClickListener {
                clickListener?.onTakePicture()
                dismissAllowingStateLoss()
            }

            choosePhotoButton.setOnClickListener {
                clickListener?.onChoosePicture()
                dismissAllowingStateLoss()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        clickListener = null
    }
}