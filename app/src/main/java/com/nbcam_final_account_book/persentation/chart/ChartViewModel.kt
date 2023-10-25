package com.nbcam_final_account_book.persentation.chart

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import com.nbcam_final_account_book.data.model.DummyData
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import com.nbcam_final_account_book.persentation.entry.EntryModel
import com.nbcam_final_account_book.persentation.home.HomeViewModel
import com.nbcam_final_account_book.persentation.main.MainViewModel

class ChartViewModel(
    private val roomRepo: RoomRepository
) : ViewModel() {


    val liveEntryListInChart: LiveData<List<EntryEntity>> = MainViewModel.liveKey.switchMap { key ->
        roomRepo.getLiveEntryByKey(key)
    }


}

class v(
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