package com.nbcam_final_account_book.persentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.databinding.HomeFragmentBinding
import com.nbcam_final_account_book.persentation.entry.EntryActivity
import com.nbcam_final_account_book.persentation.main.MainViewModel
import com.nbcam_final_account_book.unit.Unit
import java.util.Calendar


class HomeFragment : Fragment(), SpinnerDatePickerDialog.OnDateSetListener {

    companion object {
        const val EXTRA_CURRENT_TEMPLATE = "extra_current_template"
    }


    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private var days = mutableListOf<Day>()
    private var currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
    private var currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)
    private var currentDay: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    private val viewModel by lazy {
        ViewModelProvider(
            this@HomeFragment,
            HomeViewModelModelFactory(requireContext())
        )[HomeViewModel::class.java]
    }
    private val sharedViewModel: MainViewModel by activityViewModels()

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
                    viewModel.homeCurrentLiveEntryList.value?.filter {
                        it.dateTime.contains(
                            clickedDate
                        )
                    }
                        ?: listOf()

                val bottomSheetFragment =
                    HomeBottomSheetFragment(relatedEntries, clickedDate)
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

    private fun updateDays(entries: List<EntryEntity>? = null) {
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
        adapter.notifyDataSetChanged()
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
        updateDays(viewModel.homeCurrentLiveEntryList.value)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initView()


    }

    private fun initView() = with(binding) { //레이아웃 제어
        viewModel.homeCurrentLiveEntryList.observe(viewLifecycleOwner) { entries ->
            updateDays(entries)
        }

        fab.setOnClickListener {
            val intent = EntryActivity.newIntent(requireActivity()).apply {
                putExtra(EXTRA_CURRENT_TEMPLATE, getCurrentTemplate())
            }
            startActivity(intent)
//			overridePendingTransition(
//				R.anim.slide_up_enter,
//				R.anim.slide_up_exit
//			)  // overrideActivityTransition 으로 변경 예정
        }


    }

    private fun getTotalBudget(): String {
        return viewModel.getTotalBudget()
    }


    private fun getCurrentTemplate(): TemplateEntity? {
        return sharedViewModel.getCurrentTemplate()
    }

    // HomeFragment 내부
    private fun initViewModel() {
        // 여기서 바텀시트를 표시하는 로직을 제거합니다. 다른 로직이 있다면 그대로 두시면 됩니다.
        with(viewModel) {
            homeCurrentLiveEntryList.observe(viewLifecycleOwner) { it ->
                if (it != null) {
                    setIncomeAndPay(it)
                }
            }
            budgetLiveData.observe(viewLifecycleOwner) { it ->
                val totalBudget =
                    homeCurrentLiveEntryList.value.orEmpty().sumOf { it.value.toInt() }

                val totalPay = homeCurrentLiveEntryList.value.orEmpty().filter {
                    it.type == Unit.INPUT_TYPE_PAY
                }.sumOf { it.value.toInt() }

                val totalICome = homeCurrentLiveEntryList.value.orEmpty().filter {
                    it.type == Unit.INPUT_TYPE_INCOME
                }.sumOf { it.value.toInt() }

                val resultBudget = totalBudget - totalPay - totalICome


                binding.tvBalanceDescription.text = totalBudget.toString()

            }
        }

        with(sharedViewModel) {

        }

    }

    private fun setIncomeAndPay(list:List<EntryEntity>){
        val income = viewModel.getTotalIncomeAndPay(list).first
        val pay = viewModel.getTotalIncomeAndPay(list).second

        binding.payTitleTxt.text = pay
        binding.incomeTvTitle.text = income
    }


}