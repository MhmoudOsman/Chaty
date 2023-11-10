package com.chaty.app.auth.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.chaty.app.auth.viewmodels.CreateAccountVM
import com.chaty.app.base.BaseFragment
import com.chaty.app.databinding.FragmentCreateAccountBinding

class CreateAccountFragment : BaseFragment() {
    private var _binding: FragmentCreateAccountBinding? = null
    private val binding get() = _binding!!
    private val vm : CreateAccountVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateAccountBinding.inflate(layoutInflater,container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeUiState()
        binding.apply {

        }
    }

    private fun setupUi() {
        binding.apply {

        }
    }

    private fun observeUiState() {


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}