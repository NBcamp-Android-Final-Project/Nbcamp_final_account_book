package com.nbcam_final_account_book.persentation.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import com.nbcam_final_account_book.data.model.DummyData.liveDummyEntry
import com.nbcam_final_account_book.data.model.local.BudgetEntity
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import com.nbcam_final_account_book.persentation.entry.EntryModel
import com.nbcam_final_account_book.persentation.main.MainViewModel


class HomeViewModel(
    private val roomRepo: RoomRepository
) : ViewModel() {
//    val liveEntryDummyDataInHome: LiveData<List<EntryModel>> get() = liveDummyEntry

    val budgetLiveData: LiveData<List<BudgetEntity>> = MainViewModel.liveKey.switchMap { key ->
        roomRepo.getLiveBudgetByKey(key)
    }

    // 설정한 예산의 총합
    val totalBudget = budgetLiveData.value.orEmpty().sumOf { it.value.toInt() }.toString()

    val homeCurrentLiveEntryList : LiveData<List<EntryEntity>> = MainViewModel.liveKey.switchMap { key->
        roomRepo.getLiveEntryByKey(key)
    } // 이제부터 이 친구를 써주세요!



//    fun getListAll(): List<EntryModel> {
//        val list = liveDummyEntry.value.orEmpty().toMutableList()
//        return list
//    }

    fun getListAll(): List<EntryEntity> {
        val list = homeCurrentLiveEntryList.value.orEmpty().toMutableList()
        return list
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