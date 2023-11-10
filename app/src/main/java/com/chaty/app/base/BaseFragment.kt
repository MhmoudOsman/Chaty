package com.chaty.app.base

import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment : Fragment() {
    private val progressDialog = ProgressDialog()

    fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
    fun showLoading() {
        progressDialog.showDialog(childFragmentManager)
    }
    fun hideLoading() {
        progressDialog.dismissDialog()
    }

}