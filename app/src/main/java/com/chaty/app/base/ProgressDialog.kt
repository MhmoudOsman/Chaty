package com.chaty.app.base

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.chaty.app.R

class ProgressDialog : DialogFragment() {
    private var _isShowing = false
    val isShowing get() =  _isShowing

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_progress, container, false)
    }


    override fun onStart() {
        super.onStart()
        dialog?.apply {
            setCanceledOnTouchOutside(false)
            window?.apply {
                setLayout(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setBackgroundDrawableResource(android.R.color.transparent)
            }
        }
    }

    fun showDialog(manager: FragmentManager) {
        if (!isShowing) show(manager, TAG)
    }

    fun dismissDialog() {
        if (isShowing) dismiss()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        _isShowing = true
        super.show(manager, tag)
    }

    override fun onDismiss(dialog: DialogInterface) {
        _isShowing = false
        super.onDismiss(dialog)
    }
    override fun onCancel(dialog: DialogInterface) {
        _isShowing = false
        super.onCancel(dialog)
    }

    companion object {
        const val TAG = "ProgressDialog"

        private var instance: ProgressDialog? = null

        @JvmStatic
        fun newInstance(): ProgressDialog {
            if (instance == null) {
                instance = ProgressDialog()
            }
            return instance!!
        }
    }
}