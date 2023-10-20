package com.nbcam_final_account_book.persentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.data.model.DummyData
import com.nbcam_final_account_book.databinding.HomeFragmentBinding
import com.nbcam_final_account_book.persentation.entry.EntryActivity
import java.util.Calendar


class HomeFragment : Fragment(), SpinnerDatePickerDialog.OnDateSetListener {


	private var _binding: HomeFragmentBinding? = null
	private val binding get() = _binding!!
	private var days = mutableListOf<Day>()
	private var currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
	private var currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)
	private var currentDay: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)


	private val viewModel by lazy {
		ViewModelProvider(
			this@HomeFragment
		)[HomeViewModel::class.java]
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {

		_binding = HomeFragmentBinding.inflate(inflater, container, false)

		updateCalendarHeader()
		updateDays()

		// 이전/다음 버튼 클릭 리스너 설정
		binding.ivPrev.setOnClickListener {
			currentMonth--
			if (currentMonth < 0) {
				currentMonth = 11
				currentYear--
			}
			updateCalendarHeader()
			updateDays()
		}

		binding.ivNext.setOnClickListener {
			currentMonth++
			if (currentMonth > 11) {
				currentMonth = 0
				currentYear++
			}
			updateCalendarHeader()
			updateDays()
		}

		binding.gridCalendar.setOnItemClickListener { parent, view, position, id ->
			val day = days[position].date
			if (day != 0) {
				val clickedDate = "${currentYear}-" + String.format(
					"%02d",
					currentMonth + 1
				) + "-" + String.format("%02d", day)
				val relatedEntries =
					DummyData.liveDummyEntry.value?.filter { it.dateTime.contains(clickedDate) }
						?: listOf()

				val bottomSheetFragment = HomeBottomSheetFragment(relatedEntries, clickedDate)
				bottomSheetFragment.show(parentFragmentManager, "BottomSheetFragment")
			}
		}


		// DatePickerDialog 보여주기
		binding.tvMonthYear.setOnClickListener {
			showDatePickerDialog()
		}
		return binding.root
	}

	private fun updateCalendarHeader() {
		binding.tvMonthYear.text = "${currentYear}년 ${currentMonth + 1}월"
	}

	private fun updateDays() {
		days.clear()

		val calendar = Calendar.getInstance().apply {
			set(Calendar.YEAR, currentYear)
			set(Calendar.MONTH, currentMonth)
			set(Calendar.DAY_OF_MONTH, 1)
		}
		val firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK)

		// 월의 첫 번째 날짜가 무슨 요일인지에 따라 빈 칸을 추가
		for (i in 1 until firstDayOfMonth) {
			days.add(Day(0, false))
		}

		val maxDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

		for (i in 1..maxDaysInMonth) {
			val currentDate = "${currentYear}-" + String.format(
				"%02d",
				currentMonth + 1
			) + "-" + String.format("%02d", i)
			val hasEvent = viewModel.getListAll().any { it.dateTime == currentDate }
			days.add(Day(i, hasEvent))
		}

		val adapter = CalendarAdapter(requireContext(), days)
		binding.gridCalendar.adapter = adapter
	}


	private fun showDatePickerDialog() {
		val datePickerDialog = SpinnerDatePickerDialog()
		datePickerDialog.setOnDateSetListener(this)
		datePickerDialog.show(parentFragmentManager, "DatePickerDialog")
	}

	override fun onDateSet(year: Int, month: Int) {
		currentYear = year
		currentMonth = month
		updateCalendarHeader()
		updateDays()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initView()
		initViewModel()
	}

	private fun initView() = with(binding) { //레이아웃 제어
		fab.setOnClickListener {
			val intent = EntryActivity.newIntent(requireActivity())
			startActivity(intent)
//			overridePendingTransition(
//				R.anim.slide_up_enter,
//				R.anim.slide_up_exit
//			)  // overrideActivityTransition 으로 변경 예정
		}
	}

	// HomeFragment 내부
	private fun initViewModel() = with(viewModel) {
		// 여기서 바텀시트를 표시하는 로직을 제거합니다. 다른 로직이 있다면 그대로 두시면 됩니다.
		DummyData.liveDummyEntry.observe(viewLifecycleOwner) { entries ->
			// 바텀시트 표시 로직 제거
		}
	}
}