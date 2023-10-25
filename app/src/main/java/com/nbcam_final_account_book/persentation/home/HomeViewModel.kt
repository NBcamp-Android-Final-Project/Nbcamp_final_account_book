package com.nbcam_final_account_book.persentation.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import com.nbcam_final_account_book.data.model.DummyData.liveDummyEntry
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import com.nbcam_final_account_book.persentation.entry.EntryModel
import com.nbcam_final_account_book.persentation.main.MainViewModel


class HomeViewModel(
    private val roomRepo: RoomRepository
) : ViewModel() {
    val liveEntryDummyDataInHome: LiveData<List<EntryModel>> get() = liveDummyEntry

    private var key: String? = null

    // repo에 있는 함수를 통해 key값으로 리스트들을 livedata로 받습니다.
    // 현재 key값을 livedata로 받을 방법을 고민중이니 임시로 이 형태로 사용해주세요.

    val homeLiveEntryList: LiveData<List<EntryEntity>> get() = roomRepo.getAllLiveEntry()

    val homeCurrentLiveEntryList : LiveData<List<EntryEntity>> = MainViewModel.liveKey.switchMap { key->
        roomRepo.getEntryByKey(key)
    } // 이제부터 이 친구를 써주세요!

    fun updateKey(inputKey:String){
        key = inputKey
    }


    fun getListAll(): List<EntryModel> {
        val list = liveDummyEntry.value.orEmpty().toMutableList()
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