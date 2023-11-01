package com.nbcam_final_account_book.persentation.chart

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import com.nbcam_final_account_book.persentation.main.MainViewModel
import kotlin.math.roundToInt

class ChartViewModel(
    private val roomRepo: RoomRepository
) : ViewModel() {

    val liveEntryListInChart: LiveData<List<EntryEntity>> = MainViewModel.liveKey.switchMap { key ->
        roomRepo.getLiveEntryByKey(key)
    }


    private val expenses: LiveData<List<ChartTagModel>> = liveEntryListInChart.map { entryList ->
        val categoryMap = mutableMapOf<String, Double>()

        entryList.forEach { entry ->
            // value를 Double로 변환, 변환 불가능 시 0.0 사용
            val amount = entry.value.toDoubleOrNull() ?: 0.0
            categoryMap[entry.tag] = (categoryMap[entry.tag] ?: 0.0) + amount
        }

        categoryMap.map { (tag, amount) ->
            ChartTagModel(tag, amount, getCategoryColor(tag))
        }
    }

    val chartItems: LiveData<List<ChartItem>> = expenses.map { chartTagModels ->
        getChartItems(chartTagModels)
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