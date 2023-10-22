package com.nbcam_final_account_book.persentation.entry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.FragmentModalBottomBinding
import com.nbcam_final_account_book.persentation.tag.Tag

class ModalBottomFragment : BottomSheetDialogFragment() {
	private val displayInfo by lazy { requireActivity().applicationContext.resources.displayMetrics }   // 디스플레이 높이 정보

	private lateinit var binding: FragmentModalBottomBinding

	private val tagListAdapter by lazy {
		ModalTagListAdapter(onItemClick = { position ->
			onItemClickEvent(position)
		})
	}

	private fun onItemClickEvent(position: Int) {
		Toast.makeText(requireActivity(), "$position 번째 클릭", Toast.LENGTH_SHORT).show()
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentModalBottomBinding.inflate(inflater, container, false)
//		initBottomSheetHeight()

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initView()
		initTag()
	}

	private fun initView() = with(binding) {

		// 마이페이지 - 태그 관리 액티비티로 이동
		ivTagAdd.setOnClickListener {
//			(activity as MainActivity).intentToMore()
//			findNavController().navigate(R.id.action_modalBottomFragment_to_menu_more)
		}

		ivSave.setOnClickListener {
			// Firebase RTDB 에 `태그`, `결제 수단`, `메모`, `금액` 저장 후, ModalBottomSheet 및 EntryActivity 종료
		}

		ivCancel.setOnClickListener {
			dismiss()
		}
	}

	private fun initTag() {

		// 임시 데이터
		val newList = mutableListOf<Tag>()
		newList.apply {
			add(Tag(R.drawable.ic_tag, "달력"))
			add(Tag(R.drawable.ic_chart, "차트"))
			add(Tag(R.drawable.ic_help, "도움"))
			add(Tag(R.drawable.ic_home, "집"))
			add(Tag(R.drawable.ic_lock, "잠금"))
			add(Tag(R.drawable.ic_more_vert, "수직"))
			add(Tag(R.drawable.ic_mypage, "페이지"))
			add(Tag(R.drawable.ic_backup, "백업"))
			add(Tag(R.drawable.ic_check, "확인"))
			add(Tag(R.drawable.ic_check, "확인"))
			add(Tag(R.drawable.ic_check, "확인"))
			add(Tag(R.drawable.ic_check, "확인"))
			add(Tag(R.drawable.ic_check, "확인"))
			add(Tag(R.drawable.ic_check, "확인"))
			add(Tag(R.drawable.ic_check, "확인"))
			add(Tag(R.drawable.ic_check, "확인"))
		}

		binding.rvTagContainer.adapter = tagListAdapter
		tagListAdapter.submitList(newList)
	}

	// 기기 디스플레이 높이 기준으로 ModalBottomSheet 높이 계산
	private fun initBottomSheetHeight() {

		view?.viewTreeObserver?.addOnGlobalLayoutListener(object :
			OnGlobalLayoutListener {
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
							else 0.4
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
		val TAG = ModalBottomFragment::class.simpleName
	}

	fun interface IntentToMore {
		fun addFragment()
	}

	var intentToMore: IntentToMore? = null
}

