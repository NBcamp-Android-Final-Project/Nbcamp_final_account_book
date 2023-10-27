package com.nbcam_final_account_book.persentation.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import com.nbcam_final_account_book.data.model.local.BudgetEntity
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import com.nbcam_final_account_book.persentation.main.MainViewModel
import com.nbcam_final_account_book.unit.Unit
import com.nbcam_final_account_book.unit.Unit.INPUT_TYPE_INCOME
import com.nbcam_final_account_book.unit.Unit.INPUT_TYPE_PAY
import java.security.KeyStore.Entry
import java.text.DecimalFormat


class HomeViewModel(
    private val roomRepo: RoomRepository
) : ViewModel() {


    val homeCurrentLiveEntryList: LiveData<List<EntryEntity>> =
        MainViewModel.liveKey.switchMap { key ->
            roomRepo.getLiveEntryByKey(key)
        } // 이제부터 이 친구를 써주세요!

    val budgetLiveData: LiveData<List<BudgetEntity>> = MainViewModel.liveKey.switchMap { key ->
        roomRepo.getLiveBudgetByKey(key)
    }

    // 설정한 예산의 총합


    fun getListAll(): List<EntryEntity> {
        val list = homeCurrentLiveEntryList.value.orEmpty().toMutableList()
        return list
    }

    fun getTotalBudget(): String {

        val budgetList = budgetLiveData.value.orEmpty()
        val totalBudget = budgetList.sumOf { it.value.toInt() }

        val totalIncomeList = homeCurrentLiveEntryList.value.orEmpty().toMutableList().filter {
            it.type == INPUT_TYPE_INCOME
        }
        val totalIncome = totalIncomeList.sumOf { it.value.toInt() }


        val totalPayList = homeCurrentLiveEntryList.value.orEmpty().toMutableList().filter {
            it.type == INPUT_TYPE_PAY
        }
        val totalPay = totalPayList.sumOf { it.value.toInt() }

        Log.d("예산.초기", budgetLiveData.value.orEmpty()[0].value)

        Log.d("예산.총예산", totalBudget.toString())
        Log.d("예산.수입", totalIncome.toString())
        Log.d("예산.지출", totalPay.toString())

        val result: Int = totalBudget - totalPay + totalIncome
        Log.d("예산", result.toString())

        return result.toString()
    }

    fun getTotalIncomeAndPay(list: List<EntryEntity>): Pair<String, String> {

        val totalPay = list.filter {
            it.type == Unit.INPUT_TYPE_PAY
        }.sumOf { it.value.toLong() }.toString()
        val totalICome = list.filter {
            it.type == Unit.INPUT_TYPE_INCOME
        }.sumOf { it.value.toLong() }.toString()

        val incomeNum = totalICome.replace(",", "").toLong()
        val payNum = totalPay.replace(",", "").toLong()

        // 숫자를 천 단위로 쉼표로 구분하여 포맷
        val decimalFormat = DecimalFormat("#,###")
        val incomeNumFormat = decimalFormat.format(incomeNum)
        val payNumFormat = decimalFormat.format(payNum)


        return Pair(incomeNumFormat, payNumFormat)

    }

    fun getTotalBudget(list: List<EntryEntity>): String {
        val totalBudget = budgetLiveData.value.orEmpty().sumOf { it.value.toLong() }
        Log.d("총예산", totalBudget.toString())

        val totalPay = list.filter {
            it.type == Unit.INPUT_TYPE_PAY
        }.sumOf { it.value.toLong() }
        val totalICome = list.filter {
            it.type == Unit.INPUT_TYPE_INCOME
        }.sumOf { it.value.toLong() }

        val resultBudget = totalBudget + totalICome - totalPay

        // 숫자를 천 단위로 쉼표로 구분하여 포맷
        val decimalFormat = DecimalFormat("#,###")

        return decimalFormat.format(resultBudget)


    }

    fun getTotalPay(): String {
        return homeCurrentLiveEntryList.value.orEmpty().toMutableList().filter {
            it.type == INPUT_TYPE_PAY
        }.sumOf { it.value.toInt() }.toString()


    }

}

class HomeViewModelModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                RoomRepositoryImpl(
                    AndroidRoomDataBase.getInstance(context)
                )
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}