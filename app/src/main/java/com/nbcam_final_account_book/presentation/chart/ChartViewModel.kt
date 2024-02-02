package com.nbcam_final_account_book.presentation.chart

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import com.nbcam_final_account_book.presentation.main.MainViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

class ChartViewModel(
    private val roomRepo: RoomRepository
) : ViewModel() {

    val liveEntryListInChart: LiveData<List<EntryEntity>> = MainViewModel.liveKey.switchMap { key ->
        roomRepo.getLiveEntryByKey(key)
    }

    private val _liveFilterData: MutableLiveData<List<ChartFilterModel>> = MutableLiveData()
    val liveFilterData: LiveData<List<ChartFilterModel>> get() = _liveFilterData

   val expenses: LiveData<List<ChartTagModel>> = liveFilterData.map { it->
        val categoryMap = mutableMapOf<String, Double>()

       it.forEach { filter ->
            // value를 Double로 변환, 변환 불가능 시 0.0 사용
            val amount = filter.amount?: 0.0
            categoryMap[filter.name] = (categoryMap[filter.name] ?: 0.0) + amount
        }

        categoryMap.map { (tag, amount) ->
            ChartTagModel(tag, amount, getCategoryColor(tag))
        }
    }


    val chartItems: LiveData<List<ChartItem>> = expenses.map { chartTagModels ->
        getChartItems(chartTagModels)
    }

    fun firstExpense(list:List<ChartFilterModel>){
        setliveFilterData(list)

        val today = Calendar.getInstance()
        _dateRange.value = Pair(today, today)

        val todayDate = LocalDate.of(
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH) + 1,
            today.get(Calendar.DAY_OF_MONTH)
        ).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val currentList = liveFilterData.value.orEmpty().toMutableList()
        val resultList: MutableList<ChartFilterModel> = mutableListOf()

        for (item in currentList) {

            if (item.day == todayDate) {
                resultList.add(item)
            }

        }

        _liveFilterData.value = resultList
    }

    fun setliveFilterData(list: List<ChartFilterModel>) {
        _liveFilterData.value = list
    }

    private fun getChartItems(chartTagModels: List<ChartTagModel>): List<ChartItem> {
        val totalAmount = chartTagModels.sumByDouble { it.amount }
        return chartTagModels.map { chartTagModel ->
            ChartItem(
                name = chartTagModel.name,
                amount = chartTagModel.amount.roundToInt(),
                percentage = calculatePercentage(chartTagModel.amount, totalAmount)
            )
        }
    }

    private fun calculatePercentage(amount: Double, totalAmount: Double): Int {
        if (totalAmount == 0.0) return 0
        return ((amount / totalAmount) * 100).roundToInt()
    }

    // 날짜 관련 LiveData
    private val _dateRange = MutableLiveData<Pair<Calendar, Calendar>>()
    val dateRange: LiveData<Pair<Calendar, Calendar>> = _dateRange

    val dateRangeText: LiveData<String> = dateRange.map { (startDate, endDate) ->
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        if (startDate == endDate) {
            // 오늘이 선택되었을 때
            dateFormat.format(startDate.time)
        } else {
            // 기간이 선택되었을 때
            "${dateFormat.format(startDate.time)} ~ ${dateFormat.format(endDate.time)}"
        }
    }

    init {
        setToday()
    }

    fun entryListToExpense(list:List<EntryEntity>) : List<ChartFilterModel>{
        val currentExpenses = list.map {
            ChartFilterModel(
                name = it.tag,
                amount = it.value.toDouble(),
                color = getCategoryColor(it.tag),
                day = it.dateTime
            )
        }

        return currentExpenses
    }

    fun setToday() {
        val currentEntryList = liveEntryListInChart.value.orEmpty()
        val currentExpenses = entryListToExpense(currentEntryList)

        setliveFilterData(currentExpenses)
        val today = Calendar.getInstance()
        _dateRange.value = Pair(today, today)

        val todayDate = LocalDate.of(
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH) + 1,
            today.get(Calendar.DAY_OF_MONTH)
        ).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val currentList = liveFilterData.value.orEmpty().toMutableList()
        val resultList: MutableList<ChartFilterModel> = mutableListOf()

        for (item in currentList) {

                if (item.day == todayDate) {
                    resultList.add(item)
                }

        }

        _liveFilterData.value = resultList
    }

    fun setThisWeekRange() {
        val currentEntryList = liveEntryListInChart.value.orEmpty()
        val currentExpenses = entryListToExpense(currentEntryList)

        setliveFilterData(currentExpenses)

        val now = Calendar.getInstance()
        val startDate = now.clone() as Calendar
        startDate.set(Calendar.DAY_OF_WEEK, startDate.firstDayOfWeek)
        val endDate = startDate.clone() as Calendar
        endDate.add(Calendar.WEEK_OF_YEAR, 1)
        endDate.add(Calendar.DAY_OF_MONTH, -1)
        _dateRange.value = Pair(startDate, endDate)

        val datesList = mutableListOf<String>()
        val startLocalDate = LocalDate.of(
            startDate.get(Calendar.YEAR),
            startDate.get(Calendar.MONTH) + 1,
            startDate.get(Calendar.DAY_OF_MONTH)
        )
        val endLocalDate = LocalDate.of(
            endDate.get(Calendar.YEAR),
            endDate.get(Calendar.MONTH) + 1,
            endDate.get(Calendar.DAY_OF_MONTH)
        )

        var currentDate = startLocalDate
        while (!currentDate.isAfter(endLocalDate)) {
            datesList.add(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            currentDate = currentDate.plusDays(1)
        }

        val currentList = liveFilterData.value.orEmpty().toMutableList()
        val resultList: MutableList<ChartFilterModel> = mutableListOf()

        for (item in currentList) {
            for (day in datesList) {
                if (item.day == day) {
                    resultList.add(item)
                }
            }
        }

        _liveFilterData.value = resultList

    }

    fun setThisMonthRange() {
        val currentEntryList = liveEntryListInChart.value.orEmpty()
        val currentExpenses = entryListToExpense(currentEntryList)

        setliveFilterData(currentExpenses)

        val now = Calendar.getInstance()
        val startDate = now.clone() as Calendar
        startDate.set(Calendar.DAY_OF_MONTH, 1)
        val endDate = startDate.clone() as Calendar
        endDate.add(Calendar.MONTH, 1)
        endDate.add(Calendar.DAY_OF_MONTH, -1)
        _dateRange.value = Pair(startDate, endDate)

        val datesList = mutableListOf<String>()
        val startLocalDate = LocalDate.of(
            startDate.get(Calendar.YEAR),
            startDate.get(Calendar.MONTH) + 1,
            startDate.get(Calendar.DAY_OF_MONTH)
        )
        val endLocalDate = LocalDate.of(
            endDate.get(Calendar.YEAR),
            endDate.get(Calendar.MONTH) + 1,
            endDate.get(Calendar.DAY_OF_MONTH)
        )

        var currentDate = startLocalDate
        while (!currentDate.isAfter(endLocalDate)) {
            datesList.add(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            currentDate = currentDate.plusDays(1)
        }

        val currentList = liveFilterData.value.orEmpty().toMutableList()
        val resultList: MutableList<ChartFilterModel> = mutableListOf()

        for (item in currentList) {
            for (day in datesList) {
                if (item.day == day) {
                    resultList.add(item)
                }
            }
        }

        _liveFilterData.value = resultList

    }

    fun setDateRange(startDate: Calendar, endDate: Calendar) {
        val currentEntryList = liveEntryListInChart.value.orEmpty()
        val currentExpenses = entryListToExpense(currentEntryList)

        setliveFilterData(currentExpenses)
        _dateRange.value = Pair(startDate, endDate)

        val datesList = mutableListOf<String>()
        val startLocalDate = LocalDate.of(
            startDate.get(Calendar.YEAR),
            startDate.get(Calendar.MONTH) + 1,
            startDate.get(Calendar.DAY_OF_MONTH)
        )
        val endLocalDate = LocalDate.of(
            endDate.get(Calendar.YEAR),
            endDate.get(Calendar.MONTH) + 1,
            endDate.get(Calendar.DAY_OF_MONTH)
        )

        var currentDate = startLocalDate
        while (!currentDate.isAfter(endLocalDate)) {
            datesList.add(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            currentDate = currentDate.plusDays(1)
        }


        val currentList = liveFilterData.value.orEmpty().toMutableList()
        val resultList: MutableList<ChartFilterModel> = mutableListOf()

        for (item in currentList) {
            for (day in datesList) {
                if (item.day == day) {
                    resultList.add(item)
                }
            }
        }

        _liveFilterData.value = resultList
    }

}

val colorMap = mapOf(
    "식비" to Color(0xFFf37a32),
    "교통" to Color(0xFFca9232),
    "취미" to Color(0xFF34af8c),
    "쇼핑" to Color(0xFFf66ab7),
    "통신" to Color(0xFF629CFF),
    "주거" to Color(0xFF9fa131),
    "할부" to Color(0xFF39a7d5),
    "보험" to Color(0xFF32b25c),
    "미용" to Color(0xFFA0FFC6),
    "경조사" to Color(0xFFcc7af4),
    "의료" to Color(0xFF36ada4),
    "월급" to Color(0xFFca9232),
    "부수입" to Color(0xFF6e9bf4),
    "상여" to Color(0xFFbb9832),
    "용돈" to Color(0xFFf77468 ),
    "기타" to Color(0xFF9CC1E7)
)

fun getCategoryColor(tag: String): Color {
    return colorMap[tag] ?: Color(0xFF9CC1E7) // 찾는 태그가 없으면 기본 색상을 회색으로 반환
}

class ChartViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChartViewModel::class.java)) {
            return ChartViewModel(
                RoomRepositoryImpl(
                    AndroidRoomDataBase.getInstance(context)
                )
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}