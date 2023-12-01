package com.chaty.app.messages_screen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.chaty.app.base.BaseFragment
import com.chaty.app.databinding.FragmentContactBinding
import com.chaty.app.messages_screen.adapters.ContactAdapter
import com.chaty.app.messages_screen.state.ContactUiState
import com.chaty.app.messages_screen.viewmodel.ContactVM
import kotlinx.coroutines.launch

class ContactFragment : BaseFragment() {
    private var _binding: FragmentContactBinding? = null
    private val vm: ContactVM by viewModels()
    private val adapter by lazy { ContactAdapter() }
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentContactBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeUiState()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            vm.uiState.collect {
                when (it) {
                    is ContactUiState.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    is ContactUiState.Idle -> hideLoading()
                    is ContactUiState.Loading -> showLoading()
                    is ContactUiState.Success -> {
                        hideLoading()
                        adapter.submitList(it.data)
                    }
                }
            }

        }
    }

    private fun setupUI() {
        binding.apply {
            rvContacts.adapter = adapter
            rvContacts.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "ContactFragment"
    }
}