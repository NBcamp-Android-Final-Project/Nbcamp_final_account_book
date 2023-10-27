package com.nbcam_final_account_book.persentation.tag

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.nbcam_final_account_book.databinding.FragmentDialogBinding


class DialogFragment : DialogFragment() {

	private var _binding: FragmentDialogBinding? = null
	private val binding get() = _binding!!

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

		}
	}

	fun deleteItem() {

	}
}