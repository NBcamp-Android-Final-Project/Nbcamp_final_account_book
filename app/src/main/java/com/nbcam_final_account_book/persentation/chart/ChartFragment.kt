package com.nbcam_final_account_book.persentation.chart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.databinding.ChartFragmentBinding


class ChartFragment : Fragment() {


    private var _binding: ChartFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this
        )[ChartViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChartFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() = with(binding) { // 레이아웃 제어

    }

    private fun initViewModel() = with(viewModel) { //뷰 모델 제어
        liveDummyEntryListInChart.observe(viewLifecycleOwner){

        }

    }

}