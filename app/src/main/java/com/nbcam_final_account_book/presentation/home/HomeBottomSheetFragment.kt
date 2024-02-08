package com.nbcam_final_account_book.presentation.home

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.view.Window
import android.widget.TextView
import android.graphics.Color
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.databinding.HomeBottomSheetBinding
import com.nbcam_final_account_book.presentation.entry.EntryActivity
import com.nbcam_final_account_book.presentation.main.MainViewModel

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

    private fun showDeleteDialog(entryEntity: EntryEntity) {
        val deleteDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_delete_entry, null)
        val deleteDialog = Dialog(requireContext()).apply {
            // 배경을 투명하게 설정
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            // 타이틀 바 없애기
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            // 레이아웃 설정
            setContentView(deleteDialogView)
        }

        deleteDialogView.findViewById<TextView>(R.id.tv_dialog_delete).setOnClickListener {
            sharedViewModel.deleteEntry(entryEntity.id)
            deleteDialog.dismiss()
            dismiss() // 바텀시트 닫기
        }

        deleteDialogView.findViewById<TextView>(R.id.tv_dialog_cancel).setOnClickListener {
            deleteDialog.dismiss()
        }

        deleteDialog.show()

        deleteDialog.window?.let { window ->
            val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            window.setLayout(width, height)
        }
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

        adapter.onDeleteClickListener = { entryEntity ->
            showDeleteDialog(entryEntity)
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

