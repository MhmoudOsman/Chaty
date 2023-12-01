package com.chaty.app.auth.screens

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chaty.app.R
import com.chaty.app.auth.dialogs.OtpDialog
import com.chaty.app.auth.state.AuthErrorsCode.INVALID_PHONE_NUMBER
import com.chaty.app.auth.state.AuthErrorsCode.INVALID_VERIFICATION_CODE
import com.chaty.app.auth.state.AuthErrorsCode.NO_INTERNET_CONNECTION
import com.chaty.app.auth.state.AuthErrorsCode.UNKNOWN
import com.chaty.app.auth.state.AuthUiIntent
import com.chaty.app.auth.state.AuthUiState
import com.chaty.app.auth.viewmodels.LoginVM
import com.chaty.app.base.BaseFragment
import com.chaty.app.databinding.FragmentLoginBinding
import com.chaty.domain.auth.models.PhoneAuthOptionsModel
import com.google.firebase.auth.PhoneAuthProvider
import com.hbb20.CountryCodePicker
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val otpDialog by lazy { OtpDialog.newInstance() }
    private val vm: LoginVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeUiState()
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            vm.uiState.collect {
                when (it) {
                    is AuthUiState.Idle -> {
                        hideLoading()
                    }

                    is AuthUiState.Loading -> {
                        showLoading()
                    }

                    is AuthUiState.Error -> {
                        hideLoading()
                        when (it.code) {
                            INVALID_VERIFICATION_CODE -> {
                                if (otpDialog.isShowing) {
                                    otpDialog.onCodeError(getString(R.string.error_invalid_verification_code))
                                }
                            }

                            INVALID_PHONE_NUMBER -> {
                                binding.etPhone.isErrorEnabled = true
                                binding.etPhone.error = getString(R.string.error_invalid_phone_number)
                            }
                            NO_INTERNET_CONNECTION -> {
                                Toast.makeText(requireContext(),R.string.error_no_internet_connection, Toast.LENGTH_LONG).show()
                            }

                            UNKNOWN -> {
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                            }

                        }
                    }

                    is AuthUiState.LoginSuccess -> {
                        hideLoading()
                        otpDialog.hideProgress()
                        otpDialog.dismissDialog()
                        val directions =
                            LoginFragmentDirections.actionLoginToCreateAccount(binding.ccp.fullNumberWithPlus)
                        vm.userIntent.send(AuthUiIntent.Clear)
                        findNavController().navigate(directions)
                    }

                    is AuthUiState.SendOTPSuccess -> {
                        hideLoading()
                        otpDialog.hideProgress()
                        showOtpDialog(it.verificationId, it.token)
                    }
                }
            }
        }
    }


    private fun setupUi() {
        binding.apply {
            ccp.setTypeFace(ResourcesCompat.getFont(requireContext(), R.font.rubik))
            ccp.registerCarrierNumberEditText(binding.etPhone.editText)
            etPhone.prefixText = binding.ccp.selectedCountryCodeWithPlus
            ccp.setPhoneNumberValidityChangeListener {
                if (it) {
                    etPhone.isErrorEnabled = false
                    etPhone.error = null
                }
            }
            ccp.setDialogEventsListener(object : CountryCodePicker.DialogEventsListener {
                override fun onCcpDialogOpen(dialog: Dialog?) {
                }

                override fun onCcpDialogDismiss(dialogInterface: DialogInterface?) {
                    etPhone.prefixText = binding.ccp.selectedCountryCodeWithPlus
                }

                override fun onCcpDialogCancel(dialogInterface: DialogInterface?) {
                    etPhone.prefixText = binding.ccp.selectedCountryCodeWithPlus
                }

            })

            btnNext.setOnClickListener {
                lifecycleScope.launch {
                    if (ccp.isValidFullNumber) {
                        vm.userIntent.send(
                            AuthUiIntent.SendOTP(
                                PhoneAuthOptionsModel(
                                    ccp.fullNumberWithPlus,
                                    requireActivity()
                                )
                            )
                        )
                    } else {
                        etPhone.error = getString(R.string.error_invalid_phone_number)
                        etPhone.isErrorEnabled = true
                    }
                }
            }
        }

    }

    private fun showOtpDialog(
        verificationId: String,
        token: PhoneAuthProvider.ForceResendingToken
    ) {
        otpDialog.onVerifyClick = {
            otpDialog.showProgress()
            lifecycleScope.launch {
                vm.userIntent.send(AuthUiIntent.VerifyOTP(verificationId, it))
            }
        }
        otpDialog.onResendCodeClick = {
            otpDialog.showProgress()
            lifecycleScope.launch {
                vm.userIntent.send(
                    AuthUiIntent.SendOTP(
                        PhoneAuthOptionsModel(
                            binding.ccp.fullNumberWithPlus,
                            requireActivity(),
                            token
                        )
                    )
                )
            }
        }
        otpDialog.showDialog(childFragmentManager)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}