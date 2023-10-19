package com.nbcam_final_account_book.persentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.databinding.HomeFragmentBinding
import java.util.Calendar


class HomeFragment : Fragment(), SpinnerDatePickerDialog.OnDateSetListener {


    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private var days = mutableListOf<Day>()
    private var currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
    private var currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)


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
                val clickedDate = "${currentYear}-" + String.format("%02d", currentMonth + 1) + "-" + String.format("%02d", day)
                val relatedEntries = viewModel.getListAll().filter { it.dateTime.contains(clickedDate) }

                val bottomSheetFragment = HomeBottomSheetFragment(relatedEntries)
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
            // 날짜를 0으로 설정하여 빈 칸을 나타냄
            days.add(Day(0, false))
        }

        val maxDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 1..maxDaysInMonth) {
            days.add(Day(i, i % 5 == 0)) // 예시로 5의 배수 날짜에만 이벤트 표시
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

    }

    private fun initViewModel() = with(viewModel) { //뷰 모델 제어
        liveEntryDummyDataInHome.observe(viewLifecycleOwner) {
        }
    }
}