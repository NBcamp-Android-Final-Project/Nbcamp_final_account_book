package com.nbcam_final_account_book.persentation.entry

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.databinding.FragmentEntryBinding
import com.nbcam_final_account_book.persentation.tag.Tag
import com.nbcam_final_account_book.persentation.tag.TagListAdapter
import com.nbcam_final_account_book.unit.Unit.INPUT_TYPE_INCOME
import com.nbcam_final_account_book.unit.Unit.INPUT_TYPE_PAY
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class EntryFragment : Fragment() {

	private var _binding: FragmentEntryBinding? = null
	private val binding get() = _binding!!
	private var tagPosition: Int? = null
	private val viewModel: EntryViewModel by activityViewModels()
	private var shouldShow: Boolean = true

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
		_binding = FragmentEntryBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initView()
		initTag()
		compactCalendar()
		editTextFormat()
	}

	private fun initView() = with(binding) {

		ivBack.setOnClickListener {

		}

		// 마이페이지 - 태그 관리 액티비티로 이동
		ivTagAdd.setOnClickListener {
			findNavController().navigate(R.id.action_entryFragment_to_tagFragment2)
		}

		tvDateInput.setOnClickListener {
			if (!compactcalendarView.isAnimating) {
				if (shouldShow) {
					compactcalendarView.showCalendarWithAnimation()
				} else {
					compactcalendarView.hideCalendarWithAnimation()
				}
				shouldShow = !shouldShow
			}
		}

		edtAmount.setOnClickListener {
//			findNavController().navigate(R.id.action_entryFragment_to_entryDetailFragment)
		}

		ivSave.setOnClickListener {
			// Firebase RTDB 에 `태그`, `결제 수단`, `메모`, `금액` 저장 후, ModalBottomSheet 및 EntryActivity 종료
			val title = edtTitle.text.toString()
			val tag = tagPosition ?: 0
			val payment = tabLayoutPayment.selectedTabPosition
			val finance = tabLayoutFinance.selectedTabPosition
			val description = edtDescription.text.toString()
			val amount = edtAmount.text.toString()
			val currentTemplate = getCurrentTemplateEntry()
			Log.d("현재 템플릿", currentTemplate.toString())

			val dateTimeList = listOf(
				"2023-10-25",
				"2023-10-24",
				"2023-10-23",
				"2023-10-10",
				"2023-10-11",
				"2023-10-12",
				"2023-10-13",
				"2023-10-14",
			)

			val payTagList = listOf(
				"식비",
				"교통비",
				"의료비",
				"쇼핑"
			)
			val inComeList = listOf(
				"월급",
				"용돈",
				"당근",
				"불로소득"
			)

			val dayListSize = dateTimeList.size
			val payTagListSize = payTagList.size
			val inComeListSize = inComeList.size

			val dayRandomIndex = Random.nextInt(0, dayListSize)
			val payRandomIndex = Random.nextInt(0, payTagListSize)
			val inComeRandomIndex = Random.nextInt(0, inComeListSize)

			val dateTime = dateTimeList[dayRandomIndex]
			val payTag = payTagList[payRandomIndex]
			val incomeTag = inComeList[inComeRandomIndex]

			//TODO 여기 날짜 랜덤으로 넣을 수 있도록 해두겠습니다

			if (edtAmount.text.isNotEmpty()  && currentTemplate != null) {
				if (finance == 0) {
					val entryEntity = EntryEntity(
						id = 0,
						key = currentTemplate.id,
						type = INPUT_TYPE_PAY,
						dateTime = dateTime,
						value = amount,
						tag = incomeTag,
						title = title,
						description = description
					)
					insertEntry(entryEntity)
					requireActivity().finish()
				} else {
					val entryEntity = EntryEntity(
						id = 0,
						key = currentTemplate.id,
						type = INPUT_TYPE_INCOME,
						dateTime = dateTime,
						value = amount,
						tag = payTag,
						title = title,
						description = description
					)
					insertEntry(entryEntity)
					requireActivity().finish()
				}


			}


		}
	}

	private fun getCurrentTemplateEntry(): TemplateEntity? {
		return viewModel.getCurrentTemplateEntry()
	}

	private fun insertEntry(item: EntryEntity) {
		viewModel.insertEntity(item)
	}

	private fun getData(): Pair<String?, String?> {
		return viewModel.getData()
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

	private fun editTextFormat() {
		val decimalFormat = DecimalFormat("#,###")
		var result: String = ""

		binding.edtAmount.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

			override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
				if (!TextUtils.isEmpty(charSequence.toString()) && charSequence.toString() != result) {
					result =
						decimalFormat.format(charSequence.toString().replace(",", "").toDouble())
					binding.edtAmount.setText(result)
					binding.edtAmount.setSelection(result.length)
				}
			}

			override fun afterTextChanged(p0: Editable?) {}
		})
	}

	private fun compactCalendar() {

		binding.compactcalendarView.apply {
			hideCalendar()
			setFirstDayOfWeek(Calendar.SUNDAY)
			setListener(object : CompactCalendarView.CompactCalendarViewListener {
				override fun onDayClick(dateClicked: Date?) {
					binding.tvDateInput.text = dateClicked?.let { dateFormat.format(it) }
				}

				override fun onMonthScroll(firstDayOfNewMonth: Date?) {}
			})
		}
	}

	private val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일") //화면에 보이는거
	private val dateFormatSave = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA) //저장 되는 것


	companion object {
		val TAG = EntryFragment::class.simpleName
	}
}