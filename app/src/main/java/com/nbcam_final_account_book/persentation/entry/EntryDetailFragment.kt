package com.nbcam_final_account_book.persentation.entry

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.FragmentEntryDetailBinding
import com.nbcam_final_account_book.persentation.tag.Tag
import com.nbcam_final_account_book.persentation.tag.TagListAdapter

class EntryDetailFragment : Fragment() {

	private var _binding: FragmentEntryDetailBinding? = null
	private val binding get() = _binding!!
	private var tagPosition: Int? = null

	private val tagListAdapter by lazy {
		TagListAdapter(onItemClick = { position ->
			onItemClickEvent(position)
		})
	}

	private fun onItemClickEvent(position: Int) {
		Toast.makeText(requireActivity(), "$position 번째 클릭", Toast.LENGTH_SHORT).show()
		tagPosition = position
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentEntryDetailBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initView()
		initTag()
	}

	private fun initView() = with(binding) {

		ivBack.setOnClickListener {

		}

		// 마이페이지 - 태그 관리 액티비티로 이동
		ivTagAdd.setOnClickListener {

		}

		ivSave.setOnClickListener {
			// Firebase RTDB 에 `태그`, `결제 수단`, `메모`, `금액` 저장 후, ModalBottomSheet 및 EntryActivity 종료
			val title = edtTitle.text
			val tag = tagPosition ?: 0
			val tablayout = tabLayout.selectedTabPosition
			val description = edtDescription.text

			Log.d("detail", "$title, $tag, $tablayout, $description")
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

	companion object {
		val TAG = EntryDetailFragment::class.simpleName
	}
}