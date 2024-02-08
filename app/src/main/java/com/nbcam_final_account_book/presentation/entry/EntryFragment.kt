package com.nbcam_final_account_book.presentation.entry

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.databinding.FragmentEntryBinding
import com.nbcam_final_account_book.unit.Unit.INPUT_TYPE_INCOME
import com.nbcam_final_account_book.unit.Unit.INPUT_TYPE_PAY
import java.text.DecimalFormat
import java.util.Calendar
import java.util.Date

class EntryFragment : Fragment() {

	private var _binding: FragmentEntryBinding? = null
	private val binding get() = _binding!!
	private val viewModel: EntryViewModel by activityViewModels()

	// 기존 엔트리의 ID 저장 변수
	private var existingEntryId: Long = 0

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentEntryBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initViewModel()
		initView()
		editTextFormat()

		// EntryEntity 객체를 받아서 뷰 초기화
		arguments?.getSerializable("entry")?.let {
			if (it is EntryEntity) {
				existingEntryId = it.id // 기존 엔트리의 ID 저장
				initViewWithEntryData(it)
			}
		}
	}

	private fun initViewWithEntryData(entry: EntryEntity) {
		binding.edtDate.setText(entry.dateTime)
		binding.edtAmount.setText(entry.value)
		binding.edtTag.setText(entry.tag)
		binding.edtTitle.setText(entry.title)
		binding.edtDescription.setText(entry.description)
	}

	private fun initView() = with(binding) {

		ivBack.setOnClickListener {
			requireActivity().finish()
		}

		edtDate.setOnClickListener {
			showDatePicker()
		}

		edtTag.setOnClickListener {
			val bottomSheet = EntryModalFragment()
			bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
		}

		tvSave.setOnClickListener {
			// Firebase RTDB 에 `태그`, `결제 수단`, `메모`, `금액` 저장 후, ModalBottomSheet 및 EntryActivity 종료

			val tabPosition = tabLayoutPayment.selectedTabPosition
			val payment =
				if (tabPosition == 0) "Card" else "Cash"            // 카드 <-> 현금
			val finance = tabLayoutFinance.selectedTabPosition      // 지출 <-> 수입
			val date = edtDate.text.toString()                  // 날짜
			val amount = edtAmount.text.toString().replace(",", "")     // 금액
			val tag = edtTag.text.toString()                    // 카테고리
			val title = edtTitle.text.toString()                    // 제목
			val description = edtDescription.text.toString()        // 메모
			val currentTemplate = getCurrentTemplateEntry()         // 템플릿
			Log.d("현재 템플릿", currentTemplate.toString())

			if (date.isNotEmpty() && amount.isNotEmpty() && tag.isNotEmpty() && title.isNotEmpty() && currentTemplate != null) {
				if (finance == 0) {
					val entryEntity = EntryEntity(
						id = existingEntryId, // 기존 엔트리인 경우 기존 id, 새 엔트리인 경우 0
						key = currentTemplate?.id ?: "",
						type = if (finance == 0) INPUT_TYPE_PAY else INPUT_TYPE_INCOME,
						dateTime = date,
						value = amount,
						tag = tag,
						title = title,
						description = description
					)

					// 기존 엔트리 수정 또는 새 엔트리 추가
					if (existingEntryId != 0L) {
						updateEntry(entryEntity) // 기존 엔트리 수정
					} else {
						insertEntry(entryEntity) // 새 엔트리 추가
					}

					requireActivity().finish()
				} else {
					val entryEntity = EntryEntity(
						id = existingEntryId, // 기존 엔트리인 경우 기존 id, 새 엔트리인 경우 0
						key = currentTemplate?.id ?: "",
						type = if (finance == 0) INPUT_TYPE_PAY else INPUT_TYPE_INCOME,
						dateTime = date,
						value = amount,
						tag = tag,
						title = title,
						description = description
					)

					// 기존 엔트리 수정 또는 새 엔트리 추가
					if (existingEntryId != 0L) {
						updateEntry(entryEntity) // 기존 엔트리 수정
					} else {
						insertEntry(entryEntity) // 새 엔트리 추가
					}

					requireActivity().finish()
				}
			}

			if (date.isEmpty()) binding.edtDate.error = "날짜를 선택해 주세요!"
			if (amount.isEmpty()) binding.edtAmount.error = "금액을 입력해 주세요!"
			if (tag.isEmpty()) binding.edtTag.error = "카테고리를 선택해 주세요!"
			if (title.isEmpty()) binding.edtTitle.error = "제목를 입력해 주세요!"
		}
	}

	private fun initViewModel() {
		viewModel.category.observe(viewLifecycleOwner) {
			binding.edtTag.setText(it)
		}
	}

	private fun getCurrentTemplateEntry(): TemplateEntity? {
		return viewModel.getCurrentTemplateEntry()
	}

	private fun insertEntry(item: EntryEntity) {
		viewModel.insertEntity(item)
	}

	fun updateEntry(item: EntryEntity) {
		viewModel.updateEntity(item)
	}

	private fun getData(): Pair<String?, String?> {
		return viewModel.getData()
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

	private fun showDatePicker() {

		val datePicker =
			MaterialDatePicker.Builder.datePicker()
				.setTitleText("Select date")
				.setSelection(MaterialDatePicker.todayInUtcMilliseconds())
				.build()

		datePicker.show(requireActivity().supportFragmentManager, "fragment")

		datePicker.addOnPositiveButtonClickListener {
			val calendar = Calendar.getInstance()
			calendar.time = Date(it)
			val year = calendar.get(Calendar.YEAR)
			val month = calendar.get(Calendar.MONTH) + 1
			val day = calendar.get(Calendar.DAY_OF_MONTH)
			val formattedDate = formatDate(year, month, day)


			binding.edtDate.setText(formattedDate)
		}

	}

	private fun formatDate(year: Int, month: Int, day: Int): String {
		val formattedMonth = if (month < 10) "0$month" else month.toString()
		val formattedDay = if (day < 10) "0$day" else day.toString()
		return "$year-$formattedMonth-$formattedDay"
	}

	companion object {
		val TAG = EntryFragment::class.simpleName
	}
}