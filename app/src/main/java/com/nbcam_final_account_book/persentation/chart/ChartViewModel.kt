package com.nbcam_final_account_book.persentation.chart

import android.content.Context
import android.util.Log
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
import com.nbcam_final_account_book.persentation.main.MainViewModel
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

    private val expenses: MutableLiveData<List<ChartTagModel>> = MutableLiveData()

    val chartItems: LiveData<List<ChartItem>> = expenses.map { chartTagModels ->
        getChartItems(chartTagModels)
    }

    fun setExpenses(list: List<ChartTagModel>) {
        expenses.value = list
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

    fun setToday() {
        val today = Calendar.getInstance()
        _dateRange.value = Pair(today, today)
    }

    fun setThisWeekRange() {
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

        Log.d("날짜.일주일.리스트", datesList.toString())

    }

    fun setThisMonthRange() {
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

        Log.d("날짜.월.리스트", datesList.toString())

    }

    fun setDateRange(startDate: Calendar, endDate: Calendar) {
        _dateRange.value = Pair(startDate, endDate)
    }

}

val colorMap = mapOf(
    "식비" to Color(0xFFA0FFA1),
    "교통" to Color(0xFF8BE5F5),
    "취미" to Color(0xFFE6FFA1),
    "쇼핑" to Color(0xFFF2AAFF),
    "통신" to Color(0xFF629CFF),
    "주거" to Color(0xFFB2FFC2),
    "할부" to Color(0xFF6ABEFF),
    "보험" to Color(0xFFF2AAF2),
    "미용" to Color(0xFFA0FFC6),
    "경조사" to Color(0xFF99C6FF),
    "의료" to Color(0xFFE6FFC6),
    "월급" to Color(0xFFF2C6AA),
    "부수입" to Color(0xFFA0FFE6),
    "상여" to Color(0xFFFFE6FF),
    "용돈" to Color(0xFFD9FFF0),
    "기타" to Color(0xFFD1D1D1)
)

fun getCategoryColor(tag: String): Color {
    return colorMap[tag] ?: Color.Gray // 찾는 태그가 없으면 기본 색상을 회색으로 반환
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