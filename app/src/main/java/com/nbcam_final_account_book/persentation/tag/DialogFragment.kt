package com.nbcam_final_account_book.persentation.tag

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.data.model.local.TagEntity
import com.nbcam_final_account_book.databinding.FragmentDialogBinding

class DialogFragment(
	private val context: TagFragment,
	private val position: Int,
	private val item: TagEntity
) :
	DialogFragment() {

	private var _binding: FragmentDialogBinding? = null
	private val binding get() = _binding!!

	private val viewModel by lazy {
		ViewModelProvider(
			this,
			TagViewModelFactory(requireActivity())
		)[TagViewModel::class.java]
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentDialogBinding.inflate(inflater, container, false)
		dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
		dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initView()
	}

	private fun initView() = with((binding)) {

		tvDialogCancel.setOnClickListener {
			dismiss()
		}

		tvDialogDelete.setOnClickListener {
			deleteTag()
			dismiss()
		}
	}

	private fun deleteTag() {
		viewModel.deleteTag(item.id)
		context.tagManageAdapter.notifyItemRemoved(position)
	}
}