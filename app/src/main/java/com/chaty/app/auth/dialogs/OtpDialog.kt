package com.chaty.app.auth.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.chaty.app.databinding.DialogOtpBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OtpDialog : DialogFragment() {

    private var _isShowing = false
    val isShowing get() = _isShowing
    private var _binding: DialogOtpBinding? = null
    private val binding get() = _binding!!

    var onVerifyClick: ((String) -> Unit)? = null
    var onResendCodeClick: (() -> Unit)? = null
    var onCodeError: ((String?) -> Unit) = {}
    var showProgress: (() -> Unit) = {}
    var hideProgress: (() -> Unit) = {}
    fun showDialog(manager: FragmentManager) {
        if (!isShowing) show(manager, TAG)
    }

    fun dismissDialog() {
        if (isShowing) dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogOtpBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()

    }

    private fun setupUI() {
        binding.apply {
            btnResendCode.isEnabled = false
            enableResendCode()
            loading.hide()
            onCodeError = {
                vOtp.text?.clear()
                vOtp.setError(it)
                loading.hide()
            }
            showProgress = {
                loading.show()
            }
            hideProgress = {
                loading.hide()
            }
            btnVerify.setOnClickListener {
                onVerifyClick?.invoke(vOtp.text.toString())
            }
            btnResendCode.setOnClickListener {
                onResendCodeClick?.invoke()
                btnResendCode.isEnabled = false
                enableResendCode()
            }
        }

    }

    private fun enableResendCode() {
        lifecycleScope.launch {
            delay(3000)
            binding.btnResendCode.isEnabled = true
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            window?.apply {
                setBackgroundDrawableResource(android.R.color.transparent)
            }
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        _isShowing = true
        super.show(manager, tag)
    }

    override fun onDismiss(dialog: DialogInterface) {
        _isShowing = false
        binding.vOtp.text?.clear()
        super.onDismiss(dialog)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "OtpDialog"

        @JvmStatic
        fun newInstance(): OtpDialog {
            return OtpDialog()
        }
    }
}