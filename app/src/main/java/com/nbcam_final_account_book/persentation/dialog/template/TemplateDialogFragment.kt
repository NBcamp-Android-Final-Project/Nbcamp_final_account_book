package com.nbcam_final_account_book.persentation.dialog.template

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.databinding.TemplateSelectDialogBinding
import com.nbcam_final_account_book.persentation.main.MainViewModel
import com.nbcam_final_account_book.persentation.template.TemplateActivity

class TemplateDialogFragment() : DialogFragment() {

    private var _binding: TemplateSelectDialogBinding? = null
    private val binding get() = _binding!!


    private val viewModel by lazy {
        ViewModelProvider(
            this,
            TemplateDialogViewModelFactory(
                requireContext()
            )
        )[TemplateDialogViewModel::class.java]
    }

    private val sharedViewModel: MainViewModel by activityViewModels()

    private val adapter by lazy {
        TemplateDialogAdapter(
            onItemClick = { item ->
                replaceAlterDialog(item)
            },
            onItemLongClick = {item->
                viewModel.removeTemplate(item)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TemplateSelectDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initView()
        initViewModel()

    }

    private fun initView() = with(binding) {

        templateSelectRecyclerViewTemplateList.adapter = adapter

        templateBtnAdd.setOnClickListener {
            toTemplateActivity()
        }

    }

    private fun initViewModel() {
        with(viewModel) {
            dialogLiveTemplateList.observe(viewLifecycleOwner) {
                if (it != null) {

                    adapter.submitList(it)
                    btnVisibility(it)
                    Log.d("다이얼로그사이즈", it.size.toString())

                } // if
            } // observe


        }
    }


    private fun toTemplateActivity() {
        val intent = Intent(requireContext(), TemplateActivity::class.java)
        startActivity(intent)
    }

    private fun btnVisibility(list: List<TemplateEntity>) {
        binding.templateBtnAdd.visibility =
            if (viewModel.getTemplateSizeInTemplateDialog(list)) View.VISIBLE else View.INVISIBLE
    }

    private fun replaceAlterDialog(item: TemplateEntity) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("템플릿 변경")
        builder.setMessage("${item.templateTitle}으로 이동하시겠습니까??")
        builder.setNegativeButton("예") { _, _ ->

            sharedViewModel.updateCurrentTemplate(item)

        }
        builder.setPositiveButton("아니오") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

}