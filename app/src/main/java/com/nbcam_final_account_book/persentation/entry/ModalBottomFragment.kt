package com.nbcam_final_account_book.persentation.entry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nbcam_final_account_book.databinding.FragmentModalBottomBinding
import com.nbcam_final_account_book.persentation.tag.TagRecyclerAdapter

class ModalBottomFragment : BottomSheetDialogFragment() {

	private lateinit var binding: FragmentModalBottomBinding
	private val displayInfo by lazy { requireActivity().applicationContext.resources.displayMetrics }
	private val adapter by lazy { TagRecyclerAdapter() }

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentModalBottomBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initView()
		initBottomSheetHeight()
		initTag()
	}

	private fun initView() = with(binding) {

		ivTagAdd.setOnClickListener {
//			findNavController().navigate(R.id.action_modalBottomFragment_to_tagFragment)
		}

		ivSave.setOnClickListener {
//			val entry = EntryActivity()
//			val amount: String? = entry.findViewById<EditText>(R.id.edt_num).text?.toString()
//			Log.d("amount", "$amount")

			dismiss()
		}
	}

	private fun initTag() {
		binding.rvTagContainer.adapter = adapter
	}

	private fun initBottomSheetHeight() {

		view?.viewTreeObserver?.addOnGlobalLayoutListener(object :
			ViewTreeObserver.OnGlobalLayoutListener {
			override fun onGlobalLayout() {

				view!!.viewTreeObserver.removeOnGlobalLayoutListener(this)

				val dialog = dialog as BottomSheetDialog
				val bottomSheet =
					dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
				val behavior = BottomSheetBehavior.from(bottomSheet!!)
				behavior.state = BottomSheetBehavior.STATE_EXPANDED
				val newHeight =
					activity?.window?.decorView?.measuredHeight?.minus(
						activity?.window?.decorView?.measuredHeight?.times(
							if (displayInfo.heightPixels > 2000) 0.25
							else 0.35
						)!!
					)
				val viewGroupLayoutParams = bottomSheet.layoutParams
				viewGroupLayoutParams.height = newHeight?.toInt() ?: 0
				bottomSheet.layoutParams = viewGroupLayoutParams
			}
		})
	}

	override fun onDestroy() {
		super.onDestroy()
		view?.viewTreeObserver?.addOnGlobalLayoutListener(null)
	}

	companion object {
		const val TAG = "ModalBottomSheet"
	}
}