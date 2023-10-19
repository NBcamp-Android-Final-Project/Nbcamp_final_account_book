package com.nbcam_final_account_book.persentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nbcam_final_account_book.databinding.HomeBottomSheetBinding
import com.nbcam_final_account_book.persentation.entry.EntryModel

class HomeBottomSheetFragment(private val entries: List<EntryModel>, private val clickedDate: String) : BottomSheetDialogFragment() {
    private var _binding: HomeBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = EntryAdapter(entries)
        binding.recyclerViewEntryList.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewEntryList.adapter = adapter

        // 선택한 날짜를 TextView에 설정합니다.
        binding.tvSelectedDate.text = clickedDate
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

