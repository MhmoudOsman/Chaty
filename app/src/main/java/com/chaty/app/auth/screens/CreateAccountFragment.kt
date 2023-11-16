package com.chaty.app.auth.screens

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.chaty.app.MainActivity
import com.chaty.app.R
import com.chaty.app.auth.state.CreateAccountUiIntent
import com.chaty.app.auth.state.CreateAccountUiState.Error
import com.chaty.app.auth.state.CreateAccountUiState.Idle
import com.chaty.app.auth.state.CreateAccountUiState.LoadSuccess
import com.chaty.app.auth.state.CreateAccountUiState.Loading
import com.chaty.app.auth.state.CreateAccountUiState.SaveSuccess
import com.chaty.app.auth.viewmodels.CreateAccountVM
import com.chaty.app.base.BaseFragment
import com.chaty.app.databinding.FragmentCreateAccountBinding
import com.chaty.app.tools.TextInputValidator
import com.chaty.domain.user.models.UserModel
import kotlinx.coroutines.launch

class CreateAccountFragment : BaseFragment() {
    private var _binding: FragmentCreateAccountBinding? = null
    private var _enableCamera: Boolean = false
    private val binding get() = _binding!!
    private val enableCamera get() = _enableCamera
    private val vm: CreateAccountVM by viewModels()
    private val args: CreateAccountFragmentArgs by navArgs()
    private var profileUri: Uri? = null
    private var userData: UserModel? = null
    private val nameValidator: TextInputValidator by lazy { TextInputValidator(binding.etName) }
    private var imageLauncher = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            profileUri = result.uriContent
            binding.imgProfile.setImageURI(profileUri)
        } else {
            // An error occurred.
            result.error?.printStackTrace()
        }
    }
    private var cameraLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            _enableCamera = granted
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkPermission(Manifest.permission.CAMERA)) {
            cameraLauncher.launch(Manifest.permission.CAMERA)
        } else {
            _enableCamera = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateAccountBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeUiState()

    }

    private fun setupUi() {
        binding.apply {
            btnChoosePic.setOnClickListener {
                picImage()
            }
            btnConfirm.setOnClickListener {
                nameValidator.validate()
                saveUser()

            }
        }
    }

    private fun saveUser() {
        if (nameValidator.isValid()) {
            val name = binding.etName.editText?.text.toString()
            val info = binding.etInfo.editText?.text.toString()
            val image = userData?.image ?: ""
            val phone = args.phone
            lifecycleScope.launch {
                profileUri?.let {
                    vm.userIntent.send(
                        CreateAccountUiIntent.SaveUserWithPic(
                            name,
                            info,
                            it,
                            phone
                        )
                    )
                } ?: vm.userIntent.send(
                    CreateAccountUiIntent.SaveUser(
                        name,
                        info,
                        image,
                        phone
                    )
                )
            }

        } else return
    }

    private fun picImage() {
        imageLauncher.launch(
            CropImageContractOptions(
                null, CropImageOptions(
                    aspectRatioX = 1,
                    aspectRatioY = 1,
                    outputCompressQuality = 100,
                    fixAspectRatio = true,
                    imageSourceIncludeCamera = enableCamera,
                    imageSourceIncludeGallery = true,
                    autoZoomEnabled = true,
                    outputCompressFormat = Bitmap.CompressFormat.PNG,
                    guidelines = CropImageView.Guidelines.ON,
                    cropShape = CropImageView.CropShape.OVAL,
                    cornerShape = CropImageView.CropCornerShape.OVAL
                )
            )
        )

    }

    private fun observeUiState() {
        lifecycleScope.launch {
            vm.uiState.collect { state ->
                when (state) {
                    is Idle -> {
                        hideLoading()
                    }

                    is Loading -> {
                        showLoading()
                    }

                    is Error -> {
                        hideLoading()
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }

                    is LoadSuccess -> {
                        hideLoading()
                        userData = state.user
                        userData?.let {
                            binding.apply {
                                etName.editText?.setText(it.name)
                                etInfo.editText?.setText(it.info)
                                imgProfile.load(it.image) {
                                    crossfade(true)
                                    error(R.drawable.img_profile_pic)
                                }
                            }
                        }
                    }

                    is SaveSuccess -> {
                        hideLoading()
                        (requireActivity() as MainActivity).refreshActivity()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}