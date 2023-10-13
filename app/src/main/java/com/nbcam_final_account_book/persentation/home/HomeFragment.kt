package com.nbcam_final_account_book.persentation.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.databinding.HomeFragmentBinding


class HomeFragment : Fragment() {


    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this@HomeFragment
        )[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        Log.d("호출", binding.root.toString())
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) { //레이아웃 제어

    }

    private fun initViewModel() = with(viewModel) { //뷰 모델 제어

    }

}