package com.nbcam_final_account_book.persentation.firstpage.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.FirstLoginFragmentBinding
import com.nbcam_final_account_book.databinding.FirstSplashFragmentBinding


class SplashFragment : Fragment() {
    private var _binding: FirstSplashFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FirstSplashFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


}