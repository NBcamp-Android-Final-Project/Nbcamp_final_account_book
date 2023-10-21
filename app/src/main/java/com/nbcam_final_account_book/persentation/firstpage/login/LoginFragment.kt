package com.nbcam_final_account_book.persentation.firstpage.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.FirstLoginFragmentBinding
import com.nbcam_final_account_book.databinding.TemplateBudgetFragmentBinding


class LoginFragment : Fragment() {

    private var _binding: FirstLoginFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FirstLoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {

    }


}