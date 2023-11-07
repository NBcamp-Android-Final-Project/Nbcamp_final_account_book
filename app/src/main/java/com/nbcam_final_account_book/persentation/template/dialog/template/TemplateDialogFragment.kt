package com.nbcam_final_account_book.persentation.template.dialog.template

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.databinding.TemplateSelectDialogBinding
import com.nbcam_final_account_book.persentation.main.MainViewModel
import com.nbcam_final_account_book.persentation.template.TemplateActivity
import com.nbcam_final_account_book.persentation.template.TemplateActivity.Companion.EXTRA_TEMPLATE_TYPE
import com.nbcam_final_account_book.persentation.template.TemplateType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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

			onItemDeleteClick = { item ->
				deleteTemplate(item)

			}
		)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = TemplateSelectDialogBinding.inflate(inflater, container, false)
		dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
		dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
		initViewModel()
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)


		initView()
//		initViewModel()

	}

	private fun initView() = with(binding) {

		templateSelectRecyclerViewTemplateList.adapter = adapter

		templateBtnAdd.setOnClickListener {
			toTemplateActivity()
		}

		templateTvCancel.setOnClickListener {
			dismiss()
		}

	}

	private fun initViewModel() {
		with(viewModel) {
			dialogLiveTemplateList.observe(viewLifecycleOwner) {
				if (it != null) {

					adapter.submitList(it)
					btnVisibility(it)
					Log.d("템플릿사이즈", it.size.toString())

				} // if
			} // observe


		}
	}

	private fun deleteTemplate(item: TemplateEntity) {
		CoroutineScope(Dispatchers.IO).launch {

			if (!viewModel.cantDelete()) {

				viewModel.removeTemplate(item)
				val default = viewModel.getDefaultTemplate()

				sharedViewModel.updateCurrentTemplateInCo(default)

			}//canDelete
		}//CoroutineScope
	}


	private fun toTemplateActivity() {
		val intent = Intent(requireContext(), TemplateActivity::class.java).apply {
			putExtra(EXTRA_TEMPLATE_TYPE, TemplateType.ADD.name)
		}
		startActivity(intent)
	}

	private fun btnVisibility(list: List<TemplateEntity>) {
        binding.templateBtnAdd.visibility =
            if (viewModel.getTemplateSizeInTemplateDialog(list)) View.VISIBLE else View.INVISIBLE

		binding.templateIvPaid.visibility =
			if (viewModel.getTemplateSizeInTemplateDialog(list)) View.GONE else View.VISIBLE

		binding.templateTvSub.visibility =
			if (viewModel.getTemplateSizeInTemplateDialog(list)) View.GONE else View.VISIBLE
	}

	private fun canDelete(list: List<TemplateEntity>): Boolean {
		Log.d("삭제", list.size.toString())
		return viewModel.cantDelete(list)

	}

	private fun replaceAlterDialog(item: TemplateEntity) {
		val builder = AlertDialog.Builder(context)
		builder.setTitle("템플릿 변경")
		builder.setMessage("${item.templateTitle}으로 이동하시겠습니까??")
		builder.setNegativeButton("예") { _, _ ->

			sharedViewModel.updateCurrentTemplate(item)

			Toast.makeText(requireContext(), "${item.templateTitle}", Toast.LENGTH_SHORT).show()

		}
		builder.setPositiveButton("아니오") { dialog, _ ->
			dialog.dismiss()
		}
		val dialog = builder.create()
		dialog.show()
	}

}