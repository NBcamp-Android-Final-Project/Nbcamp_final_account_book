package com.nbcam_final_account_book.persentation.template.dialog.template

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.graphics.Canvas
import android.graphics.Color
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nbcam_final_account_book.R
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

	private val sharedViewModel: MainViewModel by activityViewModels()
	private val viewModel by lazy {
		ViewModelProvider(
			this,
			TemplateDialogViewModelFactory(
				requireContext()
			)
		)[TemplateDialogViewModel::class.java]
	}

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

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setStyle(STYLE_NO_TITLE, R.style.EditNameAlertDialogStyle)
	}

	override fun onStart() {
		super.onStart()

		val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
		val height = ViewGroup.LayoutParams.WRAP_CONTENT
		dialog?.window?.setLayout(width, height)
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
		templateSelectRecyclerViewTemplateList.hasFixedSize()
		templateSelectRecyclerViewTemplateList.addItemDecoration(object : DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL) {
			override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
				val divider = ColorDrawable(Color.parseColor("#343434"))
				val left = parent.paddingLeft
				val right = parent.width - parent.paddingRight

				for (i in 0 until parent.childCount - 1) {
					val child = parent.getChildAt(i)
					val params = child.layoutParams as RecyclerView.LayoutParams
					val top = child.bottom + params.bottomMargin
					val bottom = top + divider.intrinsicHeight
					divider.setBounds(left, top, right, bottom)
					divider.draw(c)
				}
			}
		})

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
	}

	private fun canDelete(list: List<TemplateEntity>): Boolean {
		Log.d("삭제", list.size.toString())
		return viewModel.cantDelete(list)
	}

	private fun replaceAlterDialog(item: TemplateEntity) {
		val builder = androidx.appcompat.app.AlertDialog.Builder(requireActivity(), R.style.EditNameAlertDialogStyle)
		builder.setTitle("테플릿 이동")
		builder.setMessage("${item.templateTitle}으로 이동하시겠습니까??")
		builder.setPositiveButton("확인") { dialog, _ ->
			sharedViewModel.updateCurrentTemplate(item)
			Toast.makeText(requireContext(), "${item.templateTitle}", Toast.LENGTH_SHORT).show()
			dialog.dismiss()
			dismiss()
		}
		builder.setNegativeButton("취소") { dialog, _ ->
			dialog.dismiss()
		}
		val dialog = builder.create()
		dialog.show()
	}
}