package com.nbcam_final_account_book.persentation.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.databinding.HomeBottomSheetBinding
import com.nbcam_final_account_book.persentation.entry.EntryActivity
import com.nbcam_final_account_book.persentation.entry.EntryFragment
import com.nbcam_final_account_book.persentation.entry.EntryModel
import com.nbcam_final_account_book.persentation.main.MainViewModel

class HomeBottomSheetFragment(
    private val entries: List<EntryEntity>,
    private val clickedDate: String,
    private val onAddClickListener: (() -> Unit)? = null
) : BottomSheetDialogFragment() {

    private val sharedViewModel: MainViewModel by activityViewModels()

    private var _binding: HomeBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "BasicBottomModalSheet"
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = EntryAdapter(entries)
        adapter.onEditClickListener  = { entryEntity ->
            // 바텀시트 닫기
            dismiss()
            val intent = Intent(context, EntryActivity::class.java).apply {
                putExtra(EntryActivity.EXTRA_ENTRY, entryEntity)
                putExtra(HomeFragment.EXTRA_CURRENT_TEMPLATE, sharedViewModel.getCurrentTemplate())
            }
            startActivity(intent)
        }

        binding.recyclerViewEntryList.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewEntryList.adapter = adapter

        binding.ivBottomSheetAdd.setOnClickListener {
            dismiss()
            onAddClickListener?.invoke()
        }

        // 선택한 날짜를 TextView에 설정합니다.
        binding.tvSelectedDate.text = clickedDate
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

