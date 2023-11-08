package com.nbcam_final_account_book.persentation.shared

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.SharedTemplateFragmentBinding


class SharedTemplateFragment : Fragment() {

    private var _binding: SharedTemplateFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this@SharedTemplateFragment,
            SharedTemplateViewModelFactory(requireContext())
        )[SharedTemplateViewModel::class.java]
    }

    private val adapter by lazy {
        UserListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SharedTemplateFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }


    private fun initView() = with(binding) {

        sharedRecyclerUserList.adapter = adapter

    }

    private fun initViewModel() {
        with(viewModel) {

        }
    }


}