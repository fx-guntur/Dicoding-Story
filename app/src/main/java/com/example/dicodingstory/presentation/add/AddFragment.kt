package com.example.dicodingstory.presentation.add

import android.Manifest
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.dicodingstory.R
import com.example.dicodingstory.base.BaseLocationFragment
import com.example.dicodingstory.databinding.FragmentAddBinding
import com.example.dicodingstory.domain.common.ResultStatus
import com.example.dicodingstory.utils.helper.compressImage
import com.example.dicodingstory.utils.helper.getFileFromUri
import com.example.dicodingstory.utils.helper.getImageUri
import com.example.dicodingstory.utils.helper.observe
import com.example.dicodingstory.utils.helper.showSnackBar
import com.example.dicodingstory.utils.helper.uriToFile
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class AddFragment : BaseLocationFragment<FragmentAddBinding>() {
    private var image: File? = null
    private var tempUri: Uri? = null
    private var myLocation: Location? = null
    private var allowLocation: Boolean = false

    private val addStoryViewModel: AddStoryViewModel by viewModel()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentAddBinding {
        return FragmentAddBinding.inflate(inflater, container, false)
    }

    override fun initUI() {
        tempUri?.let {
            binding.holderPhotoIv.visibility = View.GONE
            binding.photoIv.visibility = View.VISIBLE
            binding.photoIv.setImageURI(it)
        }

        binding.apply {
            holderPhotoIv.setOnClickListener {
                initCamera()
            }

            photoIv.setOnClickListener {
                initCamera()
            }

            uploadBtn.setOnClickListener {
                initProcess()
            }

            mapBtn.setOnClickListener {
                initPermissionAndGetLocation()
                allowLocation = true
                getLocation {
                    myLocation = it
                    binding.apply {
                        latitudeTv.text = it.latitude.toString()
                        longitudeTv.text = it.longitude.toString()
                    }
                }
            }
        }
    }

    override fun initProcess() {
        val desc = binding.descriptionEditText.text.toString()
        when {
            desc.isEmpty() -> binding.descriptionEditText.error =
                getString(R.string.label_description_empty)

            tempUri == null -> binding.root.showSnackBar(getString(R.string.label_photo_empty))
            else -> {
                uploadStory(desc)
            }
        }
    }

    private fun uploadStory(storyDescription: String) {
        tempUri?.let { uri ->
            image = uri.uriToFile(requireContext()).compressImage()

            observe(
                addStoryViewModel.addStory(
                    image!!,
                    storyDescription,
                    latitude = if (allowLocation) myLocation?.latitude?.toDouble() else null,
                    longitude = if (allowLocation) myLocation?.longitude?.toDouble() else null
                ),
                ::uploadResultStatus
            )
        }
    }

    private fun uploadResultStatus(resultStatus: ResultStatus<String>) {
        when (resultStatus) {
            is ResultStatus.Loading -> {
                showProgressBar(true)
            }

            is ResultStatus.Success -> {
                showProgressBar(false)
                setFragmentResult(
                    IS_UPLOAD_SUCCESS_KEY,
                    bundleOf(IS_UPLOAD_SUCCESS_BUNDLE to true)
                )
                binding.root.showSnackBar(
                    getString(R.string.dialogue_upload_success)
                )
                backToMain()
            }

            is ResultStatus.Error -> {
                showProgressBar(false)
                binding.root.showSnackBar(resultStatus.message)
            }
        }
    }

    private fun initCamera() {
        Dexter.withContext(requireActivity())
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    imagePickerOption()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            }).withErrorListener {
                binding.root.showSnackBar(getString(R.string.dialogue_permission_denied))
            }.onSameThread()
            .check()
    }

    private fun imagePickerOption() {
        AlertDialog.Builder(requireActivity())
            .setTitle(context?.getString(R.string.label_image_picker_method))
            .setItems(R.array.pictures_method) { _, p1 ->
                if (p1 == 0) {
                    cameraRegistration.launch()
                } else {
                    getUriImageFromFile.launch("image/*")
                }
            }.create().show()
    }

    private val cameraRegistration =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                val uri = requireActivity().getImageUri(bitmap)
                tempUri = uri
                image = requireActivity().getFileFromUri(uri)
                binding.holderPhotoIv.visibility = View.GONE
                binding.photoIv.visibility = View.VISIBLE
                binding.photoIv.setImageBitmap(bitmap)
            }
        }

    private val getUriImageFromFile =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                tempUri = uri
                image = requireActivity().getFileFromUri(uri)
                binding.holderPhotoIv.visibility = View.GONE
                binding.photoIv.visibility = View.VISIBLE
                binding.photoIv.setImageURI(uri)
            }
        }


    private fun showProgressBar(status: Boolean) {
        binding.progressBar4.visibility = if (status) View.VISIBLE else View.GONE
    }

    private fun backToMain() {
        findNavController().popBackStack()
    }

    companion object {
        const val IS_UPLOAD_SUCCESS_BUNDLE = "is_upload_success_bundle"
        const val IS_UPLOAD_SUCCESS_KEY = "is_upload_success_key"
    }
}