package com.nbcam_final_account_book.persentation.tag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.databinding.TagFragmentBinding


class TagFragment : Fragment() {
    private var _binding: TagFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(
            this@TagFragment
        )[TagViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TagFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {

    }

    private fun initViewModel() = with(viewModel) {
        liveDummyTagInTag.observe(viewLifecycleOwner) {

        }
    }
}